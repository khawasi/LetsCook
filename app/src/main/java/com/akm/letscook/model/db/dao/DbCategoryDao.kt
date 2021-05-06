package com.akm.letscook.model.db.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.akm.letscook.model.db.DbCategory
import kotlinx.coroutines.flow.Flow

@Dao
abstract class DbCategoryDao: BaseDao<DbCategory>() {

    @Query("select * from DbCategory")
    abstract fun getAllCategories(): Flow<List<DbCategory>>

    @Query("update dbcategory set name = :name, thumbnailUrl = :thumbnailUrl where id = :id")
    abstract fun miniUpdate(id: Long, name: String, thumbnailUrl: String)

    @Query("update dbcategory set lastAccessed = :lastAccessed where name = :name")
    abstract fun updateLastAccessed(lastAccessed: String, name: String)

    @Transaction
    open fun insertOrMiniUpdate(objList: List<DbCategory>) {
        val insertResult = insert(objList)

        for (i in insertResult.indices) {
            if (insertResult[i] == -1L) {
                miniUpdate(objList[i].id, objList[i].name, objList[i].thumbnailUrl)
            }
        }

    }

}