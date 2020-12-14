package com.raiyansoft.eata.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.raiyansoft.eata.R
import com.raiyansoft.eata.model.chat.TextMessage
import com.raiyansoft.eata.util.Constants
import kotlinx.android.synthetic.main.item_deisgn_recever.view.*
import kotlinx.android.synthetic.main.item_deisgn_sender.view.*

class MessageAdapter(context: Context, var data: ArrayList<TextMessage>) :
    RecyclerView.Adapter<MessageAdapter.MyViewHolder>() {

    private val SENDER = Constants.getSharePref(context).getString(Constants.UserID, "")
    private val TYPE = Constants.getSharePref(context).getInt(Constants.TYPE, 0)

    class MyViewHolder(item: View) : RecyclerView.ViewHolder(item)


    override fun getItemViewType(position: Int): Int {
        val textMessage = data[position]
        if (TYPE == 0) {
            if (textMessage.from == SENDER) {
                return R.layout.item_deisgn_sender
            } else {
                return R.layout.item_deisgn_recever
            }
        } else {
            if (textMessage.to == SENDER) {
                return R.layout.item_deisgn_recever

            } else {
                return R.layout.item_deisgn_sender
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return data.size
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val textMessage = data[position]

        holder.itemView.apply {
            if (TYPE == 0) {
                if (textMessage.from == SENDER) {
                    txtSender.text = data[position].message
                    txtDataSender.text =
                        android.text.format.DateFormat.format("hh:mm a", data[position].date.toLong())
                } else {
                    txtReceiver.text = data[position].message
                    txtDataReceiver.text =
                        android.text.format.DateFormat.format("hh:mm a", data[position].date.toLong())
                }
            } else {
                if (textMessage.to == SENDER) {
                    txtReceiver.text = data[position].message
                    txtDataReceiver.text =
                        android.text.format.DateFormat.format("hh:mm a", data[position].date.toLong())

                } else {
                    txtSender.text = data[position].message
                    txtDataSender.text =
                        android.text.format.DateFormat.format("hh:mm a",data[position].date.toLong())

                }
            }
        }
    }

}

