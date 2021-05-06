package com.akm.letscook.model.network

import com.akm.letscook.model.db.DbMeal
import com.akm.letscook.model.db.DbMealIngredient
import com.akm.letscook.model.domain.Meal

data class NetMeal(
    val id: Long,
    val name: String,
    val thumbnailUrl: String,
    val category: String,
    val instructions: String = "",
    val ingredients: List<NetMealIngredient> = emptyList()
) {
    fun mapToDbMeal(lastAccessed: String, categoryName: String = ""): DbMeal =
        DbMeal(
            id,
            name,
            thumbnailUrl,
            category = if (categoryName.isEmpty()) category else categoryName,
            instructions,
            lastAccessed
        )

    fun mapToMeal(): Meal =
        Meal(
            id,
            name,
            thumbnailUrl
        )
}

data class NetMealIngredient(
    val name: String,
    val measurement: String
) {
    fun mapToDbMealIngredient(mealId: Long): DbMealIngredient =
        DbMealIngredient(
            0,
            mealId,
            name,
            measurement
        )

}