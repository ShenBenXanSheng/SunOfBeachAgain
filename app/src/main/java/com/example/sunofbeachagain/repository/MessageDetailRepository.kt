package com.example.sunofbeachagain.repository

import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.sunofbeachagain.domain.MessageDetailData
import com.example.sunofbeachagain.retrofit.RetrofitUtil

class MessageDetailRepository : PagingSource<Int, MessageDetailData>() {
    private var token: String = ""
    private var state: String = ""

    enum class MessageDetailLoadStatus {
        LOADING, SUCCESS, ERROR, EMPTY
    }

    val messageDetailLoadStateLiveData = MutableLiveData<MessageDetailLoadStatus>()

    override fun getRefreshKey(state: PagingState<Int, MessageDetailData>): Int? {
        return null
    }


    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MessageDetailData> {
        val detailList = mutableListOf<MessageDetailData>()
        val currentPage = params.key ?: 1
        if (currentPage == 1) {
            messageDetailLoadStateLiveData.value = MessageDetailLoadStatus.LOADING
        }

        return try {
            when (state) {
                "问答消息" -> {
                    val messageWenDaData =
                        RetrofitUtil.messageApi.getMessageWenDaData(token, currentPage)
                    messageWenDaData.data.content.forEach {
                        val messageDetailData = MessageDetailData(it.avatar,
                            it.nickname,
                            it.createTime,
                            it.title,
                            "去看看问题解决了吗",
                            it.wendaId,
                            "问答消息", it.uid, it.hasRead.toInt(),false,it.id)
                        detailList.add(messageDetailData)
                    }
                }

                "点赞消息" -> {
                    val messageUpUpData =
                        RetrofitUtil.messageApi.getMessageUpUpData(token, currentPage)

                    messageUpUpData.data.content.forEach {
                        val messageDetailData = MessageDetailData(it.avatar,
                            it.nickname,
                            it.thumbTime,
                            "点赞了",
                            it.title,
                            "123",
                            "点赞消息", it.uid, it.hasRead.toInt(),false,it.id)
                        detailList.add(messageDetailData)
                    }
                }

                "摸鱼消息" -> {
                    val messageMoYuData =
                        RetrofitUtil.messageApi.getMessageMoYuData(token, currentPage)

                    messageMoYuData.data.content.forEach {
                        val messageDetailData = MessageDetailData(it.avatar,
                            it.nickname,
                            it.createTime,
                            it.content,
                            it.title,
                            it.momentId,
                            "摸鱼消息", it.uid, it.hasRead.toInt(),false,it.id)
                        detailList.add(messageDetailData)
                    }

                }

                "@消息" -> {
                    val messageAiTeData =
                        RetrofitUtil.messageApi.getMessageAiTeData(token, currentPage)
                    messageAiTeData.data.content.forEach {
                        val title = when (it.type) {
                            "moment" -> "摸鱼消息"
                            "wenda" -> "问答消息"
                            else -> "文章消息"
                        }
                        val messageDetailData = MessageDetailData(it.avatar,
                            it.nickname,
                            it.publishTime,
                            it.content,
                            title,
                            it.exId,
                            title, it.uid, it.hasRead.toInt(),true,it.id)
                        detailList.add(messageDetailData)
                    }
                }
            }

            val prePage = if (currentPage == 1) null else currentPage - 1

            val nextPage = if (detailList.isEmpty()) null else currentPage + 1

            if (nextPage == null) {
                messageDetailLoadStateLiveData.value = MessageDetailLoadStatus.EMPTY
            } else {
                messageDetailLoadStateLiveData.value = MessageDetailLoadStatus.SUCCESS
            }

            LoadResult.Page(detailList, prePage, nextPage)
        } catch (e: Exception) {
            messageDetailLoadStateLiveData.value = MessageDetailLoadStatus.ERROR
            e.printStackTrace()
            LoadResult.Page(mutableListOf(), null, null)

        }

    }

    fun getToken(token: String) {
        this.token = token
    }

    fun getState(state: String) {
        this.state = state
    }
}