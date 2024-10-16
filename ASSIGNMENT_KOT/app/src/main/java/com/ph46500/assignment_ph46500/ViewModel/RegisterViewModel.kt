package com.ph46500.assignment_ph46500.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ph46500.assignment_ph46500.APIServices.ApiService
import com.ph46500.assignment_ph46500.APIServices.RetrofitClient
import com.ph46500.assignment_ph46500.Model.User
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RegisterViewModel: ViewModel() {

    private val apiService = RetrofitClient.apiService

    fun registerUser(user: User, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val response = apiService.registerUser(user)
                onSuccess()
            } catch (e: Exception) {
                onError(e.message ?: "Đăng ký thất bại")
            }
        }
    }

}