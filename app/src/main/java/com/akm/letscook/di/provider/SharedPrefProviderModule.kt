package com.akm.letscook.di.provider

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.akm.letscook.repository.SharedPrefRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class SharedPrefProviderModule {

    @Provides
    @Singleton
    fun provideSharedPref(@ApplicationContext context: Context) : SharedPrefRepository {
//        val mainKey = MasterKey.Builder(context)
//            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
//            .build()
//
//        val sharedPrefFile: String = context.packageName
//
//        return EncryptedSharedPreferences.create(
//            context,
//            sharedPrefFile,
//            mainKey,
//            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
//            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
//        )
        return SharedPrefRepository(context)
    }

}