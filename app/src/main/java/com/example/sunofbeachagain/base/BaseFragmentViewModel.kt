package com.example.sunofbeachagain.base

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

abstract class BaseFragmentViewModel<A:Activity,vm:ViewModel> :BaseFragment<A>(){
    lateinit var currentViewModel: vm

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        currentViewModel = ViewModelProvider(requireActivity())[getCurrentViewModel()]
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    abstract fun getCurrentViewModel():Class<vm>
}