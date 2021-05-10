package com.akm.letscook.view.category

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akm.letscook.model.domain.Category
import com.akm.letscook.repository.IRepositoryTransaction
import com.akm.letscook.repository.SharedPrefRepository
import com.akm.letscook.util.AppConstants
import com.akm.letscook.util.Resource
import com.akm.letscook.util.currDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val repo: IRepositoryTransaction,
    private val sharedPrefRepo: SharedPrefRepository,
) : ViewModel() {

    private var _categories = MutableStateFlow<Resource<List<Category>>>(Resource.loading())
    val categories = _categories.asStateFlow()

    private val currDate = currDate()

    init {
        Log.v("CATEGORY", "VM INIT")
        getAllCategories(sharedPrefRepo.getString(AppConstants.CURR_DATE_CAT))
    }

    private fun getAllCategories(savedDate: String) {
        viewModelScope.launch {
            Log.v("CATEGORY", "VM METHOD")
            repo
                .getCategories(savedDate)
                .collect {
                    _categories.value = it
                    if (it.status == Resource.Status.SUCCESS && currDate != savedDate) {
                        sharedPrefRepo.putString(AppConstants.CURR_DATE_CAT, currDate)
                    }
                }
        }
    }
}