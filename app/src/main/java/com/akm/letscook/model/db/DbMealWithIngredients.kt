package com.akm.letscook.model.db

import androidx.room.Embedded
import androidx.room.Relation
import com.akm.letscook.model.domain.Meal

class DbMealWithIngredients{

    @Embedded
    var dbMeal: DbMeal? = null

    @Relation(
            parentColumn = "id",
            entityColumn = "mealId"
    )
    var dbMealIngredients: List<DbMealIngredient>? = ArrayList()

    fun mapToMealWithIngredients(): Meal = dbMeal!!.mapToMeal(dbMealIngredients!!)

}