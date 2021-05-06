package com.akm.letscook.model.domain

data class Meal(
        val id: Long = -1,
        val name: String = "",
        val thumbnailUrl: String = "",
        val category: String = "",
        val instructions: String = "",
        val lastAccessed: String = "",
        val ingredients: List<MealIngredient> = emptyList()
)