package com.raiyansoft.eata.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.raiyansoft.eata.R
import com.raiyansoft.eata.databinding.ItemImageSliderBinding
import com.raiyansoft.eata.model.cases.Image
import kotlinx.android.synthetic.main.item_image_slider.view.*


class ImageSliderAdapter(
    var data: ArrayList<Image>
) :
    RecyclerView.Adapter<ImageSliderAdapter.MyViewHolder>() {


    class MyViewHolder(val item: ItemImageSliderBinding) : RecyclerView.ViewHolder(item.root) {

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView_layout: ItemImageSliderBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_image_slider, parent, false
        )
        return MyViewHolder(
            itemView_layout
        )
    }

    override fun getItemCount(): Int {
        return data.size
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.itemView.apply {
            Glide.with(context)
                .load(data[position].image)
                .placeholder(R.color.colorAccent)
                .into(tv_image_slider)
        }


    }


    interface onClick {
        fun onClickItem(position: Int, type: Int)
    }


}
