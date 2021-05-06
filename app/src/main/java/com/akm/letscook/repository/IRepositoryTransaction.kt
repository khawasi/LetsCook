package com.akm.letscook.repository

import com.akm.letscook.model.domain.Category
import com.akm.letscook.model.domain.Meal
import com.akm.letscook.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface IRepositoryTransaction {

    suspend fun getRecommendedMeal(
        savedDate: String,
        mealId: Long
    ): Flow<Resource<Meal>>

    suspend fun getCategories(savedDate: String): Flow<Resource<List<Category>>>

    suspend fun getCategoryMeals(savedDate: String, categoryName: String): Flow<Resource<List<Meal>>>

    suspend fun getMealByMealId(savedDate: String, mealId: Long): Flow<Resource<Meal>>

    suspend fun searchMealsByName(query: String): Flow<Resource<List<Meal>>>

    suspend fun getFavoriteMeals(): Flow<Resource<List<Meal>>>

    suspend fun isMealFavorite(mealId: Long): Flow<Boolean>

    suspend fun setMealToFavorite(mealId: Long)

    suspend fun deleteMealFromFavorite(mealId: Long)
}