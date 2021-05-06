package com.akm.letscook.di.binder

import com.akm.letscook.repository.IRepositoryTransaction
import com.akm.letscook.repository.AppRepositoryTransactionImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryBindsModule {

    @Binds
//    @Singleton
    abstract fun bindsLetsCookRepositoryTransaction(impl: AppRepositoryTransactionImpl): IRepositoryTransaction

}