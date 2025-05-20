package com.shengj.stockapp.model

data class Stock(
    val code: String,
    val name: String,
    val price: Double,
    val change: Double,
    val changePercent: Double,
    val volume: Long,
    val currentVolume: Long,
    val amount: Double,
    val volumeRatio: Double,
    val high: Double,
    val low: Double,
    val amplitude: Double,
    val turnoverRate: Double,
    val type: StockType = StockType.A
)

enum class StockType {
    A, // 沪深京
    HK, // 港股
    US // 美股
}

enum class TabType {
    ALL, // 全部
    POSITION, // 持仓
    A, // 沪深京
    HK, // 港股
    US // 美股
}

enum class SortColumn {
    LATEST, // 最新
    CHANGE_PERCENT, // 涨幅
    CHANGE, // 涨跌
    MOMENTUM, // 涨速
    VOLUME, // 总量
    CURRENT_VOLUME, // 现量
    AMOUNT, // 金额
    VOLUME_RATIO, // 量比
    HIGH, // 最高
    LOW, // 最低
    AMPLITUDE, // 振幅
    TURNOVER_RATE // 换手
} 