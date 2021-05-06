package com.akm.letscook.model.network

import com.akm.letscook.model.network.util.EasierFormat
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface IApiService {

    @GET("{apiKey}/lookup.php")
    @EasierFormat
    suspend fun getMealById(
            @Path("apiKey") apiKey: String,
            @Query("i") mealId: Long
    ) : List<NetMeal>

    @GET("{apiKey}/random.php")
    @EasierFormat
    suspend fun getRecommendedMeal(@Path("apiKey") apiKey: String) : List<NetMeal>

    @GET("{apiKey}/filter.php")
    @EasierFormat
    suspend fun getMealsByCategory(
            @Path("apiKey") apiKey: String,
            @Query("c") categoryName: String
    ) : List<NetMeal>

    @GET("{apiKey}/search.php")
    @EasierFormat
    suspend fun searchMealsByName(
            @Path("apiKey") apiKey: String,
            @Query("s") queryName: String
    ): List<NetMeal>

    @GET("{apiKey}/categories.php")
    suspend fun getCategories(@Path(value = "apiKey") apiKey: String) : NetCategories

}