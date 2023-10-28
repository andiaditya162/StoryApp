package com.dityapra.mystoryapp.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dityapra.mystoryapp.data.model.UserModel
import com.dityapra.mystoryapp.data.remote.response.ApiResponse
import com.dityapra.mystoryapp.data.remote.retrofit.ApiConfig
import com.dityapra.mystoryapp.helper.Helper
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import org.json.JSONTokener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddStoryViewModel : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun uploadImage(
        user: UserModel,
        description: String,
        imageMultipart: MultipartBody.Part,
        callback: Helper.ApiCallbackString
    ) {
        _isLoading.value = true

        if (description.isEmpty()) {
            _isLoading.value = false
            callback.onResponse(false, "Deskripsi tidak boleh kosong.")
            return
        }

        val descriptionRequest = description.toRequestBody("text/plain".toMediaTypeOrNull())
        val service = ApiConfig().getApiService().addStories("Bearer ${user.token}", descriptionRequest, imageMultipart)
        service.enqueue(object : Callback<ApiResponse> {
            override fun onResponse(
                call: Call<ApiResponse>,
                response: Response<ApiResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null && !responseBody.error) {
                        callback.onResponse(response.body() != null, SUCCESS)
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")

                    val jsonObject = JSONTokener(response.errorBody()!!.string()).nextValue() as JSONObject
                    val message = jsonObject.getString("message")
                    callback.onResponse(false, message)
                }
            }
            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
                callback.onResponse(false, t.message.toString())
            }
        })

    }

    companion object {
        private const val TAG = "AddStoryViewModel"
        private const val SUCCESS = "success"
    }
}