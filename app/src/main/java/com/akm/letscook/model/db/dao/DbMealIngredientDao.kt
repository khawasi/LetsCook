package com.akm.letscook.model.db.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.akm.letscook.model.db.DbMealIngredient

@Dao
abstract class DbMealIngredientDao: BaseDao<DbMealIngredient>() {

    @Query("select * from dbmealingredient where mealId = :mealId")
    abstract fun getDbMealIngredientsByMealId(mealId: Long): List<DbMealIngredient>

    @Query("delete from dbmealIngredient where mealId = :mealId")
    abstract fun deleteDbMealIngredientsByMealId(mealId: Long)

    @Transaction
    open fun updateDbMealIngredients(mealId: Long, dbMealIngredients: List<DbMealIngredient>){
        deleteDbMealIngredientsByMealId(mealId)
        insert(dbMealIngredients)
    }
}