package com.akm.letscook.util

import com.akm.letscook.model.db.DbCategory
import com.akm.letscook.model.db.DbMeal
import com.akm.letscook.model.db.DbMealIngredient
import com.akm.letscook.model.domain.Category
import com.akm.letscook.model.domain.Meal
import com.akm.letscook.model.domain.MealIngredient
import com.akm.letscook.model.network.NetMeal
import com.akm.letscook.model.network.NetMealIngredient

fun List<NetMeal>.mapToDbMeals(lastAccessed: String = "", category: String = "") : List<DbMeal> =
        map { netMeal ->
            netMeal.mapToDbMeal(lastAccessed, category)
        }

fun List<NetMeal>.mapToMealsFromNet(): List<Meal> =
    map{ netMeal ->
        netMeal.mapToMeal()
    }

fun List<NetMealIngredient>.mapToDbMealIngredients(mealId: Long): List<DbMealIngredient> =
        map{ netMealIngredient ->
            netMealIngredient.mapToDbMealIngredient(mealId)
        }

fun List<DbMealIngredient>.mapToMealIngredients(): List<MealIngredient> =
        map { dbMealIngredient ->
            dbMealIngredient.mapToMealIngredient()
        }

fun List<DbCategory>.mapToCategories(): List<Category> =
        map { dbCategory ->
            dbCategory.mapToCategory()
        }

fun List<DbMeal>.mapToMealsFromDb(): List<Meal> =
        map { dbMeal ->
            dbMeal.mapToMeal()
        }