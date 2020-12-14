package com.raiyansoft.eata.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.raiyansoft.eata.R
import com.raiyansoft.eata.model.categories.Content
import kotlinx.android.synthetic.main.item_add_image.view.*
import kotlinx.android.synthetic.main.item_deisgn_categories.view.*


class CategoriesAdapter(var data: ArrayList<Content>, val onClick: OnClickCategories) :
    RecyclerView.Adapter<CategoriesAdapter.MyViewHolder>() {


    class MyViewHolder(item: View) : RecyclerView.ViewHolder(item)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_deisgn_categories, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return data.size
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val category = data[position]
        holder.itemView.apply {
            txtCategoryType.text = category.title
            Glide.with(context)
                .load(category.image)
                .centerCrop()
                .placeholder(R.color.colorPurple_1000)
                .into(imageView4)


            setOnClickListener {
                onClick.onClick(category)
            }

        }
    }


    interface OnClickCategories {
        fun onClick(category: Content)
    }

}

