package com.akm.letscook

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.akm.letscook.model.db.DbMeal
import com.akm.letscook.model.db.LetsCookDatabase
import com.akm.letscook.model.db.dao.DbMealDao
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import kotlin.jvm.Throws

@RunWith(AndroidJUnit4::class)
class DbMealDaoTest {
    private lateinit var dbMealDao: DbMealDao
    private lateinit var db: LetsCookDatabase

    @Before
    fun createDb(){
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context,
            LetsCookDatabase::class.java
        ).build()
        dbMealDao = db.dbMealDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb(){
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun writeAndReadInList(){
        val dbMeal = DbMeal(
            23423,
            "TestName",
            "URL",
            "cat",
            "asd"
        )
        dbMealDao.insertOrUpdate(dbMeal)
        val byId = dbMealDao.getDbMealById(23423)
        assertThat(byId[0]).isEqualTo(dbMeal)
        val byName = dbMealDao.searchDbMealsByName("TestName")
        assertThat(byName[0]).isEqualTo(dbMeal)
    }

    @Test
    @Throws(Exception::class)
    fun writeAndReadInListBlocking() = runBlocking {
        val dbMealMini = DbMeal(
            23423,
            "TestName",
            "URL",
        )
        val categoryName = "Mexican"
        dbMealDao.insertOrUpdate(dbMealMini)
        dbMealDao.insertOrUpdateFromCategory(listOf(dbMealMini), categoryName)
        val byCategoryName = dbMealDao.getAllMealsByCategory(categoryName).first()
        assertThat(byCategoryName[0].id).isEqualTo(dbMealMini.id)
        assertThat(byCategoryName[0].category).isNotEmpty()
        assertThat(byCategoryName[0].category).isEqualTo(categoryName)
        assertThat(byCategoryName[0].instructions).isEmpty()
    }
}