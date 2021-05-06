package com.akm.letscook.model.db

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
        foreignKeys = [
            ForeignKey(
                    entity = DbMeal::class,
                    parentColumns = ["id"],
                    childColumns = ["mealId"],
                    onDelete = ForeignKey.CASCADE
            )],
        indices = [Index(
                value = ["mealId"],
                unique = true
        )]
)
data class DbFavoriteMeal(
        @PrimaryKey
        val mealId: Long
)