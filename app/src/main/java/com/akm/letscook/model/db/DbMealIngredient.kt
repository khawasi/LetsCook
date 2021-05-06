package com.akm.letscook.model.db

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.akm.letscook.model.domain.MealIngredient

@Entity(
        foreignKeys = [
            ForeignKey(
                    entity = DbMeal::class,
                    parentColumns = ["id"],
                    childColumns = ["mealId"],
                    onDelete = ForeignKey.CASCADE
            )],
        indices = [Index(
                value = ["mealId"]
        )]
)
data class DbMealIngredient(
        @PrimaryKey(autoGenerate = true) val id: Long = 0,
        val mealId: Long,
        val name: String,
        val measurement: String
){
    fun mapToMealIngredient(): MealIngredient =
            MealIngredient(
                    name,
                    measurement
            )
}