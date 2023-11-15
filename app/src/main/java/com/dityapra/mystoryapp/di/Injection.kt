package com.dityapra.mystoryapp.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.dityapra.mystoryapp.data.local.datastore.UserPreferences
import com.dityapra.mystoryapp.data.remote.retrofit.ApiConfig
import com.dityapra.mystoryapp.data.repository.StoryRepository

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "token")

object Injection {
    fun provideRepository(context: Context): StoryRepository {
        val pref = UserPreferences.getInstance(context.dataStore)
        val apiService = ApiConfig.getApiService()
        return StoryRepository.getInstance(apiService, pref)
    }
}