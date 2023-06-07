package com.manuchar.themoviedb.presentation.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf

abstract class BaseViewModel : ViewModel() {

    protected fun onInit() = flowOf(1)
}