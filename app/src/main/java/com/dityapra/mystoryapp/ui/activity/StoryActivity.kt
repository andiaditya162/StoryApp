package com.dityapra.mystoryapp.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.dityapra.mystoryapp.R
import com.dityapra.mystoryapp.data.model.UserModel
import com.dityapra.mystoryapp.databinding.ActivityStoryBinding
import com.dityapra.mystoryapp.ui.adapter.StoryAdapter
import com.dityapra.mystoryapp.ui.viewmodel.StoryViewModel
import com.google.android.material.snackbar.Snackbar

@Suppress("DEPRECATION")
class StoryActivity : AppCompatActivity() {

    private var _binding: ActivityStoryBinding? = null
    private val binding get() = _binding

    private lateinit var user: UserModel
    private lateinit var adapter: StoryAdapter

    private val viewModel by viewModels<StoryViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityStoryBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setupToolbar()
        addStoryAction()

        user = intent.getParcelableExtra(EXTRA_USER)!!

        setListStory()
        adapter = StoryAdapter()

        showSnackBar()

        setupRecycleView()

        showLoading()
        showHaveDataOrNot()
    }

    private fun setupToolbar(){
        setSupportActionBar(binding?.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun showSnackBar() {
        viewModel.snackBarText.observe(this) {
            it.getContentIfNotHandled()?.let { snackBarText ->
                Snackbar.make(
                    findViewById(R.id.rv_story),
                    snackBarText,
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun setupRecycleView(){
        binding?.rvStory?.layoutManager = LinearLayoutManager(this)
        binding?.rvStory?.setHasFixedSize(true)
        binding?.rvStory?.adapter = adapter
    }

    private fun showLoading() {
        viewModel.isLoading.observe(this) {
            binding?.apply {
                if (it) {
                    progressBar.visibility = View.VISIBLE
                    rvStory.visibility = View.INVISIBLE
                } else {
                    progressBar.visibility = View.GONE
                    rvStory.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun showHaveDataOrNot(){
        viewModel.isHaveData.observe(this){
            binding?.apply {
                if (it) {
                    rvStory.visibility = View.VISIBLE
                    tvInfo.visibility = View.GONE
                } else {
                    rvStory.visibility = View.GONE
                    tvInfo.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun setListStory() {
        viewModel.showListStory(user.token)
        viewModel.itemStory.observe(this) {
            adapter.setListStory(it)
        }
    }

    override fun onResume() {
        super.onResume()
        setListStory()
    }

    private fun addStoryAction(){
        binding?.ivAddStory?.setOnClickListener {
            val moveToAddStoryActivity = Intent(this, AddStoryActivity::class.java)
            moveToAddStoryActivity.putExtra(AddStoryActivity.EXTRA_USER, user)
            startActivity(moveToAddStoryActivity)
        }
    }

    companion object {
        const val EXTRA_USER = "user"
    }
}