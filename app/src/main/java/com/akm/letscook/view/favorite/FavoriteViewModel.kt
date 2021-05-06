package com.akm.letscook.view.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akm.letscook.model.domain.Meal
import com.akm.letscook.repository.IRepositoryTransaction
import com.akm.letscook.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val repo: IRepositoryTransaction
): ViewModel() {

    private var _meals = MutableStateFlow<Resource<List<Meal>>>(Resource.loading())
    val meals = _meals.asStateFlow()

    private var _meal = MutableStateFlow<Meal?>(null)
    val meal = _meal.asStateFlow()

    private fun getFavoriteMeals() {
        viewModelScope.launch {
            repo.getFavoriteMeals().collect {
                _meals.value = it
            }
        }
    }

    init {
        getFavoriteMeals()
    }

    suspend fun setMealForDetail(meal: Meal){
        _meal.emit(meal)
    }

    fun navigatedToMealDetail(){
        _meal.value = null
    }
}