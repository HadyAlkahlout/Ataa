package com.raiyansoft.eata.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.raiyansoft.eata.R
import com.raiyansoft.eata.databinding.ItemNotificationBinding
import com.raiyansoft.eata.model.notification.Content
import com.raiyansoft.eata.model.notification.Notification
import kotlinx.android.synthetic.main.item_notification.view.*


class NotificationAdapter(
    var data: ArrayList<Content>, val itemclick: onClick
) :
    RecyclerView.Adapter<NotificationAdapter.MyViewHolder>() {


    class MyViewHolder(item: ItemNotificationBinding) : RecyclerView.ViewHolder(item.root) {
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView_layout: ItemNotificationBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_notification, parent, false)
        return MyViewHolder(
            itemView_layout
        )
    }

    override fun getItemCount(): Int {
        return data.size
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val notification = data[position]

        holder.itemView.apply {
            /*notificaiton_card.startAnimation(
                AnimationUtils.loadAnimation(
                    holder.itemView.context,
                    R.anim.fragment_fade_enter
                )
            )*/

            tv_notification_title.text = notification.title
            tv_notification_message.text = notification.message

            setOnClickListener {
                itemclick.onClickItem(notification, position)
            }

        }

    }

    interface onClick {
        fun onClickItem(notification: Content, type: Int)
    }


}
