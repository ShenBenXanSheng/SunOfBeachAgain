package com.example.sunofbeachagain.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.sunofbeachagain.databinding.ItemUserCenterFollowOrFansBinding
import com.example.sunofbeachagain.domain.bean.CheckTokenBean
import com.example.sunofbeachagain.domain.bean.UserFollowAndFans

class UserFollowOrFansAdapter : PagingDataAdapter<UserFollowAndFans, InnerHolder>(object :
    DiffUtil.ItemCallback<UserFollowAndFans>() {
    override fun areItemsTheSame(oldItem: UserFollowAndFans, newItem: UserFollowAndFans): Boolean {
        return oldItem.userId == newItem.userId
    }

    override fun areContentsTheSame(
        oldItem: UserFollowAndFans,
        newItem: UserFollowAndFans,
    ): Boolean {
        return oldItem == newItem
    }

}) {
    private lateinit var checkTokenBean: CheckTokenBean

    private lateinit var onFollowOrFansClickListener: OnFollowOrFansClickListener

    override fun onBindViewHolder(holder: InnerHolder, position: Int) {
        val itemUserCenterFollowOrFansBinding =
            holder.dataBinding as ItemUserCenterFollowOrFansBinding
        itemUserCenterFollowOrFansBinding.apply {

            getItem(position)?.let {
                val requestOptions = RequestOptions.bitmapTransform(RoundedCorners(20))

                Glide.with(itemFollowOrFansAvatar).load(it.avatar).apply(requestOptions)
                    .into(itemFollowOrFansAvatar)


                itemFollowOrFansState.text = when (it.relative) {
                    0 -> {
                        "关注"
                    }

                    1 -> {
                        "回粉"
                    }

                    2 -> {
                        "已关注"
                    }

                    else -> {
                        "互相关注"
                    }
                }

                if (this@UserFollowOrFansAdapter::checkTokenBean.isInitialized) {
                    if (it.userId == checkTokenBean.data.id) {
                        itemFollowOrFansState.text = "自己"
                    } else {

                        itemFollowOrFansState.setOnClickListener {view->
                            if (this@UserFollowOrFansAdapter::onFollowOrFansClickListener.isInitialized) {
                                onFollowOrFansClickListener.onFollowOrFansFollowAndUnfollowClick(it.relative,it.userId)
                            }
                        }

                    }
                }

                itemFollowOrFansNickname.text = it.nickname

                itemFollowOrFansAvatar.setOnClickListener { view ->
                    if (this@UserFollowOrFansAdapter::onFollowOrFansClickListener.isInitialized) {
                        onFollowOrFansClickListener.onFollowOrFansAvatarClick(it.userId)
                    }
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InnerHolder {
        return InnerHolder(ItemUserCenterFollowOrFansBinding.inflate(LayoutInflater.from(parent.context),
            parent,
            false))
    }


    fun setOnFollowOrFansClickListener(onFollowOrFansClickListener: OnFollowOrFansClickListener) {
        this.onFollowOrFansClickListener = onFollowOrFansClickListener
    }

    fun getToken(checkTokenBean: CheckTokenBean) {
        this.checkTokenBean = checkTokenBean

        notifyDataSetChanged()
    }

    interface OnFollowOrFansClickListener {
        fun onFollowOrFansAvatarClick(userId: String)

        fun onFollowOrFansFollowAndUnfollowClick(followState: Int, userId: String)
    }
}