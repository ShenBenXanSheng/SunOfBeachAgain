package com.example.sunofbeachagain.base

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

abstract class BaseActivityViewModel<vm : ViewModel> : BaseActivity() {

    var currentViewModel: vm? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        currentViewModel = ViewModelProvider(this)[getCurrentViewModel()]
        super.onCreate(savedInstanceState)
    }


    abstract fun getCurrentViewModel(): Class<vm>
}