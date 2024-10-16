package com.ph46500.assignment_ph46500.ViewModel

import android.content.Context
import android.content.SharedPreferences
import android.net.http.HttpException
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ph46500.assignment_ph46500.APIServices.RetrofitClient
import com.ph46500.assignment_ph46500.APIServices.RetrofitClient.apiService
import com.ph46500.assignment_ph46500.Model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

class UserViewModel : ViewModel() {

    private val _userInfoState = MutableStateFlow<User?>(null)
    val userInfoState: StateFlow<User?> = _userInfoState
    fun getTokenFromSharedPreferences(context: Context): String? {
        val sharedPref: SharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        return sharedPref.getString("token", null)
    }

    fun getUserInfo(context: Context) {
        viewModelScope.launch {
            try {
                val token = getTokenFromSharedPreferences(context)
                if (token != null) {
                   val user = RetrofitClient.apiService.getUserInfo("Bearer $token")
                    _userInfoState.value = user
                } else {
                    Log.d("TAG", "Please login first")
                    _userInfoState.value = null
                }
            } catch (e: Exception) {
                Log.d("TAG", "Please login first")
            }
        }
    }

    fun UpdateUser(
        context: Context,
        name: String,
        dateOfBirth: String,
        gender: String,
    ) {
        viewModelScope.launch {
            try {
                val token = getTokenFromSharedPreferences(context)
                if (token != null) {
                    val updateUser = User(
                        name = name,
                        userName = _userInfoState.value?.userName ?: "",
                        email = _userInfoState.value?.email ?: "",
                        password = _userInfoState.value?.password ?: "",
                        avatar = _userInfoState.value?.avatar ?: "",
                        dateOfBirth = dateOfBirth,
                        gender = gender
                    )
                    RetrofitClient.apiService.updateUser("Bearer $token", updateUser)
                    Log.d("TAG", "User info updated successfully")
                } else {
                    Log.d("TAG", "Please login first")
                }
            } catch (e: Exception) {
                Log.d("TAG", "Failed to update user info: ${e.message}")
            }
        }
    }

    fun UpdateAvatar(context: Context, avatar: MultipartBody.Part) {
        viewModelScope.launch {
            try {
                val token = getTokenFromSharedPreferences(context)
                if (token != null) {
                    val response = RetrofitClient.apiService.updateAvatar("Bearer $token", avatar)
                    Log.d("TAG", "Avatar updated successfully: ${response.body()}")
                } else {
                    Log.d("TAG", "Please login first")
                }
            } catch (e: Exception) {
                Log.d("TAG", "Failed to update avatar: ${e.message}")
            }
        }
    }


}