package com.shengj.stockapp.data

import com.shengj.stockapp.model.Stock
import com.shengj.stockapp.model.StockType
import com.shengj.stockapp.model.TabType
import kotlinx.coroutines.delay
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random

@Singleton
class MockStockService @Inject constructor() {
    
    suspend fun getStocks(tabType: TabType): List<Stock> {
        // 模拟网络延迟
        delay(500)
        
        return when (tabType) {
            TabType.ALL -> allStocks
            TabType.POSITION -> positionStocks
            TabType.A -> aStocks
            TabType.HK -> hkStocks
            TabType.US -> usStocks
        }
    }
    
    private val allStocks = listOf(
        Stock(
            code = "000001",
            name = "上证指数",
            price = 3380.48,
            change = 12.90,
            changePercent = 0.38,
            volume = 150000000,
            currentVolume = 5000000,
            amount = 2000000000.0,
            volumeRatio = 1.2,
            high = 3390.25,
            low = 3370.15,
            amplitude = 0.6,
            turnoverRate = 1.5
        ),
        Stock(
            code = "300059",
            name = "东方财富",
            price = 21.52,
            change = 0.02,
            changePercent = 0.09,
            volume = 120000000,
            currentVolume = 4000000,
            amount = 1500000000.0,
            volumeRatio = 1.1,
            high = 21.80,
            low = 21.30,
            amplitude = 2.3,
            turnoverRate = 2.1
        ),
        Stock(
            code = "600519",
            name = "贵州茅台",
            price = 1586.00,
            change = 7.02,
            changePercent = 0.44,
            volume = 3000000,
            currentVolume = 100000,
            amount = 4500000000.0,
            volumeRatio = 0.9,
            high = 1590.00,
            low = 1570.00,
            amplitude = 1.2,
            turnoverRate = 0.5
        ),
        Stock(
            code = "00700",
            name = "腾讯控股",
            price = 517.00,
            change = 3.00,
            changePercent = 0.58,
            volume = 8000000,
            currentVolume = 300000,
            amount = 4000000000.0,
            volumeRatio = 1.0,
            high = 520.00,
            low = 510.00,
            amplitude = 1.9,
            turnoverRate = 0.8,
            type = StockType.HK
        ),
        Stock(
            code = "AAPL",
            name = "苹果",
            price = 208.78,
            change = -2.48,
            changePercent = -1.17,
            volume = 60000000,
            currentVolume = 2000000,
            amount = 10000000000.0,
            volumeRatio = 1.1,
            high = 212.00,
            low = 207.50,
            amplitude = 2.1,
            turnoverRate = 0.4,
            type = StockType.US
        ),
        Stock(
            code = "589060",
            name = "科创综指ETF东财",
            price = 0.979,
            change = 0.003,
            changePercent = 0.31,
            volume = 50000000,
            currentVolume = 1500000,
            amount = 50000000.0,
            volumeRatio = 1.3,
            high = 0.985,
            low = 0.975,
            amplitude = 1.0,
            turnoverRate = 1.2
        ),
        Stock(
            code = "159380",
            name = "A500ETF东财",
            price = 1.038,
            change = 0.005,
            changePercent = 0.48,
            volume = 45000000,
            currentVolume = 1400000,
            amount = 45000000.0,
            volumeRatio = 1.2,
            high = 1.042,
            low = 1.030,
            amplitude = 1.1,
            turnoverRate = 1.3
        ),
        Stock(
            code = "159637",
            name = "新能源车龙头ETF",
            price = 0.620,
            change = 0.005,
            changePercent = 0.81,
            volume = 40000000,
            currentVolume = 1300000,
            amount = 40000000.0,
            volumeRatio = 1.4,
            high = 0.625,
            low = 0.615,
            amplitude = 1.6,
            turnoverRate = 1.5
        ),
        Stock(
            code = "159622",
            name = "创新药ETF沪港深",
            price = 0.895,
            change = 0.022,
            changePercent = 2.52,
            volume = 35000000,
            currentVolume = 1200000,
            amount = 35000000.0,
            volumeRatio = 1.5,
            high = 0.900,
            low = 0.870,
            amplitude = 3.3,
            turnoverRate = 1.7
        ),
        Stock(
            code = "159599",
            name = "芯片ETF基金",
            price = 1.459,
            change = 0.006,
            changePercent = 0.41,
            volume = 30000000,
            currentVolume = 1100000,
            amount = 30000000.0,
            volumeRatio = 1.1,
            high = 1.465,
            low = 1.450,
            amplitude = 1.0,
            turnoverRate = 1.4
        )
    )
    
    private val positionStocks = listOf(
        allStocks[1],
        allStocks[2],
        allStocks[6],
        allStocks[8]
    )
    
    private val aStocks = allStocks.filter { it.type == StockType.A }
    private val hkStocks = allStocks.filter { it.type == StockType.HK }
    private val usStocks = allStocks.filter { it.type == StockType.US }
} 