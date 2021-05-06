package com.akm.letscook.model.db

import kotlinx.coroutines.flow.Flow

interface IDbTransaction {
    suspend fun getMealById(mealId: Long): List<DbMeal>
    suspend fun getMealsByCategory(categoryName: String): Flow<List<DbMeal>>
    suspend fun searchMealsByName(name: String): List<DbMeal>
    suspend fun upsertMeals(dbMeals: List<DbMeal>)
    suspend fun upsertMealsFromCategory(dbMeals: List<DbMeal>, categoryName: String)

    suspend fun getMealIngredientsByMealId(mealId: Long): List<DbMealIngredient>
    suspend fun insertMealIngredients(dbMealIngredients: List<DbMealIngredient>)
    suspend fun deleteDbMealIngredientsByMealId(mealId: Long)
    suspend fun updateDbMealIngredients(mealId: Long, dbMealIngredients: List<DbMealIngredient>)

    suspend fun getAllCategories(): Flow<List<DbCategory>>
    suspend fun upsertCategories(categories: List<DbCategory>)
    suspend fun upsertMiniCategories(categories: List<DbCategory>)
    suspend fun updateCategoryLastAccessed(lastAccessed: String, name: String)
//    suspend fun deleteDbCategoryTable()

    suspend fun insertDbFavoriteMeal(dbFavoriteMeal: DbFavoriteMeal)
    suspend fun deleteDbFavoriteMeal(dbFavoriteMeal: DbFavoriteMeal)
    suspend fun isMealFavorite(mealId: Long): Boolean
    suspend fun getAllFavoriteDbMeals(): Flow<List<DbMeal>>

    suspend fun getMealWithIngredients(mealId: Long): DbMealWithIngredients
}