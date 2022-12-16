package com.example.sunofbeachagain.repository

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.sunofbeachagain.domain.bean.MoYuComment
import com.example.sunofbeachagain.retrofit.RetrofitUtil

class MoYuCommentRepository : PagingSource<Int, MoYuComment>() {
    private var momentId: String = ""

    override fun getRefreshKey(state: PagingState<Int, MoYuComment>): Int? {
        return null
    }




    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MoYuComment> {
        val currentPage = params.key ?: 1


        return try {
            val moYuDetailComment = RetrofitUtil.moYuApi.getMoYuDetailComment(momentId, currentPage)


            val prePage = if (currentPage == 1) null else currentPage - 1

            val nextPage = if (moYuDetailComment.data.list.isEmpty()) null else currentPage + 1

            LoadResult.Page(moYuDetailComment.data.list, prePage, nextPage)

        } catch (e: Exception) {
            e.printStackTrace()
            LoadResult.Error(e)

        }
    }

    fun getMomentId(momentId: String) {
        this.momentId = momentId
    }
}