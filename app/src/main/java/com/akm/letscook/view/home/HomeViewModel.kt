package com.akm.letscook.view.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akm.letscook.model.domain.Meal
import com.akm.letscook.repository.IRepositoryTransaction
import com.akm.letscook.repository.SharedPrefRepository
import com.akm.letscook.util.AppConstants
import com.akm.letscook.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repo: IRepositoryTransaction,
    private val sharedPrefRepo: SharedPrefRepository,
) : ViewModel() {

    private var _meal = MutableStateFlow<Resource<Meal>>(Resource.loading())
    val meal = _meal.asStateFlow()

    init {
        getRecommendedMeal(
            sharedPrefRepo.getString(AppConstants.CURR_DATE_RECO),
            sharedPrefRepo.getLong(AppConstants.MEAL_ID)
        )
    }

    private fun getRecommendedMeal(savedDate: String, mealId: Long) {
        viewModelScope.launch {
            repo
                .getRecommendedMeal(
                    savedDate,
                    mealId
                )
                .collect {
                    _meal.value = it
                    if (it.status == Resource.Status.SUCCESS && it.data?.lastAccessed != savedDate) {
                        it.data?.let { meal1 ->
                            sharedPrefRepo.putLong(
                                AppConstants.MEAL_ID,
                                meal1.id
                            )
                        }
                        it.data?.let { meal1 ->
                            sharedPrefRepo.putString(
                                AppConstants.CURR_DATE_RECO,
                                meal1.lastAccessed
                            )
                        }
                    }
                }
        }
    }

}