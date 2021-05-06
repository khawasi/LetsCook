package com.akm.letscook.model.db

import com.akm.letscook.model.domain.MealIngredient
import com.akm.letscook.util.mapToMealIngredients
import com.google.common.truth.Truth.assertThat
import org.junit.Test


class DbMealIngredientTest {

    private val dbMealIngredient = DbMealIngredient(
        123123,
        123534,
        "Sugar",
        "2 tb Spoon"
    )

    private val dbMealIngredient2 = DbMealIngredient(
        1245456,
        123534,
        "Salt",
        "4 tb Spoon"
    )

    private val dbMealIngredients = listOf(dbMealIngredient, dbMealIngredient2)

    private val expectedMealIngredient = MealIngredient(
        "Sugar",
        "2 tb Spoon"
    )

    private val expectedMealIngredient2 = MealIngredient(
        "Salt",
        "4 tb Spoon"
    )

    private val expectedMealIngredients = listOf(expectedMealIngredient, expectedMealIngredient2)


    @Test
    fun testMapToMealIngredient() {
        val mealIngredient = dbMealIngredient.mapToMealIngredient()
        assertThat(mealIngredient).isInstanceOf(MealIngredient::class.java)
        assertThat(mealIngredient.name).isEqualTo(expectedMealIngredient.name)
        assertThat(mealIngredient.measurement).isEqualTo(expectedMealIngredient.measurement)
        assertThat(mealIngredient).isEqualTo(expectedMealIngredient)
    }

    @Test
    fun testMapToMealIngredients(){
        val mealIngredients = dbMealIngredients.mapToMealIngredients()
        assertThat(mealIngredients).isNotEmpty()
        assertThat(mealIngredients).isEqualTo(expectedMealIngredients)
    }
}