package com.enty.test

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.enty.test.databinding.ContactItemBinding
import com.enty.test.databinding.UserItemBinding
import com.entyr.model.Message
import com.entyr.model.UserInfo

class ContactAdapter(val context: Context, val onItemClick: (userInfo: UserInfo)->Unit): ListAdapter<UserInfo, ContactAdapter.UserViewHolder>(Companion) {



    class UserViewHolder(val binding: ContactItemBinding) : RecyclerView.ViewHolder(binding.root)

    

    companion object : DiffUtil.ItemCallback<UserInfo>() {
        override fun areItemsTheSame(
            oldItem: UserInfo,
            newItem: UserInfo
        ): Boolean {
            return true
        }

        override fun areContentsTheSame(
            oldItem: UserInfo,
            newItem: UserInfo
        ): Boolean {
            return oldItem == newItem
        }
    }

   

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return  UserViewHolder(
            ContactItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ))
       
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val binding = holder.binding
        val item = currentList[position]

        binding.root.setOnClickListener {
            onItemClick(item)
        }
        if (!item.profPic.isBlank())
        loadImg(item.profPic, binding.civProfile)
        binding.tvName.text = item.username

    }
    private fun loadImg(url: String, view: ImageView) {
        Glide
            .with(context)
            .load(url)
            .override(100)
            .into(view)
    }
}