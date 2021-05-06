package com.akm.letscook.model.network

interface INetTransaction {
    suspend fun getMealById(mealId: Long): List<NetMeal>
    suspend fun getRecommendedMeal(): List<NetMeal>
    suspend fun getMealsByCategory(categoryName: String): List<NetMeal>
    suspend fun searchMealsByName(queryName: String): List<NetMeal>
    suspend fun getCategories(): NetCategories
}