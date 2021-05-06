package com.akm.letscook.model.db

import com.akm.letscook.model.domain.Category
import com.akm.letscook.util.mapToCategories
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class DbCategoryTest{
    private val dbCategory = DbCategory(
        89,
        "Mexican",
        "https://asdfasdf.vxc/oioioa",
        "19930404"
    )

    private val dbCategory2 = DbCategory(
        90,
        "Vegetarian",
        "https://asdsdf.vxc/vbmnxc",
        "19930402"
    )
    private val dbCategories = listOf(dbCategory, dbCategory2)

    private val expectedCategory = Category(
        89,
        "Mexican",
        "https://asdfasdf.vxc/oioioa",
        "19930404"
    )

    private val expectedCategory2 = Category(
        90,
        "Vegetarian",
        "https://asdsdf.vxc/vbmnxc",
        "19930402"
    )

    private val expectedCategories = listOf(expectedCategory, expectedCategory2)

    @Test
    fun testMapToCategory(){
        val category = dbCategory.mapToCategory()
        assertThat(category).isInstanceOf(Category::class.java)
        assertThat(category.id).isEqualTo(expectedCategory.id)
        assertThat(category.name).isEqualTo(expectedCategory.name)
        assertThat(category.thumbnailUrl).isEqualTo(expectedCategory.thumbnailUrl)
        assertThat(category.lastAccessed).isEqualTo(expectedCategory.lastAccessed)
        assertThat(category).isEqualTo(expectedCategory)
    }

    @Test
    fun testMapToCategories(){
        val categories =dbCategories.mapToCategories()
        assertThat(categories).isNotEmpty()
        assertThat(categories).isEqualTo(expectedCategories)
    }
}