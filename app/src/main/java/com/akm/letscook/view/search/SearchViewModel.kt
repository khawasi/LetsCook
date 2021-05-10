package com.akm.letscook.view.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akm.letscook.model.domain.Meal
import com.akm.letscook.repository.IRepositoryTransaction
import com.akm.letscook.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repo: IRepositoryTransaction
) : ViewModel() {

    private var _query = MutableStateFlow("")
    val query = _query.asStateFlow()

    private var _meals = MutableStateFlow<Resource<List<Meal>>>(Resource.loading())
    val meals = _meals.asStateFlow()

    init {
        searchMealsByName("")
    }

    fun searchMealsByName(query: String){
        viewModelScope.launch {
            if (query.length > 2) {
                delay(500)
                repo.searchMealsByName(query)
                    .distinctUntilChanged()
                    .collect {
                    _meals.value = it
                }
            }
        }
    }

    fun setQuery(queryName: String){
        _query.value = queryName
    }

}