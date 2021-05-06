package com.akm.letscook.di.binder

import com.akm.letscook.model.db.IDbTransaction
import com.akm.letscook.model.db.RoomTransactionImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DbBindsModule {

    @Binds
    @Singleton
    abstract fun bindsRoomDbTransaction(impl: RoomTransactionImpl): IDbTransaction

}