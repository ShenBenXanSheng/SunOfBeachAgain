package com.example.sunofbeachagain.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.sunofbeachagain.domain.bean.EssayList
import com.example.sunofbeachagain.repository.EssayListRepository
import kotlinx.coroutines.flow.Flow

class EssayListViewModel : ViewModel() {
    val essayListRepository = EssayListRepository()


    fun getEssayListData(categoryId: String): Flow<PagingData<EssayList>> {

        essayListRepository.getCategoryId(categoryId)
        return Pager(PagingConfig(1),
            initialKey = null,
            pagingSourceFactory = { essayListRepository }).flow.cachedIn(viewModelScope)
    }
}


