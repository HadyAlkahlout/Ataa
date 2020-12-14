package com.raiyansoft.eata.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.raiyansoft.eata.R
import com.raiyansoft.eata.model.categories.Content
import com.raiyansoft.eata.model.question.Question
import kotlinx.android.synthetic.main.item_deisgn_question.view.*


class QuestionAdapter(var data: ArrayList<Question>) :
    RecyclerView.Adapter<QuestionAdapter.MyViewHolder>() {


    class MyViewHolder(item: View) : RecyclerView.ViewHolder(item)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_deisgn_question, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return data.size
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val question = data[position]
        holder.itemView.apply {
            txtTitleQuetion.text = question.question
            txtAnswer.text = question.answer

//            setOnClickListener {
//            }

        }
    }


    interface OnClickCategories {
        fun onClick(category: Content)
    }

}

