package com.example.sunofbeachagain.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.example.sunofbeachagain.databinding.ItemTestBinding.inflate

class TestAdapter : RecyclerView.Adapter<TestAdapter.InnerHolder>() {
    class InnerHolder(itemView: View, val dataBinding: ViewDataBinding) :
        RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InnerHolder {
        val inflate = inflate(LayoutInflater.from(parent.context))
        return InnerHolder(inflate.root, inflate)
    }

    override fun onBindViewHolder(holder: InnerHolder, position: Int) {

    }

    override fun getItemCount() = 100
}