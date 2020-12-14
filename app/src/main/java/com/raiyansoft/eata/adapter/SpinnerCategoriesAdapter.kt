package com.raiyansoft.eata.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.raiyansoft.eata.R
import com.raiyansoft.eata.model.categories.Content
import kotlinx.android.synthetic.main.item_design_spinner.view.*


class SpinnerCategoriesAdapter(val content: ArrayList<Content>) : BaseAdapter() {

    override fun getCount(): Int {
        return content.size
    }

    override fun getItem(p0: Int): Content {
        return content[p0]
    }

    override fun getItemId(p0: Int): Long {
        return content[p0].id
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        val view = LayoutInflater.from(p2!!.context)
            .inflate(R.layout.item_design_spinner, p2, false)

        view.txtCategorySpenner.text = content[p0].title

        return view
    }
}