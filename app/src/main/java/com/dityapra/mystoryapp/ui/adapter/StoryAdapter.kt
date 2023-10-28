package com.dityapra.mystoryapp.ui.adapter

import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dityapra.mystoryapp.R
import com.dityapra.mystoryapp.data.remote.response.ListStoryItem
import com.dityapra.mystoryapp.databinding.ItemStoryBinding
import com.dityapra.mystoryapp.helper.DiffCallback
import com.dityapra.mystoryapp.helper.Helper
import com.dityapra.mystoryapp.ui.activity.DetailStoryActivity
import java.util.TimeZone

class StoryAdapter: RecyclerView.Adapter<StoryAdapter.ViewHolder>() {

    private val listStory = ArrayList<ListStoryItem>()

    fun setListStory(itemStory: List<ListStoryItem>) {
        val diffCalback = DiffCallback(this.listStory, itemStory)
        val diffResult = DiffUtil.calculateDiff(diffCalback)

        this.listStory.clear()
        this.listStory.addAll(itemStory)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listStory[position])
    }

    override fun getItemCount() = listStory.size

    inner class ViewHolder(private var binding: ItemStoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(story: ListStoryItem) {
            with(binding) {
                Glide.with(imgItemImage)
                    .load(story.photoUrl)
                    .placeholder(R.drawable.ic_image)
                    .error(R.drawable.ic_none_image)
                    .into(imgItemImage)
                tvName.text = story.name
                tvDescription.text = story.description
                tvCreatedTime.text = binding.root.resources.getString(R.string.created_add, Helper.formatDate(story.createdAt, TimeZone.getDefault().id))

                imgItemImage.setOnClickListener {
                    val intent = Intent(it.context, DetailStoryActivity::class.java)
                    intent.putExtra(DetailStoryActivity.EXTRA_STORY, story)
                    it.context.startActivity(intent)
                }
            }
        }
    }
}