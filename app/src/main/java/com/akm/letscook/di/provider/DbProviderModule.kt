package com.akm.letscook.di.provider

import android.content.Context
import androidx.room.Room
import com.akm.letscook.model.db.LetsCookDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DbProviderModule {

    @Provides
    @Singleton
    fun provideRoomDb(@ApplicationContext context: Context) =
            Room.databaseBuilder(
                    context,
                    LetsCookDatabase::class.java,
                    "LetsCook"
            ).build()

    @Provides
    @Singleton
    fun provideMealDao(db: LetsCookDatabase) = db.dbMealDao()

    @Provides
    @Singleton
    fun provideMealIngredientDao(db: LetsCookDatabase) = db.dbMealIngredientDao()

    @Provides
    @Singleton
    fun provideMCategoryDao(db: LetsCookDatabase) = db.dbCategoryDao()

    @Provides
    @Singleton
    fun provideFavoriteMealDao(db: LetsCookDatabase) = db.dbFavoriteMealDao()

    @Provides
    @Singleton
    fun provideMealWithIngredientsDao(db: LetsCookDatabase) = db.dbMealWithIngredientsDao()

}