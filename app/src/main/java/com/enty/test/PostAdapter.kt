package com.enty.test

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.enty.test.databinding.PostItemBinding
import com.entyr.model.PostResponse
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class PostAdapter(val context: Context) : ListAdapter<PostResponse, PostAdapter.PostViewHolder>(Companion) {


    class PostViewHolder(val binding: PostItemBinding) : RecyclerView.ViewHolder(binding.root)


    companion object : DiffUtil.ItemCallback<PostResponse>() {
        override fun areItemsTheSame(
            oldItem: PostResponse,
            newItem: PostResponse
        ): Boolean {
            return false
        }

        override fun areContentsTheSame(
            oldItem: PostResponse,
            newItem: PostResponse
        ): Boolean {
            return false
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        return PostViewHolder(
            PostItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val binding = holder.binding
        val item = currentList[position]
        binding.tvTitle.text = item.authorName
        binding.tvDesc.text = item.text
        loadImg(item.url, binding.ivPostImage)
        val date = getDate(item.date)
        binding.tvPostDate.text = date.toString()
        if (!item.profileUri.isBlank())
            loadProfile(item.profileUri, binding.profileImage)


    }

    private fun loadImg(url: String, view: ImageView) {
        Glide
            .with(context)
            .load(url)
            .into(view)
    }
    private fun loadProfile(url: String, view: ImageView) {
        Glide
            .with(context)
            .load(url)
            .override(100)
            .into(view)
    }


    private fun getDate(milliSeconds: Long): String {

        val dateFormat = DateFormat.getDateInstance().format(Date(milliSeconds))

        return dateFormat
    }


}