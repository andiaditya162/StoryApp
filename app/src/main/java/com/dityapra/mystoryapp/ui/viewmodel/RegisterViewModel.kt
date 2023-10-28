package com.dityapra.mystoryapp.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dityapra.mystoryapp.data.remote.response.ApiResponse
import com.dityapra.mystoryapp.data.remote.retrofit.ApiConfig
import com.dityapra.mystoryapp.helper.Helper
import org.json.JSONObject
import org.json.JSONTokener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun register(name: String, email: String, password: String, callback: Helper.ApiCallbackString){
        _isLoading.value = true

        val service = ApiConfig().getApiService().register(name, email, password)
        service.enqueue(object : Callback<ApiResponse> {
            override fun onResponse(
                call: Call<ApiResponse>,
                response: Response<ApiResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null && !responseBody.error)
                        callback.onResponse(response.body() != null, SUCCESS)

                } else {
                    Log.e(TAG, "onFailure1: ${response.message()}")

                    val jsonObject = JSONTokener(response.errorBody()!!.string()).nextValue() as JSONObject
                    val message = jsonObject.getString("message")
                    callback.onResponse(false, message)
                }
            }

            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure2: ${t.message}")
                callback.onResponse(false, t.message.toString())
            }
        })
    }

    companion object{
        private const val TAG = "RegisterviewModel"
        private const val SUCCESS = "success"
    }
}