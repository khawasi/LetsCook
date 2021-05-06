package com.akm.letscook.model.db

import com.akm.letscook.model.domain.Meal
import com.akm.letscook.model.domain.MealIngredient
import com.akm.letscook.util.mapToMealsFromDb
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class DbMealTest {

    private val dbMeal = DbMeal(
        123534,
        "Tacos",
        "https://asdasdasd.xcv/asdasd.jpg",
        "Mexican",
        "LOREM IPSUM somthing something",
        "19930404"
    )

    private val dbMeal2 = DbMeal(
        123590,
        "Tortilla",
        "https://asdasdasd.xcv/vbnvbn.jpg",
        "Mexican",
        "LOREM IPSUM NOthing Nothing",
        "19930404"
    )

    private val dbMeals = listOf(dbMeal, dbMeal2)

    private val dbMealIngredient1 = DbMealIngredient(
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

    private val dbMealIngredients = listOf(dbMealIngredient1, dbMealIngredient2)

    private val expectedMealIngredient1 = MealIngredient(
        "Sugar",
        "2 tb Spoon"
    )

    private val expectedMealIngredient2 = MealIngredient(
        "Salt",
        "4 tb Spoon"
    )

    private val expectedMealIngredients = listOf(expectedMealIngredient1, expectedMealIngredient2)

    private val expectedMealNoIngredients = Meal(
        123534,
        "Tacos",
        "https://asdasdasd.xcv/asdasd.jpg",
        "Mexican",
        "LOREM IPSUM somthing something",
        "19930404",
    )

    private val expectedMealNoIngredients2 = Meal(
        123590,
        "Tortilla",
        "https://asdasdasd.xcv/vbnvbn.jpg",
        "Mexican",
        "LOREM IPSUM NOthing Nothing",
        "19930404"
    )

    private val expectedMealsNoIngredient = listOf(expectedMealNoIngredients, expectedMealNoIngredients2)

    private val expectedMealWithIngredients = Meal(
        123534,
        "Tacos",
        "https://asdasdasd.xcv/asdasd.jpg",
        "Mexican",
        "LOREM IPSUM somthing something",
        "19930404",
        expectedMealIngredients
    )

    @Test
    fun testMapToMeal() {
        val meal = dbMeal.mapToMeal()
        assertThat(meal).isInstanceOf(Meal::class.java)
        assertThat(meal.id).isEqualTo(expectedMealNoIngredients.id)
        assertThat(meal.name).isEqualTo(expectedMealNoIngredients.name)
        assertThat(meal.thumbnailUrl).isEqualTo(expectedMealNoIngredients.thumbnailUrl)
        assertThat(meal.category).isEqualTo(expectedMealNoIngredients.category)
        assertThat(meal.instructions).isEqualTo(expectedMealNoIngredients.instructions)
        assertThat(meal.lastAccessed).isEqualTo(expectedMealNoIngredients.lastAccessed)
        assertThat(meal.ingredients).isEmpty()
    }

    @Test
    fun testMapToMealWithIngredients() {
        val meal = dbMeal.mapToMeal(dbMealIngredients)
        assertThat(meal).isInstanceOf(Meal::class.java)
        assertThat(meal.id).isEqualTo(expectedMealWithIngredients.id)
        assertThat(meal.name).isEqualTo(expectedMealWithIngredients.name)
        assertThat(meal.thumbnailUrl).isEqualTo(expectedMealWithIngredients.thumbnailUrl)
        assertThat(meal.category).isEqualTo(expectedMealWithIngredients.category)
        assertThat(meal.instructions).isEqualTo(expectedMealWithIngredients.instructions)
        assertThat(meal.lastAccessed).isEqualTo(expectedMealWithIngredients.lastAccessed)
        assertThat(meal.ingredients).isNotEmpty()
        assertThat(meal.ingredients).isEqualTo(expectedMealIngredients)
    }

    @Test
    fun testMapToMealsNoIngredient(){
        val meals = dbMeals.mapToMealsFromDb()
        assertThat(meals).isNotEmpty()
        assertThat(meals).isEqualTo(expectedMealsNoIngredient)
    }
}