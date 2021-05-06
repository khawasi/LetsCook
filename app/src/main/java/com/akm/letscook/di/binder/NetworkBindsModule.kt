package com.akm.letscook.di.binder

import com.akm.letscook.model.network.INetTransaction
import com.akm.letscook.model.network.RetrofitTransactionImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class NetworkBindsModule {

    @Binds
    @Singleton
    abstract fun bindsRetrofitTransaction(impl: RetrofitTransactionImpl) : INetTransaction

}