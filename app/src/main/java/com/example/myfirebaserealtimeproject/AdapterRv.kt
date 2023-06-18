package com.example.myfirebaserealtimeproject

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myfirebaserealtimeproject.databinding.ItemusersBinding
import com.example.myfirebaserealtimeproject.modul.User
import com.squareup.picasso.Picasso

class AdapterRv (var list:List<User>,var onItemClickListener: OnItemClickListener) : RecyclerView.Adapter<AdapterRv.Vh>(){

    inner class Vh(var itemUsersBinding: ItemusersBinding)
        : RecyclerView.ViewHolder(itemUsersBinding.root) {
        fun onBind(user: User) {
            itemUsersBinding.emailTv.text=user.email
            itemUsersBinding.nameTv.text=user.displayName
            if (user.online){
               itemUsersBinding.network.setImageResource(R.drawable.onlayn_shape)
            }else {
                itemUsersBinding.network.setImageResource(R.drawable.ic_offline)
            }
            itemUsersBinding.onlayn.visibility = if (user.online)  View.VISIBLE else View.INVISIBLE
            Picasso.get().load(user.photoUrl).into(itemUsersBinding.imageImg)

            itemView.setOnClickListener {
                onItemClickListener.onItemClick(user)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(ItemusersBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(list[position])
    }
    interface OnItemClickListener{
        fun onItemClick(user: User )
    }
}