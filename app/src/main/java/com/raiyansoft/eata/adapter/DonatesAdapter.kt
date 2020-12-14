package com.raiyansoft.eata.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.raiyansoft.eata.R
import com.raiyansoft.eata.databinding.ItemDonatesBinding
import com.raiyansoft.eata.model.donation.Content
import kotlinx.android.synthetic.main.item_donates.view.*


class DonatesAdapter(
    var data: ArrayList<Content>, val itemclick: onClick
) :
    RecyclerView.Adapter<DonatesAdapter.MyViewHolder>() {


    class MyViewHolder(item: ItemDonatesBinding) : RecyclerView.ViewHolder(item.root) {
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView_layout: ItemDonatesBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_donates, parent, false
        )
        return MyViewHolder(
            itemView_layout
        )
    }

    override fun getItemCount(): Int {
        return data.size
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val donates = data[position]

        holder.itemView.apply {

            titleDo.text = donates.case_title
            tv_donates_date.text = donates.date
            tv_donates_price.text = "دك" + donates.total.toString()

            tv_move_to_chat.setOnClickListener {
                itemclick.onClickItem(donates, 1)
            }
            setOnClickListener {
                itemclick.onClickItem(donates, position)
            }

        }

    }

    interface onClick {
        fun onClickItem(content: Content, type: Int)
    }


}
