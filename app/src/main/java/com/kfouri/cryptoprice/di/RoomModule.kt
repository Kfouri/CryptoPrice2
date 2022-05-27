package com.kfouri.cryptoprice.di

import android.content.Context
import androidx.room.Room
import com.kfouri.cryptoprice.BuildConfig
import com.kfouri.cryptoprice.data.db.AppDatabase
import com.kfouri.cryptoprice.data.db.dao.CurrencyDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    @Singleton
    @Provides
    fun provideDataBase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            BuildConfig.APPLICATION_ID
        ).fallbackToDestructiveMigration().build()
    }

    @Singleton
    @Provides
    fun provideCurrencyDao(appDatabase: AppDatabase): CurrencyDao {
        return appDatabase.currencyDao()
    }
}