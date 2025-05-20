package com.shengj.stockapp.data

import com.shengj.stockapp.model.Stock
import com.shengj.stockapp.model.TabType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StockRepository @Inject constructor(
    private val mockStockService: MockStockService
) {
    fun getStocks(tabType: TabType): Flow<List<Stock>> = flow {
        emit(mockStockService.getStocks(tabType))
    }.flowOn(Dispatchers.IO)
} 