package com.akm.letscook.model.network

import com.akm.letscook.model.db.DbCategory
import com.akm.letscook.model.domain.Category
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NetCategories(
        val categories: List<NetCategory>
) {
    fun mapToDbCategories(): List<DbCategory> =
            categories.map { netCategory ->
                netCategory.mapToDbCategory()
            }

}

@JsonClass(generateAdapter = true)
data class NetCategory(
        val idCategory: Long,
        val strCategory: String,
        val strCategoryThumb: String,
        val strCategoryDescription: String
){
    fun mapToDbCategory(): DbCategory =
            DbCategory(
                    idCategory,
                    strCategory,
                    strCategoryThumb
            )
}