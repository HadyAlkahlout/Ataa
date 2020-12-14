package com.raiyansoft.eata.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.raiyansoft.eata.R
import com.raiyansoft.eata.databinding.UserItemChatBinding
import com.raiyansoft.eata.model.chat.User_Chat
import kotlinx.android.synthetic.main.user_item_chat.view.*


class UserAdapter(
    var data: MutableList<User_Chat>, val itemclick: onClick
) :
        RecyclerView.Adapter<UserAdapter.MyViewHolder>() {


    class MyViewHolder(val item: UserItemChatBinding) : RecyclerView.ViewHolder(item.root) {


        fun bind(n: User_Chat) {
            item.user = n
            item.executePendingBindings()
        }


    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView_layout: UserItemChatBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context),
                R.layout.user_item_chat, parent, false)
        return MyViewHolder(itemView_layout)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun setUsers(lsit: List<User_Chat>) {
        this.data = lsit as MutableList<User_Chat>
    }



    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val currentItem = data[position]
        holder.bind(currentItem)

        holder.itemView.allcard.setOnClickListener {
            itemclick.onClickItem(data[position],1)
        }




    }

    interface onClick {
        fun onClickItem(userChat: User_Chat, type: Int)
    }




}
