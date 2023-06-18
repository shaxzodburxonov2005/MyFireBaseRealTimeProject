package com.example.myfirebaserealtimeproject

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myfirebaserealtimeproject.databinding.ItemfromBinding
import com.example.myfirebaserealtimeproject.databinding.ItemtooBinding
import com.example.myfirebaserealtimeproject.modul.MessageChat

class MessageAdapter (var list: List<MessageChat>, var uid: String) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class FromVh(var itemFromBinding: ItemfromBinding) :
        RecyclerView.ViewHolder(itemFromBinding.root) {
        fun onBind(messageChat: MessageChat) {
            itemFromBinding.nameUser.text = messageChat.message
            itemFromBinding.dataTime.text = messageChat.date
        }
    }

    inner class ToVh(var itemTooBinding: ItemtooBinding) :
        RecyclerView.ViewHolder(itemTooBinding.root) {
        fun onBind(messageChat: MessageChat) {
            itemTooBinding.nameUser.text = messageChat.message
            itemTooBinding.dataTime.text = messageChat.date
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        Log.d("AAA", "onCreateViewHolder: $viewType")
        if (viewType == 1) {
            return FromVh(
                ItemfromBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )

        } else {
            return ToVh(
                ItemtooBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun getItemViewType(position: Int): Int {
        Log.d("AAA", "getItemViewType: ${list[position].fromUid} == $uid")
        return if (list[position].fromUid == uid) {
            1
        } else {
            2
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == 1) {
            val fromVh = holder as FromVh
            fromVh.onBind(list[position])
        } else if (getItemViewType(position) == 2) {
            val toVh = holder as ToVh
            toVh.onBind(list[position])

        }

    }
}