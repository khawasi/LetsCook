package com.akm.letscook.repository

import android.util.Log
import com.akm.letscook.di.qualifier.IoDispatcher
import com.akm.letscook.model.db.*
import com.akm.letscook.model.domain.Category
import com.akm.letscook.model.domain.Meal
import com.akm.letscook.model.network.INetTransaction
import com.akm.letscook.util.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject

class AppRepositoryTransactionImpl @Inject constructor(
    private val dbTransaction: IDbTransaction,
    private val networkTransaction: INetTransaction,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : IRepositoryTransaction {

    private val currDate: String = currDate()

    override suspend fun getRecommendedMeal(savedDate: String, mealId: Long): Flow<Resource<Meal>> =
        flow {
            emit(Resource.loading())

            var newMealId = mealId

            if (currDate != savedDate) {
                try {
                    val netMeals = networkTransaction.getRecommendedMeal()
                    val dbMealWithIngredients = DbMealWithIngredients()

                    dbMealWithIngredients.dbMeal = netMeals[0].mapToDbMeal(currDate)
                    newMealId = netMeals[0].id
                    dbMealWithIngredients.dbMealIngredients =
                        netMeals[0].ingredients.mapToDbMealIngredients(
                            newMealId
                        )
                    dbTransaction.upsertMeals(listOf(dbMealWithIngredients.dbMeal!!))
                    dbTransaction.updateDbMealIngredients(
                        newMealId,
                        dbMealWithIngredients.dbMealIngredients!!
                    )
                } catch (e: Exception) {
                    emit(
                        Resource.error(
                            e.localizedMessage!! +
                                    "\nSomething is wrong. Using the old data inside local storage if exist",
                            Meal()
                        )
                    )
                }
            }

            try {
                emit(Resource.loading())
                emit(
                    Resource.success(
                        dbTransaction
                            .getMealWithIngredients(newMealId)
                            .mapToMealWithIngredients()
                    )
                )
            } catch (e: Exception) {
                emit(Resource.error(e.localizedMessage!!, Meal()))
            }

        }.flowOn(ioDispatcher)


    override suspend fun getCategories(savedDate: String): Flow<Resource<List<Category>>> =
        flow {
            emit(Resource.loading())

            if (currDate != savedDate) {
                try {
                    val netCategories = networkTransaction.getCategories()
                    dbTransaction.upsertMiniCategories(netCategories.mapToDbCategories())
                } catch (e: Exception) {
                    emit(
                        Resource.error(
                            e.localizedMessage!! +
                                    "\nSomething is wrong. Using the old data inside local storage if exist",
                            listOf(Category())
                        )
                    )
                }
            }

            try {
                emit(Resource.loading())
                dbTransaction.getAllCategories().map {
                    it.sortedBy { dbCategory ->
                        dbCategory.name
                    }
                }.collect {
                    emit(Resource.success(it.mapToCategories()))
                }
            } catch (e: IOException) {
                emit(Resource.error(e.localizedMessage!!, listOf(Category())))
            }

        }.flowOn(ioDispatcher)

    override suspend fun getCategoryMeals(savedDate: String, categoryName: String) =
        flow {

            emit(Resource.loading())

            if (savedDate != currDate) {
                try {
                    val netMeals = networkTransaction.getMealsByCategory(categoryName)
                    val dbMeals = netMeals.mapToDbMeals(category = categoryName)
                    dbTransaction.upsertMealsFromCategory(dbMeals, categoryName)
                    dbTransaction.updateCategoryLastAccessed(currDate, categoryName)
                } catch (e: Exception) {

                    emit(
                        Resource.error(
                            e.localizedMessage!! +
                                    "\nSomething is wrong. Using the old data inside local storage if exist",
                            listOf(Meal())
                        )
                    )
                }
            }

            try {
                emit(Resource.loading())
                dbTransaction.getMealsByCategory(categoryName).map {
                    it.sortedBy { dbMeal ->
                        dbMeal.name
                    }
                }.collect {
                    emit(
                        Resource.success(it.mapToMealsFromDb())
                    )
                }
            } catch (e: IOException) {
                emit(Resource.error(e.localizedMessage!!, listOf(Meal())))
            }

        }.flowOn(ioDispatcher)

    override suspend fun getMealByMealId(savedDate: String, mealId: Long): Flow<Resource<Meal>> =
        flow {
            emit(Resource.loading())

            if (currDate != savedDate) {
                try {
                    val netMeals = networkTransaction.getMealById(mealId)
                    val dbMealWithIngredients = DbMealWithIngredients()

                    dbMealWithIngredients.dbMeal = netMeals[0].mapToDbMeal(currDate)
                    dbMealWithIngredients.dbMealIngredients =
                        netMeals[0].ingredients.mapToDbMealIngredients(
                            netMeals[0].id
                        )
                    dbTransaction.upsertMeals(listOf(dbMealWithIngredients.dbMeal!!))
                    dbTransaction.updateDbMealIngredients(
                        netMeals[0].id,
                        dbMealWithIngredients.dbMealIngredients!!
                    )
                } catch (e: Exception) {

                    emit(
                        Resource.error(
                            e.localizedMessage!! +
                                    "\nSomething is wrong. Using the old data inside local storage if exist",
                            Meal()
                        )
                    )

                }
            }

            try {
                emit(Resource.loading())
                emit(
                    Resource.success(
                        dbTransaction
                            .getMealWithIngredients(mealId)
                            .mapToMealWithIngredients()
                    )
                )
            } catch (e: IOException) {
                emit(Resource.error(e.localizedMessage!!, Meal()))
            }

        }.flowOn(ioDispatcher)

    override suspend fun searchMealsByName(query: String): Flow<Resource<List<Meal>>> =
        flow {
            emit(Resource.loading())

            try {
                emit(
                    Resource.success(
                        networkTransaction.searchMealsByName(query).mapToMealsFromNet()
                    )
                )
            } catch (e: Exception) {
                try {
                    dbTransaction.searchMealsByName(query).map {
                        it.sortedBy { dbMeal ->
                            dbMeal.name
                        }
                    }.collect{
                        emit(
                            Resource.error(
                                e.localizedMessage!! +
                                        "\nSomething is wrong. Using the old data inside local storage if exist",
                                it.mapToMealsFromDb()
                            )
                        )
                    }
                } catch (e2: Exception) {
                    emit(Resource.error(e2.localizedMessage!!, listOf(Meal())))
                }
            }

        }
            .distinctUntilChanged()
            .flowOn(ioDispatcher)

    override suspend fun getFavoriteMeals() =
        flow {
            emit(Resource.loading())
            try {
                dbTransaction.getAllFavoriteDbMeals().map {
                    it.sortedBy { dbMeal ->
                        dbMeal.name
                    }
                }.collect {
                    emit(Resource.success(it.mapToMealsFromDb()))
                }
            } catch (e: IOException) {
                emit(Resource.error(e.localizedMessage!!, listOf(Meal())))
            }
        }.flowOn(ioDispatcher)

    override suspend fun isMealFavorite(mealId: Long) =
        flow {
            try {
                emit(dbTransaction.isMealFavorite(mealId))
            } catch (e: IOException) {
                Log.e("REPO_ISMEALFAVORITE", e.localizedMessage!!)
                emit(false)
            }
        }.flowOn(ioDispatcher)

    override suspend fun setMealToFavorite(mealId: Long) {
        withContext(ioDispatcher) {
            dbTransaction.insertDbFavoriteMeal(
                DbFavoriteMeal(mealId)
            )
        }
    }

    override suspend fun deleteMealFromFavorite(mealId: Long) {
        withContext(ioDispatcher) {
            dbTransaction.deleteDbFavoriteMeal(
                DbFavoriteMeal(mealId)
            )
        }
    }
}