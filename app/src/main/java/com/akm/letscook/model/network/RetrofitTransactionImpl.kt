package com.akm.letscook.model.network

import com.akm.letscook.di.qualifier.ApiKey
import kotlinx.coroutines.withTimeout
import javax.inject.Inject

class RetrofitTransactionImpl @Inject constructor(
    private val apiService: IApiService,
    @ApiKey private val apiKey: String
) : INetTransaction {

    override suspend fun getMealById(mealId: Long): List<NetMeal> {
        return withTimeout(10_000) {
            apiService.getMealById(apiKey, mealId)
        }
    }

    override suspend fun getRecommendedMeal(): List<NetMeal> {
        return withTimeout(10_000) {
            apiService.getRecommendedMeal(apiKey)
        }
    }


    override suspend fun getMealsByCategory(categoryName: String): List<NetMeal> {
        return withTimeout(10_000) {
            apiService.getMealsByCategory(apiKey, categoryName)
        }
    }

    override suspend fun searchMealsByName(queryName: String): List<NetMeal> {
        return withTimeout(10_000) {
            apiService.searchMealsByName(apiKey, queryName)
        }
    }

    override suspend fun getCategories(): NetCategories {
        return withTimeout(10_000) {
            apiService.getCategories(apiKey)
        }
    }
}