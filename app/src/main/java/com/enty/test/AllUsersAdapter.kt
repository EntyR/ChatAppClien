package com.enty.test

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.enty.test.databinding.UserItemBinding
import com.enty.test.utils.getUser
import com.entyr.model.LastMessageModel

import com.entyr.model.Message
import com.entyr.model.UserInfo

class AllUsersAdapter(val context: Context, val onItemClick: (userInfo: UserInfo)->Unit): ListAdapter<LastMessageModel, AllUsersAdapter.UserViewHolder>(Companion) {



    class UserViewHolder(val binding: UserItemBinding) : RecyclerView.ViewHolder(binding.root)

    

    companion object : DiffUtil.ItemCallback<LastMessageModel>() {
        override fun areItemsTheSame(
            oldItem: LastMessageModel,
            newItem: LastMessageModel
        ): Boolean {
            return true
        }

        override fun areContentsTheSame(
            oldItem: LastMessageModel,
            newItem: LastMessageModel
        ): Boolean {
            return oldItem == newItem
        }
    }

   

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return  UserViewHolder(
            UserItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ))
       
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {

        val binding = holder.binding
        val item = currentList[position]
        val isUser = (getUser(context) == item.sendToUser)
        val userEmail = if (getUser(context) == item.sendToUser)
            item.userName
        else item.sendToUser
        binding.tvLastMessage.text = item.text
        binding.root.setOnClickListener {
            if(isUser)
            onItemClick(UserInfo(
                userEmail, item.userNickName, item.userProfPick, ""
            )) else onItemClick(UserInfo(
                userEmail, item.sendToNickName, item.sendProfPick, ""
            ))
        }
        if (getUser(context) == item.sendToUser)
        binding.tvName.text = item.userNickName
        else binding.tvName.text = item.sendToNickName
        binding.tvLastMessage.text = item.text
        binding.tvDate.text = item.timestamp.toString()
        val profUri =  if(isUser) item.userProfPick
        else item.sendProfPick

        if (!profUri.isBlank()) loadImg(profUri, binding.civProfile)

        binding.civProfile
    }
    private fun loadImg(url: String, view: ImageView) {
        Glide
            .with(context)
            .load(url)
            .into(view)
    }
}