package com.dityapra.mystoryapp

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.dityapra.mystoryapp.data.model.UserModel
import com.dityapra.mystoryapp.data.model.UserPreference
import com.dityapra.mystoryapp.databinding.ActivityMainBinding
import com.dityapra.mystoryapp.ui.activity.LoginActivity
import com.dityapra.mystoryapp.ui.activity.StoryActivity
import com.dityapra.mystoryapp.ui.viewmodel.MainViewModel
import com.dityapra.mystoryapp.ui.viewmodel.ViewModelFactory

val Context.dataStore: DataStore<Preferences> by preferencesDataStore("settings")
class MainActivity : AppCompatActivity() {

    private lateinit var user: UserModel
    private lateinit var mainViewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()
        playAnimation()
        buttonListener()
    }

    private fun setupViewModel(){
        mainViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[MainViewModel::class.java]

        mainViewModel.getUser().observe(this){
            user = UserModel(
                it.name,
                it.email,
                it.password,
                it.userId,
                it.token,
                true
            )
            binding.nameTextView.text = getString(R.string.greeting, user.name)
        }
    }
    private fun playAnimation(){
        val name = ObjectAnimator.ofFloat(binding.nameTextView, View.ALPHA, 1f).setDuration(500)
        val message = ObjectAnimator.ofFloat(binding.messageTextView, View.ALPHA, 1f).setDuration(500)
        val logout = ObjectAnimator.ofFloat(binding.btnLogout, View.ALPHA, 1f).setDuration(500)

        AnimatorSet().apply {
            playSequentially(name, message, logout)
            startDelay = 500
        }.start()
    }

    private fun buttonListener(){
        binding.btnStory.setOnClickListener{
            val moveToListStoryActivity = Intent(this@MainActivity, StoryActivity::class.java)
            moveToListStoryActivity.putExtra(StoryActivity.EXTRA_USER, user)
            startActivity(moveToListStoryActivity)
        }
        binding.btnLogout.setOnClickListener {
            mainViewModel.logout()
            AlertDialog.Builder(this).apply {
                setTitle("Information")
                setMessage("Logout Success")
                setPositiveButton("Continue") { _, _ ->
                startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                finish()
                }
                create()
                show()
            }
        }
    }
}