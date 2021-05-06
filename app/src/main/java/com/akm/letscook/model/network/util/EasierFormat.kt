package com.akm.letscook.model.network.util

import com.akm.letscook.model.network.MealsFromNetwork
import com.akm.letscook.model.network.NetMeal
import com.akm.letscook.model.network.NetMealIngredient
import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonQualifier
import com.squareup.moshi.ToJson

@JsonQualifier
annotation class EasierFormat

class MealsFromNetworkAdapter {
    @EasierFormat
    @FromJson
    fun netMealsfromJson(mealsFromNetwork: MealsFromNetwork): List<NetMeal> {
        val networkMeals = mutableListOf<NetMeal>()
        var networkMealIngredients: MutableList<NetMealIngredient>

        for (mealFromNetwork in mealsFromNetwork.meals) {

            networkMealIngredients = mutableListOf()
            if (mealFromNetwork.strIngredient1?.isNotEmpty() == true && mealFromNetwork.strMeasure1?.isNotEmpty() == true)
                networkMealIngredients.add(NetMealIngredient(mealFromNetwork.strIngredient1, mealFromNetwork.strMeasure1))


            if (mealFromNetwork.strIngredient2?.isNotEmpty() == true && mealFromNetwork.strMeasure2?.isNotEmpty() == true)
                networkMealIngredients.add(NetMealIngredient(mealFromNetwork.strIngredient2, mealFromNetwork.strMeasure2))


            if (mealFromNetwork.strIngredient3?.isNotEmpty() == true && mealFromNetwork.strMeasure3?.isNotEmpty() == true)
                networkMealIngredients.add(NetMealIngredient(mealFromNetwork.strIngredient3, mealFromNetwork.strMeasure3))


            if (mealFromNetwork.strIngredient4?.isNotEmpty() == true && mealFromNetwork.strMeasure4?.isNotEmpty() == true)
                networkMealIngredients.add(NetMealIngredient(mealFromNetwork.strIngredient4, mealFromNetwork.strMeasure4))


            if (mealFromNetwork.strIngredient5?.isNotEmpty() == true && mealFromNetwork.strMeasure5?.isNotEmpty() == true)
                networkMealIngredients.add(NetMealIngredient(mealFromNetwork.strIngredient5, mealFromNetwork.strMeasure5))


            if (mealFromNetwork.strIngredient6?.isNotEmpty() == true && mealFromNetwork.strMeasure6?.isNotEmpty() == true)
                networkMealIngredients.add(NetMealIngredient(mealFromNetwork.strIngredient6, mealFromNetwork.strMeasure6))


            if (mealFromNetwork.strIngredient7?.isNotEmpty() == true && mealFromNetwork.strMeasure7?.isNotEmpty() == true)
                networkMealIngredients.add(NetMealIngredient(mealFromNetwork.strIngredient7, mealFromNetwork.strMeasure7))


            if (mealFromNetwork.strIngredient8?.isNotEmpty() == true && mealFromNetwork.strMeasure8?.isNotEmpty() == true)
                networkMealIngredients.add(NetMealIngredient(mealFromNetwork.strIngredient8, mealFromNetwork.strMeasure8))


            if (mealFromNetwork.strIngredient9?.isNotEmpty() == true && mealFromNetwork.strMeasure9?.isNotEmpty() == true)
                networkMealIngredients.add(NetMealIngredient(mealFromNetwork.strIngredient9, mealFromNetwork.strMeasure9))


            if (mealFromNetwork.strIngredient10?.isNotEmpty() == true && mealFromNetwork.strMeasure10?.isNotEmpty() == true)
                networkMealIngredients.add(NetMealIngredient(mealFromNetwork.strIngredient10, mealFromNetwork.strMeasure10))


            if (mealFromNetwork.strIngredient11?.isNotEmpty() == true && mealFromNetwork.strMeasure11?.isNotEmpty() == true)
                networkMealIngredients.add(NetMealIngredient(mealFromNetwork.strIngredient11, mealFromNetwork.strMeasure11))


            if (mealFromNetwork.strIngredient12?.isNotEmpty() == true && mealFromNetwork.strMeasure12?.isNotEmpty() == true)
                networkMealIngredients.add(NetMealIngredient(mealFromNetwork.strIngredient12, mealFromNetwork.strMeasure12))


            if (mealFromNetwork.strIngredient13?.isNotEmpty() == true && mealFromNetwork.strMeasure13?.isNotEmpty() == true)
                networkMealIngredients.add(NetMealIngredient(mealFromNetwork.strIngredient13, mealFromNetwork.strMeasure13))


            if (mealFromNetwork.strIngredient14?.isNotEmpty() == true && mealFromNetwork.strMeasure14?.isNotEmpty() == true)
                networkMealIngredients.add(NetMealIngredient(mealFromNetwork.strIngredient14, mealFromNetwork.strMeasure14))


            if (mealFromNetwork.strIngredient15?.isNotEmpty() == true && mealFromNetwork.strMeasure15?.isNotEmpty() == true)
                networkMealIngredients.add(NetMealIngredient(mealFromNetwork.strIngredient15, mealFromNetwork.strMeasure15))


            if (mealFromNetwork.strIngredient16?.isNotEmpty() == true && mealFromNetwork.strMeasure16?.isNotEmpty() == true)
                networkMealIngredients.add(NetMealIngredient(mealFromNetwork.strIngredient16, mealFromNetwork.strMeasure16))


            if (mealFromNetwork.strIngredient17?.isNotEmpty() == true && mealFromNetwork.strMeasure17?.isNotEmpty() == true)
                networkMealIngredients.add(NetMealIngredient(mealFromNetwork.strIngredient17, mealFromNetwork.strMeasure17))


            if (mealFromNetwork.strIngredient18?.isNotEmpty() == true && mealFromNetwork.strMeasure18?.isNotEmpty() == true)
                networkMealIngredients.add(NetMealIngredient(mealFromNetwork.strIngredient18, mealFromNetwork.strMeasure18))


            if (mealFromNetwork.strIngredient19?.isNotEmpty() == true && mealFromNetwork.strMeasure19?.isNotEmpty() == true)
                networkMealIngredients.add(NetMealIngredient(mealFromNetwork.strIngredient19, mealFromNetwork.strMeasure19))


            if (mealFromNetwork.strIngredient20?.isNotEmpty() == true && mealFromNetwork.strMeasure20?.isNotEmpty() == true)
                networkMealIngredients.add(NetMealIngredient(mealFromNetwork.strIngredient20, mealFromNetwork.strMeasure20))

            networkMeals.add(
                    NetMeal(
                            id = mealFromNetwork.idMeal,
                            name = mealFromNetwork.strMeal,
                            thumbnailUrl = mealFromNetwork.strMealThumb,
                            category = mealFromNetwork.strCategory ?: "",
                            instructions = mealFromNetwork.strInstructions ?: "",
                            ingredients = networkMealIngredients.toList()
                    )
            )
        }
        return networkMeals.toList()
    }

    @ToJson
    fun netMealsToJson(@EasierFormat netMeals: List<NetMeal>): MealsFromNetwork {
        throw UnsupportedOperationException()
    }

}