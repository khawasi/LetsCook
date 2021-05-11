package com.akm.letscook.view

import androidx.lifecycle.ViewModel
import com.akm.letscook.repository.SharedPrefRepository
import com.akm.letscook.util.AppConstants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val sharedPrefRepo: SharedPrefRepository
) : ViewModel() {
    private var _isNightMode =
        MutableStateFlow(sharedPrefRepo.getBoolean(AppConstants.IS_NIGHT_MODE))
    val isNightMode = _isNightMode.asStateFlow()

    fun setIsNightMode(isNightMode: Boolean){
        sharedPrefRepo.putBoolean(AppConstants.IS_NIGHT_MODE, isNightMode)
        _isNightMode.value = isNightMode
    }
}