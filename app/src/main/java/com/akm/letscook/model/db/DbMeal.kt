package com.akm.letscook.model.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.akm.letscook.model.domain.Meal
import com.akm.letscook.util.mapToMealIngredients

@Entity
data class DbMeal(
        @PrimaryKey val id: Long,
        val name: String,
        val thumbnailUrl: String,
        val category: String = "",
        val instructions: String = "",
        val lastAccessed: String = ""
){
        fun mapToMeal(): Meal =
                Meal(
                        id,
                        name,
                        thumbnailUrl,
                        category,
                        instructions,
                        lastAccessed
                )

        fun mapToMeal(dbMealIngredients: List<DbMealIngredient>): Meal {
                return Meal(
                        id,
                        name,
                        thumbnailUrl,
                        category,
                        instructions,
                        lastAccessed,
                        dbMealIngredients.mapToMealIngredients()
                )
        }
}