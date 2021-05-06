package com.akm.letscook.model.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.akm.letscook.model.db.dao.*

@Database(
        entities = [
            DbMeal::class,
            DbMealIngredient::class,
            DbFavoriteMeal::class,
            DbCategory::class
        ],
        version = 1,
        exportSchema = true
)
abstract class LetsCookDatabase : RoomDatabase(){
    abstract fun dbMealDao(): DbMealDao
    abstract fun dbCategoryDao(): DbCategoryDao
    abstract fun dbMealIngredientDao(): DbMealIngredientDao
    abstract fun dbFavoriteMealDao(): DbFavoriteMealDao
    abstract fun dbMealWithIngredientsDao(): DbMealWithIngredientsDao
}