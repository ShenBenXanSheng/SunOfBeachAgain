package com.example.sunofbeachagain.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.example.sunofbeachagain.databinding.ItemItemShouCangDetailBinding
import com.example.sunofbeachagain.domain.bean.UserShouCangDetail

class UserShouCangDetailAdapter : PagingDataAdapter<UserShouCangDetail, InnerHolder>(object :
    DiffUtil.ItemCallback<UserShouCangDetail>() {
    override fun areItemsTheSame(
        oldItem: UserShouCangDetail,
        newItem: UserShouCangDetail,
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: UserShouCangDetail,
        newItem: UserShouCangDetail,
    ): Boolean {
        return oldItem == newItem
    }

}) {
    private lateinit var onUserShouCangDetailItemClickListener: OnUserShouCangDetailItemClickListener


    override fun onBindViewHolder(holder: InnerHolder, position: Int) {

        (holder.dataBinding as ItemItemShouCangDetailBinding).apply {
            getItem(position)?.let {

                itemShoucangDetailName.text = it.title
                itemShoucangDetailTime.text = it.addTime

                root.setOnClickListener { view ->
                    val indexOf = it.url.indexOf('1')

                    val substring = it.url.substring(indexOf , it.url.length)

                    Log.d("substring",substring)

                    if (this@UserShouCangDetailAdapter::onUserShouCangDetailItemClickListener.isInitialized) {
                        onUserShouCangDetailItemClickListener.onShouCangDetailItemClick(substring)
                    }
                }

                root.setOnLongClickListener {v->
                    if (this@UserShouCangDetailAdapter::onUserShouCangDetailItemClickListener.isInitialized) {
                        onUserShouCangDetailItemClickListener.onDeleteShouCangLongClick(it.url)
                    }
                    true
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InnerHolder {
        return InnerHolder(ItemItemShouCangDetailBinding.inflate(LayoutInflater.from(parent.context),
            parent,
            false))
    }

    fun setOnUserShouCangDetailItemClickListener(onUserShouCangDetailItemClickListener: OnUserShouCangDetailItemClickListener) {
        this.onUserShouCangDetailItemClickListener = onUserShouCangDetailItemClickListener
    }

    interface OnUserShouCangDetailItemClickListener {
        fun onShouCangDetailItemClick(essayId: String)

        fun onDeleteShouCangLongClick(url:String)
    }
}