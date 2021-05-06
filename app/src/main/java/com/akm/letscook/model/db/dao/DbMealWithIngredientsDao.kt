package com.akm.letscook.model.db.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.akm.letscook.model.db.DbMealWithIngredients

@Dao
abstract class DbMealWithIngredientsDao {

    @Transaction
    @Query("select * from DbMeal where id = :mealId")
    abstract fun getMealWithIngredients(mealId: Long): DbMealWithIngredients

}