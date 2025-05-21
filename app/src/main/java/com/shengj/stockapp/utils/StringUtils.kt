package com.shengj.stockapp.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.shengj.stockapp.R
import com.shengj.stockapp.StockApplication

/**
 * 字符串工具类
 */
object StringUtils {
    
    // 提取格式常量，避免直接使用stringResource
    private const val FORMAT_EMPTY = "--"
    
    /**
     * 格式化大数字（非Compose上下文中使用）
     */
    fun formatNumber(number: Long): String {
        return when {
            number >= 100000000 -> String.format(
                StockApplication.instance.getString(R.string.format_billion), 
                number / 100000000.0
            )
            number >= 10000 -> String.format(
                StockApplication.instance.getString(R.string.format_million), 
                number / 10000.0
            )
            else -> number.toString()
        }
    }
    
    /**
     * 格式化大数字（非Compose上下文中使用）
     */
    fun formatNumber(number: Double): String {
        return when {
            number >= 100000000 -> String.format(
                StockApplication.instance.getString(R.string.format_billion), 
                number / 100000000.0
            )
            number >= 10000 -> String.format(
                StockApplication.instance.getString(R.string.format_million), 
                number / 10000.0
            )
            else -> number.toString()
        }
    }
    
    /**
     * 格式化百分比（非Compose上下文中使用）
     */
    fun formatPercent(value: Double): String {
        val format = if (value >= 0) {
            StockApplication.instance.getString(R.string.format_positive_percent)
        } else {
            StockApplication.instance.getString(R.string.format_negative_percent)
        }
        return String.format(format, value.toString())
    }
    
    /**
     * 获取空值占位符（非Compose上下文中使用）
     */
    fun getEmptyPlaceholder(): String {
        return StockApplication.instance.getString(R.string.format_empty)
    }
}

/**
 * 格式化大数字（Compose上下文中使用）
 */
@Composable
fun formatNumberWithResource(number: Long): String {
    return when {
        number >= 100000000 -> String.format(
            stringResource(R.string.format_billion), 
            number / 100000000.0
        )
        number >= 10000 -> String.format(
            stringResource(R.string.format_million), 
            number / 10000.0
        )
        else -> number.toString()
    }
}

/**
 * 格式化大数字（Compose上下文中使用）
 */
@Composable
fun formatNumberWithResource(number: Double): String {
    return when {
        number >= 100000000 -> String.format(
            stringResource(R.string.format_billion), 
            number / 100000000.0
        )
        number >= 10000 -> String.format(
            stringResource(R.string.format_million), 
            number / 10000.0
        )
        else -> number.toString()
    }
}

/**
 * 格式化百分比（Compose上下文中使用）
 */
@Composable
fun formatPercentWithResource(value: Double): String {
    val format = if (value >= 0) {
        stringResource(R.string.format_positive_percent)
    } else {
        stringResource(R.string.format_negative_percent)
    }
    return String.format(format, value.toString())
} 