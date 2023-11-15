package com.dityapra.mystoryapp.view.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.dityapra.mystoryapp.R
import com.dityapra.mystoryapp.adapter.LoadingStateAdapter
import com.dityapra.mystoryapp.adapter.StoryAdapter
import com.dityapra.mystoryapp.databinding.ActivityMainBinding
import com.dityapra.mystoryapp.view.ViewModelFactory
import com.dityapra.mystoryapp.view.addstory.AddStoryActivity
import com.dityapra.mystoryapp.view.maps.MapsActivity
import com.dityapra.mystoryapp.view.welcome.WelcomeActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val mainViewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupAction()
    }

    private fun setupView() {
        binding.rvStories.setHasFixedSize(true)
        binding.rvStories.layoutManager = LinearLayoutManager(this)

        mainViewModel.getUser().observe(this) { user ->
            if (user.isLogin) {
                val adapter = StoryAdapter()
                binding.rvStories.adapter = adapter.withLoadStateFooter(
                    footer = LoadingStateAdapter {
                        adapter.retry()
                    }
                )
                mainViewModel.stories.observe(this) {
                    adapter.submitData(lifecycle, it)
                }
            } else {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            }
        }
    }

    private fun setupAction() {
        binding.btnAddStory.setOnClickListener {
            startActivity(Intent(this, AddStoryActivity::class.java))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_item, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.maps -> {
                startActivity(Intent(this, MapsActivity::class.java))
            }

            R.id.logout -> {
                AlertDialog.Builder(this)
                    .setTitle("Logout")
                    .setMessage("Are you sure you want to logout?")
                    .setCancelable(false)
                    .setPositiveButton("Logout") { _, _ ->
                        mainViewModel.logout()
                    }
                    .setNegativeButton("Cancel") { dialog, _ ->
                        dialog.cancel()
                    }
                    .show()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}