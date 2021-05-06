package com.akm.letscook.di.provider

import com.akm.letscook.BuildConfig
import com.akm.letscook.di.qualifier.ApiKey
import com.akm.letscook.di.qualifier.BaseUrl
import com.akm.letscook.model.network.IApiService
import com.akm.letscook.model.network.util.MealsFromNetworkAdapter
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkProviderModule {

    @Provides
    @BaseUrl
    fun provideBaseUrl() = BuildConfig.BASE_URL

    @Provides
    @ApiKey
    fun providesApiKey() = BuildConfig.API_KEY

    @Provides
    fun provideOkHttpClient() =
            if (BuildConfig.DEBUG) {
                val loggingInterceptor = HttpLoggingInterceptor()
                loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
                OkHttpClient.Builder().addInterceptor(loggingInterceptor).build()
            } else {
                OkHttpClient.Builder().build()
            }

    @Provides
    fun provideMoshi() : Moshi =
            Moshi.Builder()
                    .add(MealsFromNetworkAdapter())
                    .build()

    @Provides
    @Singleton
    fun provideRetrofit(@BaseUrl baseUrl: String, moshi: Moshi, okHttpClient: OkHttpClient): Retrofit =
            Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(MoshiConverterFactory.create(moshi))
                    .client(okHttpClient)
                    .build()

    @Provides
    @Singleton
    fun provideService(retrofit: Retrofit): IApiService = retrofit.create(IApiService::class.java)

}