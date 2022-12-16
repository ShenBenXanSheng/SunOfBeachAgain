package com.example.sunofbeachagain.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sunofbeachagain.databinding.ItemMoyuMomentBinding

class MoYuMomentAdapter : RecyclerView.Adapter<InnerHolder>() {
    private lateinit var onMoYuMomentItemClickListener: OnMoYuMomentItemClickListener

    private val imageList = mutableListOf<Any>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InnerHolder {
        return InnerHolder(ItemMoyuMomentBinding.inflate(LayoutInflater.from(parent.context),
            parent,
            false))
    }

    override fun onBindViewHolder(holder: InnerHolder, position: Int) {
        val image = imageList[position]

        val itemMoyuMomentBinding = holder.dataBinding as ItemMoyuMomentBinding

        Glide.with(itemMoyuMomentBinding.itemMoyuMomentCover).load(image)
            .into(itemMoyuMomentBinding.itemMoyuMomentCover)


        itemMoyuMomentBinding.itemMoyuMomentCover.setOnClickListener {


            if (this::onMoYuMomentItemClickListener.isInitialized) {
                if (image is Int) {
                    imageList.removeAt(imageList.size - 1)
                    onMoYuMomentItemClickListener.addImageAgain(imageList)
                } else {
                    onMoYuMomentItemClickListener.checkImage(imageList, position)
                }
            }

        }
    }

    override fun getItemCount() = imageList.size

    fun setData(currentImageList: MutableList<Any>) {
        imageList.clear()
        imageList.addAll(currentImageList)
        notifyDataSetChanged()
    }

    fun setOnMoYuMomentItemClickListener(onMoYuMomentItemClickListener: OnMoYuMomentItemClickListener) {
        this.onMoYuMomentItemClickListener = onMoYuMomentItemClickListener
    }

    interface OnMoYuMomentItemClickListener {
        fun addImageAgain(imageList: MutableList<Any>)

        fun checkImage(imageList: MutableList<Any>, position: Int)
    }

}