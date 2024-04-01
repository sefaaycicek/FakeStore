package com.baris.fakestore.di

import com.baris.fakestore.data.dataSource.FakeStoreApiDataSource
import com.baris.fakestore.data.dataSource.FakeStoreApiDataSourceImpl
import com.baris.fakestore.data.dataSource.FakeStoreDatabaseDataSource
import com.baris.fakestore.data.dataSource.FakeStoreDatabaseDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Created on 26.02.2024.
 * @author saycicek
 */

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Binds
    @Singleton
    abstract fun bindFakeStoreApiDataSource(fakeStoreDataSourceImpl: FakeStoreApiDataSourceImpl): FakeStoreApiDataSource

    @Binds
    @Singleton
    abstract fun bindFakeStoreDatabaseDataSource(fakeStoreDatabaseDataSourceImpl: FakeStoreDatabaseDataSourceImpl): FakeStoreDatabaseDataSource

}