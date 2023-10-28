package com.dityapra.mystoryapp.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dityapra.mystoryapp.data.remote.response.AllStoriesResponse
import com.dityapra.mystoryapp.data.remote.response.ListStoryItem
import com.dityapra.mystoryapp.data.remote.retrofit.ApiConfig
import com.dityapra.mystoryapp.helper.Event
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StoryViewModel : ViewModel(){

    private val _itemStory = MutableLiveData<List<ListStoryItem>>()
    val itemStory: LiveData<List<ListStoryItem>> = _itemStory

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isHaveData = MutableLiveData<Boolean>()
    val isHaveData: LiveData<Boolean> = _isHaveData

    private val _snackBarText = MutableLiveData<Event<String>>()
    val snackBarText: LiveData<Event<String>> = _snackBarText

    fun showListStory(token: String) {
        _isLoading.value = true
        _isHaveData.value = true
        val client = ApiConfig
            .getApiService()
            .getAllStories("Bearer $token")

        client.enqueue(object : Callback<AllStoriesResponse> {
            override fun onResponse(
                call: Call<AllStoriesResponse>,
                response: Response<AllStoriesResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        if (!responseBody.error) {
                            _itemStory.value = response.body()?.listStory
                            _isHaveData.value = responseBody.message == "Stories fetched successfully"
                        }
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                    _snackBarText.value = Event(response.message())
                }
            }

            override fun onFailure(call: Call<AllStoriesResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
                _snackBarText.value = Event(t.message.toString())
            }
        })
    }

    companion object{
        private const val TAG = "StoryViewModel"
    }
}