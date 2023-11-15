package com.dityapra.mystoryapp.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.dityapra.mystoryapp.data.local.datastore.model.UserModel
import com.dityapra.mystoryapp.data.remote.response.ListStoryItem
import com.dityapra.mystoryapp.data.repository.StoryRepository
import kotlinx.coroutines.launch

class MainViewModel(private val repository: StoryRepository) : ViewModel() {
    val stories: LiveData<PagingData<ListStoryItem>> by lazy {
        repository.getStories("Bearer ${repository.getToken()}").cachedIn(viewModelScope)
    }

    fun getUser(): LiveData<UserModel> = repository.getUser()

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }
}