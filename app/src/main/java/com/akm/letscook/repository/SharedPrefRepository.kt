package com.akm.letscook.repository

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

class SharedPrefRepository (
        context: Context
) {
    private val mainKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

    private val sharedPrefFile: String = context.packageName

    private val sharedPref =
        EncryptedSharedPreferences.create(
            context,
            sharedPrefFile,
            mainKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

    private val editor = sharedPref.edit()

    fun getString(key: String): String = sharedPref.getString(key, "").toString()

    fun getLong(key: String): Long = sharedPref.getLong(key, -1)

    fun getBoolean(key: String): Boolean = sharedPref.getBoolean(key, false)

    fun putString(key: String, value: String){
        editor.putString(key, value).apply()
    }

    fun putLong(key: String, value: Long){
        editor.putLong(key, value).apply()
    }

    fun putBoolean(key: String, value: Boolean){
        editor.putBoolean(key, value).apply()
    }

}