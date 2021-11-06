package com.example.assignment.DI

import com.example.assignment.Data.Repository.TestSeriesRepository
import com.example.assignment.Data.Repository.TestSeriesRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideTestSeriesRepository(): TestSeriesRepository {
        return TestSeriesRepositoryImpl()
    }
}