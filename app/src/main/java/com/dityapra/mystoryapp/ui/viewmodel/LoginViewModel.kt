package com.dityapra.mystoryapp.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dityapra.mystoryapp.data.model.UserModel
import com.dityapra.mystoryapp.data.model.UserPreference
import com.dityapra.mystoryapp.data.remote.response.LoginResponse
import com.dityapra.mystoryapp.data.remote.retrofit.ApiConfig
import com.dityapra.mystoryapp.helper.Helper
import kotlinx.coroutines.launch
import org.json.JSONObject
import org.json.JSONTokener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(private val pref: UserPreference): ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun login(email: String, password: String, callback: Helper.ApiCallbackString){
        _isLoading.value = true

        val service = ApiConfig().getApiService().login(email, password)
        service.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful){
                    val responseBody = response.body()
                    if (responseBody != null && !responseBody.error){
                        callback.onResponse(response.body() != null, SUCCESS)

                        val model = UserModel(
                            responseBody.loginResult.name,
                            email,
                            password,
                            responseBody.loginResult.userId,
                            responseBody.loginResult.token,
                            true
                        )
                        saveUser(model)
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")

                    val jsonObject = JSONTokener(response.errorBody()!!.string()).nextValue() as JSONObject
                    val message = jsonObject.getString("message")
                    callback.onResponse(false, message)
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
                callback.onResponse(false, t.message.toString())
            }
        })
    }

    fun saveUser(user: UserModel){
        viewModelScope.launch {
            pref.saveUser(user)
        }
    }
    companion object{
        private const val TAG = "LoginViewModel"
        private const val SUCCESS = "succes"
    }
}