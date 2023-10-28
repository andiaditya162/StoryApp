package com.dityapra.mystoryapp.ui.activity

import android.graphics.text.LineBreaker
import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.dityapra.mystoryapp.R
import com.dityapra.mystoryapp.data.remote.response.ListStoryItem
import com.dityapra.mystoryapp.databinding.ActivityDetailStoryBinding
import com.dityapra.mystoryapp.helper.Helper
import com.dityapra.mystoryapp.ui.viewmodel.DetailStoryViewModel
import java.util.TimeZone

@Suppress("DEPRECATION")
class DetailStoryActivity : AppCompatActivity() {
    private lateinit var story: ListStoryItem
    private lateinit var binding: ActivityDetailStoryBinding

    private val vm: DetailStoryViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            run {
                binding.tvDescription.justificationMode = LineBreaker.JUSTIFICATION_MODE_INTER_WORD
            }
        }

        story = intent.getParcelableExtra(EXTRA_STORY)!!
        vm.setDetailStory(story)
        displayResult()
        setupToolbar()
    }

    private fun setupToolbar(){
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun displayResult() {
        with(binding){
            tvName.text = vm.storyItem.name
            tvCreatedTime.text = Helper.formatDate(vm.storyItem.createdAt, TimeZone.getDefault().id)
            tvDescription.text = vm.storyItem.description

            Glide.with(ivStory)
                .load(vm.storyItem.photoUrl)
                .placeholder(R.drawable.ic_image)
                .error(R.drawable.ic_none_image)
                .into(ivStory)
        }
    }
    companion object {
        const val EXTRA_STORY = "story"
    }
}