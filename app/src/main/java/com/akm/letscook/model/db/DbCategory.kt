package com.akm.letscook.model.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.akm.letscook.model.domain.Category

@Entity
data class DbCategory(
        @PrimaryKey val id: Long,
        val name: String,
        val thumbnailUrl: String,
        val lastAccessed: String = ""
){

    fun mapToCategory(): Category =
        Category(
                id,
                name,
                thumbnailUrl,
                lastAccessed
        )

}