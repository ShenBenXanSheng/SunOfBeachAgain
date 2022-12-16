package com.example.sunofbeachagain.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import com.example.sunofbeachagain.databinding.ItemLoadMoreBinding

class LoadMoreAdapter :LoadStateAdapter<InnerHolder>(){
    override fun onBindViewHolder(holder: InnerHolder, loadState: LoadState) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): InnerHolder {
        val itemLoadMoreBinding = ItemLoadMoreBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return InnerHolder(itemLoadMoreBinding)
    }
}