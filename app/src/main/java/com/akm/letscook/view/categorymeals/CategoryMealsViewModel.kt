package com.akm.letscook.view.categorymeals

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akm.letscook.model.domain.Meal
import com.akm.letscook.repository.IRepositoryTransaction
import com.akm.letscook.util.AppConstants
import com.akm.letscook.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryMealsViewModel @Inject constructor(
    private val repo: IRepositoryTransaction,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    private var _meals = MutableStateFlow<Resource<List<Meal>>>(Resource.loading())
    val meals = _meals.asStateFlow()

    private val savedDate: String = savedStateHandle.get<String>("savedDate")!!
    private val categoryName: String = savedStateHandle.get<String>(AppConstants.CATEGORY_NAME)!!

    init {
        getCategoryMeals(savedDate, categoryName)
    }

    private fun getCategoryMeals(savedDate: String, categoryName: String){
        viewModelScope.launch {
            repo.getCategoryMeals(savedDate, categoryName).collect {
                _meals.value = it
            }
        }
    }
}