package com.raiyansoft.eata.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.raiyansoft.eata.R
import com.raiyansoft.eata.databinding.ItemDeisgnCaseBinding
import com.raiyansoft.eata.databinding.ItemDeisgnNeedCaseBinding
import com.raiyansoft.eata.model.cases.DataX
import kotlinx.android.synthetic.main.item_deisgn_case.view.*
import kotlinx.android.synthetic.main.item_deisgn_case.view.txtNameCase
import kotlinx.android.synthetic.main.item_deisgn_need_case.view.*


class CasesNeedAdapter(
    var data: ArrayList<DataX>, val itemclick: onClick
) :
    RecyclerView.Adapter<CasesNeedAdapter.MyViewHolder>() {


    class MyViewHolder(item: ItemDeisgnNeedCaseBinding) : RecyclerView.ViewHolder(item.root) {
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView_layout: ItemDeisgnNeedCaseBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_deisgn_need_case, parent, false
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
            txtPriceCase2.text = content.total+" دك "

            if (content.accepted!!){
                relativeLayout.setBackgroundResource(R.drawable.shape_bg_not_approve)
                txtaccepted.text="تم القبول"
            }else{
                relativeLayout.setBackgroundResource(R.drawable.shape_bg_approve)
                txtaccepted.text="معلق"
            }

            setOnClickListener {
                itemclick.onClickItem(content)
            }

        }

    }

    interface onClick {
        fun onClickItem(content: DataX)
    }


}
