package com.ph46500.assignment_ph46500.Model


data class Product(
    var _id: String,
    var image_url: String,
    var name: String,
    var price: Double,
    val description: String,
    var quantity: Int,
    var category: String,
)

data class Category(
    val id: String,
    val name: String,
    val description: String = ""
)

data class User(
    var name: String,
    var userName: String,
    var email: String,
    var password: String,
    var avatar: String,
    var dateOfBirth: String,
    var gender: String,
)
{
    constructor(email: String, password: String) : this(
        name = "",
        email = email,
        userName = "",
        password = password,
        avatar = "",
        dateOfBirth = "",
        gender = "",
    )
}

data class LoginResponse(val token: String?, val expiresIn: Long?)


data class Cart(
    val product: Product,
    val quantity:Int,
)

data class OrderItem(
    val product: Product,
    val quantity: Int
)

data class Order(
    val items: List<OrderItem>,
    val totalAmount: Double,
    val address: String,
    val paymentMethod: String,
    val phoneNumber: String,
    val status: String,
    val createdAt: String,
)





