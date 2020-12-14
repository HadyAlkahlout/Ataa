package com.raiyansoft.eata.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.raiyansoft.eata.R
import kotlinx.android.synthetic.main.item_page.view.*

class ViewPagerAdapter : RecyclerView.Adapter<ViewPagerAdapter.PagerVH>() {

    //array of colors to change the background color of screen


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerVH =
        PagerVH(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.item_page, parent, false)
        )

    //get the size of color array
    override fun getItemCount(): Int = 3

    //binding the screen with view
    override fun onBindViewHolder(holder: PagerVH, position: Int) = holder.itemView.run {
        if (position == 0) {
            tvTitle.text = "\u200F1- البحث في الحالات"
            tvAbout.text =
                "جميع الحلات في التطبيق مرتبة حسب القسم ويمكنك البحث عن الحالة الأكثر حاجة و مشاهدة الوصف الخاص بها و المستندات المرفقة و المقارنة بين الحالات و اتخاذ قرار التبرع"
            ivImage.setImageResource(R.drawable.image1)
        }
        if (position == 1) {
            tvTitle.text = "\u200F2- التحدث مع المحتاج"
            tvAbout.text =
                "بعد اختيارك للحالة عليك التحدث للمحتاج للحصول على المزيد من المعلومات و التأكد من صحة الحالة و الحصول على معلومات عنوان المحتاج "
            ivImage.setImageResource(R.drawable.image2)
        }
        if (position == 2) {
            tvTitle.text = "\u200F3- التبرع للمحتاح كاش أو سداد لجهة معينة"
            tvAbout.text =
                "الخطوة الثالثة و الأخيرة هي التوجه إلى عنوان المحتاج واتمام التبرع بحسب اتفاقك معه مثال : تبرع لحالة صحية ينصح بالتوجه للمستشفى مباشرة و تسديد الفواتير "
            ivImage.setImageResource(R.drawable.image3)

        }

    }

    class PagerVH(itemView: View) : RecyclerView.ViewHolder(itemView)
}

