package com.akm.letscook.model.db.dao

import androidx.room.Dao
import androidx.room.Query
import com.akm.letscook.model.db.DbFavoriteMeal

@Dao
abstract class DbFavoriteMealDao: BaseDao<DbFavoriteMeal>() {

    @Query("Select exists(select * from dbfavoritemeal where mealid = :mealId)")
    abstract fun isMealFavorite(mealId: Long) : Boolean

}