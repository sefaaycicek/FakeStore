package com.baris.fakestore.di

import android.content.Context
import androidx.room.Room
import com.baris.fakestore.BuildConfig
import com.baris.fakestore.data.source.api.FakeStoreApi
import com.baris.fakestore.data.source.local.FakeStoreDatabase
import com.baris.fakestore.data.source.local.dao.BasketProductsDao
import com.baris.fakestore.data.source.local.dao.FavoriteProductsDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * Created on 26.02.2024.
 * @author saycicek
 */

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideFakeStoreApi(): FakeStoreApi {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(FakeStoreApi::class.java)
    }

    @Provides
    @Singleton
    fun provideFakeStoreDatabase(
        @ApplicationContext context: Context
    ): FakeStoreDatabase {
        return Room.databaseBuilder(
            context = context,
            klass = FakeStoreDatabase::class.java,
            name = FakeStoreDatabase.NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideBasketProductsDao(
        database: FakeStoreDatabase
    ): BasketProductsDao {
        return database.getBasketProductsDao()
    }

    @Provides
    @Singleton
    fun provideFavoriteProductsDao(
        database: FakeStoreDatabase
    ): FavoriteProductsDao {
        return database.getFavoriteProductsDao()
    }

}