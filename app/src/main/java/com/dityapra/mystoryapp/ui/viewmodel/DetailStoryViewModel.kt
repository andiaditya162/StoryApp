package com.dityapra.mystoryapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.dityapra.mystoryapp.data.remote.response.ListStoryItem

class DetailStoryViewModel: ViewModel() {
    lateinit var storyItem: ListStoryItem

    fun setDetailStory(story: ListStoryItem) : ListStoryItem{
        storyItem = story
        return storyItem
    }
}