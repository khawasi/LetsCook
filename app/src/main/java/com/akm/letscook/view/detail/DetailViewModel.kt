package com.akm.letscook.view.detail

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akm.letscook.di.qualifier.IoDispatcher
import com.akm.letscook.model.domain.Meal
import com.akm.letscook.repository.IRepositoryTransaction
import com.akm.letscook.util.AppConstants
import com.akm.letscook.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val repo: IRepositoryTransaction,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private var _meal = MutableStateFlow<Resource<Meal>>(Resource.loading())
    val meal = _meal.asStateFlow()

    private var _isFavorite = MutableStateFlow(false)
    val isFavorite = _isFavorite.asStateFlow()

    private val mealId: Long = savedStateHandle.get<Long>(AppConstants.MEAL_ID)!!
    private val mealLastAccessed: String = savedStateHandle.get<String>("savedDate")!!

    init {
        getMealByMealId(mealLastAccessed, mealId)
        isMealFavorite(mealId)
    }

    private fun getMealByMealId(savedDate: String, mealId: Long) {
        viewModelScope.launch {
            repo.getMealByMealId(savedDate, mealId).collect {
                _meal.value = it
            }
        }
    }

    private fun isMealFavorite(mealId: Long) {
        viewModelScope.launch {
            repo.isMealFavorite(mealId).collect {
                _isFavorite.value = it
                Log.v("DETAIL FAVORITE", "TEST $it")
            }
        }
    }

    fun clickFavoriteMeal() {
        viewModelScope.launch {
            Log.v("DETAIL FAVORITE", "TEST CLICK")
            if (_isFavorite.value) {
                deleteMealFromFavorite()
            } else {
                setMealToFavorite()
            }
        }
    }

    private suspend fun setMealToFavorite() {
        Log.v("DETAIL FAVORITE", "TEST SET")
        repo.setMealToFavorite(mealId)
        _isFavorite.value = true
    }

    private suspend fun deleteMealFromFavorite() {
        Log.v("DETAIL FAVORITE", "TEST DELEET")
        repo.deleteMealFromFavorite(mealId)
        _isFavorite.value = false
    }

}