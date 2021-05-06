package com.akm.letscook.model.db

import com.akm.letscook.model.db.dao.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import javax.inject.Inject

class RoomTransactionImpl @Inject constructor(
    private val dbMealDao: DbMealDao,
    private val dbMealIngredientDao: DbMealIngredientDao,
    private val dbCategoryDao: DbCategoryDao,
    private val dbFavoriteMealDao: DbFavoriteMealDao,
    private val dbMealWithIngredientsDao: DbMealWithIngredientsDao
) : IDbTransaction {
    override suspend fun getMealById(mealId: Long): List<DbMeal> =
        dbMealDao.getDbMealById(mealId)


    override suspend fun getMealsByCategory(categoryName: String): Flow<List<DbMeal>> =
        dbMealDao.getAllMealsByCategory(categoryName).distinctUntilChanged()

    override suspend fun searchMealsByName(name: String): List<DbMeal> =
        dbMealDao.searchDbMealsByName(name)

    override suspend fun upsertMeals(dbMeals: List<DbMeal>) {
        dbMealDao.insertOrUpdate(dbMeals)
    }

    override suspend fun upsertMealsFromCategory(dbMeals: List<DbMeal>, categoryName: String) {
        dbMealDao.insertOrUpdateFromCategory(dbMeals, categoryName)
    }

    override suspend fun getMealIngredientsByMealId(mealId: Long): List<DbMealIngredient> =
        dbMealIngredientDao.getDbMealIngredientsByMealId(mealId)


    override suspend fun insertMealIngredients(dbMealIngredients: List<DbMealIngredient>) {
        dbMealIngredientDao.insert(dbMealIngredients)
    }

    override suspend fun deleteDbMealIngredientsByMealId(mealId: Long) {
        dbMealIngredientDao.deleteDbMealIngredientsByMealId(mealId)
    }

    override suspend fun updateDbMealIngredients(
        mealId: Long,
        dbMealIngredients: List<DbMealIngredient>
    ) =
        dbMealIngredientDao.updateDbMealIngredients(mealId, dbMealIngredients)


    override suspend fun getAllCategories(): Flow<List<DbCategory>> =
        dbCategoryDao.getAllCategories().distinctUntilChanged()


    override suspend fun upsertCategories(categories: List<DbCategory>) {
        dbCategoryDao.insertOrUpdate(categories)
    }

    override suspend fun upsertMiniCategories(categories: List<DbCategory>) {
        dbCategoryDao.insertOrMiniUpdate(categories)
    }

    override suspend fun updateCategoryLastAccessed(lastAccessed: String, name: String) {
        dbCategoryDao.updateLastAccessed(lastAccessed, name)
    }

//    override suspend fun deleteDbCategoryTable() {
//        db.dbCategoryDao()
//    }

    override suspend fun insertDbFavoriteMeal(dbFavoriteMeal: DbFavoriteMeal) {
        dbFavoriteMealDao.insert(dbFavoriteMeal)
    }

    override suspend fun deleteDbFavoriteMeal(dbFavoriteMeal: DbFavoriteMeal) {
        dbFavoriteMealDao.delete(dbFavoriteMeal)
    }

    override suspend fun isMealFavorite(mealId: Long): Boolean =
        dbFavoriteMealDao.isMealFavorite(mealId)


    override suspend fun getAllFavoriteDbMeals(): Flow<List<DbMeal>> =
        dbMealDao.getAllFavoriteDbMeals().distinctUntilChanged()


    override suspend fun getMealWithIngredients(mealId: Long): DbMealWithIngredients =
        dbMealWithIngredientsDao.getMealWithIngredients(mealId)

}