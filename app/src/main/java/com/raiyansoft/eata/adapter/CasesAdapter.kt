package com.raiyansoft.eata.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.raiyansoft.eata.R
import com.raiyansoft.eata.databinding.ItemDeisgnCaseBinding
import com.raiyansoft.eata.model.cases.DataX
import kotlinx.android.synthetic.main.item_deisgn_case.view.*
import kotlinx.android.synthetic.main.item_deisgn_case.view.txtNameCase
import kotlinx.android.synthetic.main.item_deisgn_case.view.txtPriceCase
import kotlinx.android.synthetic.main.item_deisgn_need_case.view.*


class CasesAdapter(
    var data: ArrayList<DataX>, val itemclick: onClick
) :
    RecyclerView.Adapter<CasesAdapter.MyViewHolder>() {


    class MyViewHolder(item: ItemDeisgnCaseBinding) : RecyclerView.ViewHolder(item.root) {
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView_layout: ItemDeisgnCaseBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_deisgn_case, parent, false
        )
        return MyViewHolder(
            itemView_layout
        )
    }

    override fun getItemCount(): Int {
        return data.size
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val content = data[position]

        holder.itemView.apply {
            txtNameCase.text = content.title
            txtPriceCase.text = content.total+" دك "

            setOnClickListener {
                itemclick.onClickItem(content)
            }

        }

    }

    interface onClick {
        fun onClickItem(content: DataX)
    }


}
