package com.shengj.stockapp.di

import com.shengj.stockapp.data.MockStockService
import com.shengj.stockapp.data.StockRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    
    @Provides
    @Singleton
    fun provideMockStockService(): MockStockService {
        return MockStockService()
    }
    
    @Provides
    @Singleton
    fun provideStockRepository(
        mockStockService: MockStockService
    ): StockRepository {
        return StockRepository(mockStockService)
    }
} 