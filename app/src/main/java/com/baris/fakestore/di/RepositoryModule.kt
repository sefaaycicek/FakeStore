package com.baris.fakestore.di

import com.baris.fakestore.data.repository.FakeStoreRepositoryImpl
import com.baris.fakestore.domain.repository.FakeStoreRepository
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
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindFakeStoreRepository(fakeStoreRepositoryImpl: FakeStoreRepositoryImpl): FakeStoreRepository

}