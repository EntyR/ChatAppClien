package com.enty.test

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.enty.test.databinding.ImageMessageRecieveBinding
import com.enty.test.databinding.ImageMessageSendBinding
import com.enty.test.databinding.MessageItemBinding
import com.enty.test.databinding.MessageUserItemBinding
import com.enty.test.utils.getUser
import com.entyr.model.Message
import java.text.DateFormat
import java.util.*


class ChatAdapter(val context: Context): ListAdapter<Message, RecyclerView.ViewHolder>(Companion) {


    class IncomeMessageViewHolder(val view: MessageItemBinding) : RecyclerView.ViewHolder(view.root)

    class IncomeImageViewHolder(val view: ImageMessageRecieveBinding) : RecyclerView.ViewHolder(view.root)

    class SendMessageViewHolder(val view: MessageUserItemBinding) :
        RecyclerView.ViewHolder(view.root)

    class SendImageViewHolder(val view: ImageMessageSendBinding) : RecyclerView.ViewHolder(view.root)


    companion object : DiffUtil.ItemCallback<Message>() {
        override fun areItemsTheSame(
            oldItem: Message,
            newItem: Message
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: Message,
            newItem: Message
        ): Boolean {
            return (oldItem.text == newItem.text) && (
                    oldItem.timestamp == newItem.timestamp
                    )
        }
    }

    override fun getItemViewType(position: Int): Int {
        val isUser =  (currentList[position].userName != getUser(context))
        val urlIsNull = currentList[position].url.isBlank()

        return when{
            (isUser && urlIsNull)->1
            (isUser && !urlIsNull) -> 3
            (!isUser && urlIsNull) -> 2
            else -> 4
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            1 -> IncomeMessageViewHolder(
                MessageItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            2-> SendMessageViewHolder(
                MessageUserItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            3 -> IncomeImageViewHolder(ImageMessageRecieveBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ))
            4 -> SendImageViewHolder(ImageMessageSendBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ))
            else -> throw Exception("Undefined view holder")
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        Log.e("Message", "New message: ${currentList[position]}")

        when(holder){
            is IncomeMessageViewHolder ->{
                holder.view.textView3.text = currentList[position].text
                holder.view.textView4.text = getDate(currentList[position].timestamp)
            }
            is SendMessageViewHolder -> {
                Log.e("TAG", "onBindViewHolder: ${currentList[position].url}")
                holder.view.textView3.text = currentList[position].text
                holder.view.textView4.text = getDate(currentList[position].timestamp)
            }
            is IncomeImageViewHolder -> {
                Log.e("TAG", "onBindViewHolder: ${currentList[position].url}")
                val imageView = holder.view.imageView3
                loadImg(currentList[position].url, imageView)

            }
            is SendImageViewHolder -> {
                Log.e("TAG", "onBindViewHolder: ${currentList[position].url}")
                val imageView = holder.view.imageView3
                loadImg(currentList[position].url, imageView)
            }
        }


    }

    private fun getDate(milliSeconds: Long): String {

        val dateFormat = DateFormat.getDateInstance().format(Date(milliSeconds))

        return dateFormat
    }
    private fun loadImg(url: String, view: ImageView) {
        Glide
            .with(context)
            .load(url)
            .override(300,300)
            .into(view)
    }
}