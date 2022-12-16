package com.example.sunofbeachagain.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.sunofbeachagain.R
import com.example.sunofbeachagain.base.BaseApp
import com.example.sunofbeachagain.databinding.ItemMoyuListBinding
import com.example.sunofbeachagain.domain.ItemMoYuListData
import com.example.sunofbeachagain.domain.bean.MoYuData
import com.example.sunofbeachagain.utils.StringUtil
import java.text.SimpleDateFormat

class MoYuListAdapter() :
    PagingDataAdapter<MoYuData, InnerHolder>(object : DiffUtil.ItemCallback<MoYuData>() {
        override fun areItemsTheSame(oldItem: MoYuData, newItem: MoYuData): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: MoYuData, newItem: MoYuData): Boolean {
            return oldItem == newItem
        }

    }) {


    private var contentLines: Int = 0
    private lateinit var onMoYuListItemClickListener: OnMoYuListItemClickListener

    private val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm")
    private val oneDay = 86400000
    private val oneHour = 3600000

    private var myUserId = ""


    val sp = BaseApp.mContext?.getSharedPreferences("UpSp", Context.MODE_PRIVATE)

    val upList = mutableListOf<String>()


    override fun onBindViewHolder(holder: InnerHolder, position: Int) {
        val itemMoyuListBinding = holder.dataBinding as ItemMoyuListBinding
        itemMoyuListBinding.apply {
            getItem(position)?.let { itemData ->

                itemMoyuImageRv.visibility = if (itemData.images.isNullOrEmpty()) {
                    View.GONE
                } else {
                    View.VISIBLE
                }

                itemMoyuLinkContainer.visibility =
                    if (itemData.linkUrl.isNullOrEmpty() || itemData.linkTitle.isNullOrEmpty()) {
                        View.GONE
                    } else {
                        View.VISIBLE
                    }

                val moYuPosition = if (itemData.position.isNullOrEmpty()) {
                    "刁民"
                } else {
                    itemData.position
                }

                val moYuTheme = if (itemData.topicName.isNullOrEmpty()) {
                    "摸鱼"
                } else {
                    itemData.topicName
                }

                //TODO::点赞

//==================================计算时间====================================
                val currentTime = System.currentTimeMillis()

                val dataTime = simpleDateFormat.parse(itemData.createTime)!!.time

                val intervalTime = currentTime - dataTime

                val format = simpleDateFormat.format(dataTime)
                //大于一天
                val moYuTime = if (intervalTime >= oneDay * 2) {
                    format

                } else if (intervalTime >= oneDay) {
                    "昨天${
                        format.substring(format.indexOf(' '), format.length)
                    }"

                } else if (intervalTime <= oneHour) {
                    "${(intervalTime) / 60 / 1000}分钟前"

                } else {

                    "${intervalTime / 60 / 60 / 1000}小时前"
                }



                itemMoyuContent.maxLines = contentLines

                val itemMoYuData = ItemMoYuListData(itemData.avatar,
                    itemData.nickname,
                    moYuPosition,
                    moYuTime,
                    StringUtil.stripHtml(itemData.content).replace("&nbsp", " "),
                    itemData.images,
                    itemData.linkTitle,
                    itemData.linkUrl,
                    moYuTheme,
                    itemData.commentCount.toString(),
                    itemData.thumbUpCount.toString())

                val requestOptions = RequestOptions.bitmapTransform(RoundedCorners(20))

                Glide.with(itemMoyuAvatar).load(itemData.avatar)
                    .apply(requestOptions)
                    .into(itemMoyuAvatar)

                moYuData = itemMoYuData

                root.setOnClickListener {
                    if (this@MoYuListAdapter::onMoYuListItemClickListener.isInitialized) {
                        onMoYuListItemClickListener.onMoYuListItemClick(itemData.id)
                    }
                }


                val moYuImageAdapter = MoYuImageAdapter()
                if (!itemData.images.isNullOrEmpty()) {
                    if (itemData.images.size % 3 == 0) {
                        itemMoyuImageRv.layoutManager = GridLayoutManager(BaseApp.mContext, 3)
                    } else {
                        if (itemData.images.size != 8 && itemData.images.size % 2 == 0) {
                            itemMoyuImageRv.layoutManager = GridLayoutManager(BaseApp.mContext, 2)
                        } else {
                            if (itemData.images.size == 1) {
                                itemMoyuImageRv.layoutManager =
                                    GridLayoutManager(BaseApp.mContext, 1)
                            } else {
                                if (itemData.images.size == 5) {
                                    itemMoyuImageRv.layoutManager =
                                        GridLayoutManager(BaseApp.mContext, 3)
                                } else {
                                    if (itemData.images.size == 7) {
                                        itemMoyuImageRv.layoutManager =
                                            GridLayoutManager(BaseApp.mContext, 4)
                                    }
                                }
                            }
                        }
                    }

                    itemMoyuImageRv.adapter = moYuImageAdapter
                }



                if (itemData.thumbUpList != null) {
                    if (itemData.thumbUpList.contains(myUserId)) {
                        itemMoyuUpupTv.setTextColor(ContextCompat.getColor(BaseApp.mContext!!,
                            R.color.mainColor))
                        itemMoyuUpupIv.setColorFilter(ContextCompat.getColor(BaseApp.mContext!!,
                            R.color.mainColor))

                    } else {
                        itemMoyuUpupTv.setTextColor(ContextCompat.getColor(BaseApp.mContext!!,
                            R.color.gray2))
                        itemMoyuUpupIv.setColorFilter(ContextCompat.getColor(BaseApp.mContext!!,
                            R.color.gray2))


                    }
                }



                sp?.getStringSet("positionSet", null)?.forEach {
                    if (it.toInt() == position) {
                        itemMoyuUpupTv.setTextColor(ContextCompat.getColor(BaseApp.mContext!!,
                            R.color.mainColor))
                        itemMoyuUpupIv.setColorFilter(ContextCompat.getColor(BaseApp.mContext!!,
                            R.color.mainColor))

                    }
                }



                itemMoyuUpupContainer.setOnClickListener {
                    if (this@MoYuListAdapter::onMoYuListItemClickListener.isInitialized) {
                        val onMoYuUpUpClick =
                            onMoYuListItemClickListener.onMoYuUpUpClick(itemData.id,
                                itemData.thumbUpList)


                        if (onMoYuUpUpClick) {
                            upList.add(position.toString())

                            sp?.edit()?.putStringSet("positionSet", upList.toMutableSet())?.apply()

                            itemMoyuUpupTv.setTextColor(ContextCompat.getColor(BaseApp.mContext!!,
                                R.color.mainColor))

                            itemMoyuUpupIv.setColorFilter(ContextCompat.getColor(BaseApp.mContext!!,
                                R.color.mainColor))

                            itemMoyuUpupTv.text = (itemData.thumbUpCount + 1).toString()
                        }
                    }
                }

                moYuImageAdapter.setOnMoYuImageClickListener(object
                    : MoYuImageAdapter.OnMoYuImageClickListener {
                    override fun onMoYuImageClick(images: List<String>, position: Int) {
                        if (this@MoYuListAdapter::onMoYuListItemClickListener.isInitialized) {
                            onMoYuListItemClickListener.onMoYuListImageClick(images, position)
                        }
                    }

                })


                itemMoyuLinkContainer.setOnClickListener {
                    if (this@MoYuListAdapter::onMoYuListItemClickListener.isInitialized) {
                        onMoYuListItemClickListener.onMoYuListLinkClick(itemData.linkUrl.toString())
                    }
                }

                itemMoyuAvatar.setOnClickListener {
                    if (this@MoYuListAdapter::onMoYuListItemClickListener.isInitialized) {
                        onMoYuListItemClickListener.onMoYuUserCenterClick(itemData.userId)
                    }
                }
                moYuImageAdapter.setImageData(itemData.images)
            }
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InnerHolder {
        val dataBinding =
            ItemMoyuListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return InnerHolder(dataBinding)
    }

    fun setOnMoYuListItemClickListener(onMoYuListItemClickListener: OnMoYuListItemClickListener) {
        this.onMoYuListItemClickListener = onMoYuListItemClickListener
    }

    interface OnMoYuListItemClickListener {
        fun onMoYuListItemClick(id: String)

        fun onMoYuListImageClick(images: List<String>, position: Int)

        fun onMoYuListLinkClick(linkUrl: String)

        fun onMoYuUserCenterClick(userId: String)

        fun onMoYuUpUpClick(momentId: String, thumbUpList: List<String>?): Boolean
    }


    fun getContentLine(contentLine: Int) {
        contentLines = contentLine

        notifyDataSetChanged()
    }

    fun getMyUserId(id: String) {
        myUserId = id

        notifyDataSetChanged()
    }


}