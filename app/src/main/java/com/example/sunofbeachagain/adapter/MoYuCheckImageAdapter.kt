package com.example.sunofbeachagain.adapter

import android.view.LayoutInflater.from
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.example.sunofbeachagain.R


class MoYuCheckImageAdapter : PagerAdapter() {
    private val images = mutableListOf<String>()

    override fun getCount() = images.size

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val root = from(container.context).inflate(R.layout.item_check_image, container, false)

        val image = images[position]

        val imageView = root.findViewById<ImageView>(R.id.item_check_image_iv)

        Glide.with(imageView).load(image).into(imageView)

        container.addView(root)
        return root
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View?)
    }

    fun setData(imageList: List<String>) {
        images.clear()
        images.addAll(imageList)

        notifyDataSetChanged()
    }
}