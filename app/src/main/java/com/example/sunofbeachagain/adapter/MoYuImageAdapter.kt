package com.example.sunofbeachagain.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sunofbeachagain.databinding.ItemMoyuListImagesBinding

class MoYuImageAdapter : RecyclerView.Adapter<InnerHolder>() {
    private lateinit var onMoYuImageClickListener: OnMoYuImageClickListener

    private val imagesList = mutableListOf<String>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InnerHolder {
        return InnerHolder(ItemMoyuListImagesBinding.inflate(LayoutInflater.from(parent.context),
            parent,
            false))
    }

    override fun onBindViewHolder(holder: InnerHolder, position: Int) {
        val itemMoyuListImagesBinding = holder.dataBinding as ItemMoyuListImagesBinding
        val image = imagesList[position]


        itemMoyuListImagesBinding.itemMoyuImage.setOnClickListener {


            if (this::onMoYuImageClickListener.isInitialized) {
                onMoYuImageClickListener.onMoYuImageClick(imagesList, position)
            }
        }

        Glide.with(itemMoyuListImagesBinding.itemMoyuImage).load(image)
            .into(itemMoyuListImagesBinding.itemMoyuImage)
    }

    override fun getItemCount() = imagesList.size

    fun setImageData(images: List<String>?) {
        imagesList.clear()
        if (images != null) {
            imagesList.addAll(images)
        }
        notifyDataSetChanged()
    }

    fun setOnMoYuImageClickListener(onMoYuImageClickListener: OnMoYuImageClickListener) {
        this.onMoYuImageClickListener = onMoYuImageClickListener
    }

    interface OnMoYuImageClickListener {
        fun onMoYuImageClick(images: List<String>, position: Int)
    }
}