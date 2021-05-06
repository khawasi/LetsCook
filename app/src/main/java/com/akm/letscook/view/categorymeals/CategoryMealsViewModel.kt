package com.akm.letscook.view.categorymeals

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akm.letscook.model.domain.Meal
import com.akm.letscook.repository.IRepositoryTransaction
import com.akm.letscook.util.AppConstants
import com.akm.letscook.util.Resource
import dagger.assisted.Assisted
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryMealsViewModel @Inject constructor(
    private val repo: IRepositoryTransaction,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {

    private var _meals = MutableStateFlow<Resource<List<Meal>>>(Resource.loading())
    val meals = _meals.asStateFlow()

    private var _meal = MutableStateFlow<Meal?>(null)
    val meal = _meal.asStateFlow()

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

    suspend fun setMealForDetail(meal: Meal){
        _meal.emit(meal)
    }

    fun navigatedToMealDetail(){
        _meal.value = null
    }


}