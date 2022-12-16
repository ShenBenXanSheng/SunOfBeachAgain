package com.example.sunofbeachagain.repository

import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.sunofbeachagain.domain.bean.EssayList
import com.example.sunofbeachagain.retrofit.RetrofitUtil

class EssayListRepository : PagingSource<Int, EssayList>() {
    private var categoryId: String = ""

    enum class EssayListLoadStatus {
        LOADING, SUCCESS, ERROR, EMPTY
    }

    val essayListLoadStateLiveData = MutableLiveData<EssayListLoadStatus>()

    override fun getRefreshKey(state: PagingState<Int, EssayList>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, EssayList> {
        val currentPage = params.key ?: 1
        if (currentPage == 1){
            essayListLoadStateLiveData.value = EssayListLoadStatus.LOADING
        }


        return try {
            val essayListData = if (categoryId == "114514") {
                RetrofitUtil.essayApi.getEssayListRecommendData(currentPage)
            } else {
                RetrofitUtil.essayApi.getEssayListByCategoryId(categoryId, currentPage)
            }

            val prePage = if (currentPage == 1) null else currentPage - 1

            val nextPage = if (essayListData.data.list.isEmpty()) null else currentPage + 1

            if (essayListData.data.list.isEmpty()){
                essayListLoadStateLiveData.value = EssayListLoadStatus.EMPTY
            }else{
                essayListLoadStateLiveData.value = EssayListLoadStatus.SUCCESS
            }

            return LoadResult.Page(essayListData.data.list, prePage, nextPage)
        } catch (e: Exception) {
            essayListLoadStateLiveData.value = EssayListLoadStatus.ERROR
            LoadResult.Page(mutableListOf(), null, null)
        }


    }

    fun getCategoryId(categoryId: String) {
        this.categoryId = categoryId
    }
}