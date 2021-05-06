package com.akm.letscook.model.db.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.akm.letscook.model.db.DbCategory
import com.akm.letscook.model.db.DbMeal
import kotlinx.coroutines.flow.Flow

@Dao
abstract class DbMealDao: BaseDao<DbMeal>() {

    @Query("select * from dbmeal where id = :mealId")
    abstract fun getDbMealById(mealId: Long): List<DbMeal>

    @Query("select * from dbMeal where id in (select mealId from dbfavoritemeal)")
    abstract fun getAllFavoriteDbMeals(): Flow<List<DbMeal>>

    @Query("select * from dbmeal where category = :categoryName")
    abstract fun getAllMealsByCategory(categoryName: String): Flow<List<DbMeal>>

    @Query("update dbmeal set name = :name, thumbnailUrl = :thumbnailUrl, category = :category where id = :id")
    abstract fun updateFromCategory(id: Long, name: String, thumbnailUrl: String, category: String)

    @Query("select * from dbmeal where name = :name")
    abstract fun searchDbMealsByName(name: String): List<DbMeal>

    @Transaction
    open fun insertOrUpdateFromCategory(objList: List<DbMeal>, category: String) {
        val insertResult = insert(objList)

        for (i in insertResult.indices) {
            if (insertResult[i] == -1L) {
                updateFromCategory(objList[i].id, objList[i].name, objList[i].thumbnailUrl, category)
            }
        }

    }
}