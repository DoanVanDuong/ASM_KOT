package com.ph46500.assignment_ph46500.ViewModel

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ph46500.assignment_ph46500.APIServices.RetrofitClient
import com.ph46500.assignment_ph46500.Model.Cart
import com.ph46500.assignment_ph46500.Model.Order
import com.ph46500.assignment_ph46500.Model.Product
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

@SuppressLint("StaticFieldLeak")
class ProductViewModel: ViewModel() {
    var productList= mutableStateOf<List<Product>>(emptyList())
        private set

    var selectedProduct = mutableStateOf<Product?>(null)
        private set

    var filteredProductList = mutableStateOf<List<Product>>(emptyList())
        private set

    var orderList = mutableStateOf<List<Order>>(emptyList())
        private set

    var searchQuery = mutableStateOf("")

    var cartItems = mutableStateOf<List<Cart>>(emptyList())


    init {
        fetchProducts()
    }

    private fun fetchProducts() {
        viewModelScope.launch (Dispatchers.IO){

            try {
                val products = RetrofitClient.apiService.getProducts()
                productList.value=products
                filteredProductList.value = products
            }catch (e:Exception){
                productList.value= emptyList()
                filteredProductList.value = emptyList()
            }
        }
    }

    // Hàm để lấy chi tiết sản phẩm
    fun fetchProductDetail(productId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val product = RetrofitClient.apiService.getProductDetail(productId)
                selectedProduct.value = product
            } catch (e: Exception) {
                selectedProduct.value = null
            }
        }
    }

    fun filterProductsByCategory(categoryId: String) {
        val filteredProducts = productList.value.filter { product ->
            product.category == categoryId
        }
        filteredProductList.value = filteredProducts
    }

    //Seacrh
    fun searchProducts(query: String) {
        searchQuery.value = query

        viewModelScope.launch(Dispatchers.IO) {
            try {
                if (query.isNotEmpty()) {
                    val searchResults = RetrofitClient.apiService.searchProducts(query)
                    filteredProductList.value = searchResults
                } else {
                    filteredProductList.value = productList.value
                }
            } catch (e: Exception) {
                filteredProductList.value = emptyList()
            }
        }
    }

    //getTokenFromSharedPreferences
    fun getTokenFromSharedPreferences(context: Context): String? {
        val sharedPref: SharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        return sharedPref.getString("token", null)
    }

    //addToCart
    fun addToCart(context: Context, token: String, productId: String, quantity: Int) {
        val url = "http://192.168.1.240:3000/products/addToCart"
//        val url = "http://172.20.10.8:3000/products/addToCart"
        val jsonObject = JSONObject().apply {
            put("productId", productId)
            put("quantity", quantity)
        }

        val client = OkHttpClient()

        val requestBody = jsonObject.toString().toRequestBody("application/json".toMediaTypeOrNull())
        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .addHeader("Authorization", "Bearer $token")
            .addHeader("Content-Type", "application/json")
            .build()

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = client.newCall(request).execute()
                if (response.isSuccessful) {
                    fetchCart(context)
                    Log.d("TAG", "addToCart: Product added to cart successfully: ${response.body?.string()}")
                } else {
                    Log.d("TAG", "addToCart: Failed to add product to cart: ${response.message}")
                }
            } catch (e: Exception) {
                Log.d("TAG", "addToCart: Error: ${e.localizedMessage}")
            }
        }
    }//end

    //fetchCart
    fun fetchCart(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val token = getTokenFromSharedPreferences(context)

                if (token != null) {
                    val items = RetrofitClient.apiService.getCart("Bearer $token")
                    cartItems.value = items

                    for (item in items) {
                        Log.d("TAG", "Product ID: ${item.product._id}, Quantity: ${item.quantity}")
                    }
                } else {
                    Log.d("TAG", "No authentication token found")
                    cartItems.value = emptyList()
                }
            } catch (e: Exception) {
                Log.e("TAG", "Error fetching cart: ${e.message}")
                cartItems.value = emptyList()
            }
        }
    }

    fun ClearCart(context: Context) {
        viewModelScope.launch {
            try {
                val token = getTokenFromSharedPreferences(context)
                if (token != null) {
                    val response = RetrofitClient.apiService.clearCart("Bearer $token")
                    if (response.isSuccessful) {
                        cartItems.value = emptyList()
                        Log.d("TAG", "ClearCart: Clear Cart Successfully!")
                    } else {
                        Log.d("TAG", "clearCart: Failed to clear cart: ${response.message()}")
                    }
                }
            } catch (e: Exception) {
                Log.e("TAG", "ClearCart: err to clearing cart: ${e.localizedMessage}")
                e.printStackTrace()
            }
        }
    }

    //Checkout
// Checkout
    fun checkout(context: Context, order: Order) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val token = getTokenFromSharedPreferences(context)
                if (token != null) {
                    val response = RetrofitClient.apiService.checkout("Bearer $token", order)
                    if (response.isSuccessful) {
                        // Clear the cart items
                        cartItems.value = emptyList()
                        Log.d("TAG", "Checkout successful: ${response.body()?.toString()}")
                    } else {
                        Log.d("TAG", "Checkout failed: ${response.message()}")
                    }
                } else {
                    Log.d("TAG", "No authentication token found")
                }
            } catch (e: Exception) {
                Log.e("TAG", "Error during checkout: ${e.message}")
            }
        }
    }


    fun getOrders(context: Context) {
        viewModelScope.launch {
            try {
                val token = getTokenFromSharedPreferences(context)
                if (token != null) {
                    val orders = RetrofitClient.apiService.getOrder("Bearer $token")
                    orderList.value = orders
                    Log.d("TAG", "Orders fetched successfully: $orders")
                }
            } catch (e: Exception) {
                Log.e("TAG", "Error fetching orders: ${e.message}")
                orderList.value = emptyList()
            }
        }
    }

}