package com.dityapra.mystoryapp.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.dityapra.mystoryapp.MainActivity
import com.dityapra.mystoryapp.R
import com.dityapra.mystoryapp.data.model.UserPreference
import com.dityapra.mystoryapp.ui.viewmodel.MainViewModel
import com.dityapra.mystoryapp.ui.viewmodel.ViewModelFactory

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("settings")

class RoutingActivity : AppCompatActivity() {

    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_routing)

        setupViewModel()
    }

    private fun setupViewModel(){
        mainViewModel = ViewModelProvider(this, ViewModelFactory(UserPreference.getInstance(dataStore)))[MainViewModel::class.java]

        mainViewModel.getUser().observe(this){
            if (it.isLogin){
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else{
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }
    }
}