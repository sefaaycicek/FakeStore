package com.baris.fakestore.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import javax.inject.Qualifier

/**
 * Created on 26.02.2024.
 * @author saycicek
 */

@Module
@InstallIn(SingletonComponent::class)
object DispatchersModule {

    @IoDispatcher
    @Provides
    fun provideDispatchersIO() = Dispatchers.IO

}

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class IoDispatcher