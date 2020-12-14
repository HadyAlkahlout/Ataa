package com.raiyansoft.eata.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.raiyansoft.eata.R
import com.raiyansoft.eata.model.cases.Image
import kotlinx.android.synthetic.main.item_add_image.view.*


class AddImageAdapter(
    var data: ArrayList<Image>,
    var onClick: CancelClick
) :
    RecyclerView.Adapter<AddImageAdapter.PropertyAdapterVH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PropertyAdapterVH {
        val inflate: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_add_image, parent, false)
        return PropertyAdapterVH(inflate)
    }

    override fun onBindViewHolder(viewHolder: PropertyAdapterVH, position: Int) {

        val image = data[position]

        viewHolder.itemView.apply {
            Glide.with(context)
                .load(image.image)
                .centerCrop()
//                .placeholder(R.drawable.property_placeholder)
                .into(imgProperty)

            imgCancel.setOnClickListener {
                onClick.cancelClick(image, position)
            }
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class PropertyAdapterVH(itemView: View) :
        RecyclerView.ViewHolder(itemView)


    interface CancelClick {
        fun cancelClick(item: Image, i: Int)
    }
}