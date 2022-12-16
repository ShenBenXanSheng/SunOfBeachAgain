package com.example.sunofbeachagain.adapter

import android.view.LayoutInflater.from
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.example.sunofbeachagain.R

class CheckImageAdapter(val imageList: List<String>, val activity: AppCompatActivity) :
    PagerAdapter() {
    override fun getCount() = imageList.size

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }


    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val image = imageList[position]

        val checkImageLayout =
            from(container.context).inflate(R.layout.item_check_image, container, false)

        val imageView = checkImageLayout.findViewById<ImageView>(R.id.item_check_image_iv)

        imageView.setOnClickListener {
            activity.finish()

        }


        Glide.with(imageView).load(image).override(1500).into(imageView)


        container.addView(checkImageLayout)

        return checkImageLayout
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View?)
    }


}