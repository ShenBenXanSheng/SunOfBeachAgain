package com.example.sunofbeachagain.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.sunofbeachagain.base.BaseViewModel
import com.example.sunofbeachagain.domain.EssayCategoryIdAndNameData
import com.example.sunofbeachagain.retrofit.RetrofitUtil
import kotlinx.coroutines.launch

class EssayViewModel : BaseViewModel() {
    private val categoryList = mutableListOf<EssayCategoryIdAndNameData>()

    val categoryDataLiveData = MutableLiveData<List<EssayCategoryIdAndNameData>>()

    enum class EssayCategoryLoadStatus {
        LOADING, SUCCESS, EMPTY, ERROR
    }

    val essayCategoryLoadStateLiveData = MutableLiveData<EssayCategoryLoadStatus>()
    fun getEssayCategoryId() {
        categoryList.clear()
        essayCategoryLoadStateLiveData.value = EssayCategoryLoadStatus.LOADING
        viewModelScope.launch {
            try {
                val essayCategoryIdAndNameData = EssayCategoryIdAndNameData("114514", "推荐")
                categoryList.add(essayCategoryIdAndNameData)

                val essayCategory = RetrofitUtil.essayApi.getEssayCategory()

                if (essayCategory.data.isEmpty()) {
                    essayCategoryLoadStateLiveData.value = EssayCategoryLoadStatus.EMPTY
                } else {
                    essayCategoryLoadStateLiveData.value = EssayCategoryLoadStatus.SUCCESS
                    essayCategory.data.forEach {
                        val essayCategoryIdAndNameData2 =
                            EssayCategoryIdAndNameData(it.id, it.categoryName)
                        categoryList.add(essayCategoryIdAndNameData2)
                    }

                    categoryDataLiveData.postValue(categoryList)
                }

            } catch (e: Exception) {
                essayCategoryLoadStateLiveData.value = EssayCategoryLoadStatus.ERROR
                e.printStackTrace()
            }

        }

    }
}