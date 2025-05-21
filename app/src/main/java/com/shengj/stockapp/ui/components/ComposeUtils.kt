package com.shengj.stockapp.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember

/**
 * 创建一个派生状态，当依赖项发生变化时才会重新计算
 */
@Composable
fun <T, R> rememberDerivedState(
    value: T,
    calculation: (T) -> R
): State<R> {
    val derivedValue = remember(value) {
        derivedStateOf { calculation(value) }
    }
    return derivedValue
}

/**
 * 创建一个派生状态，当多个依赖项发生变化时才会重新计算
 */
@Composable
fun <T1, T2, R> rememberDerivedState(
    value1: T1,
    value2: T2,
    calculation: (T1, T2) -> R
): State<R> {
    val derivedValue = remember(value1, value2) {
        derivedStateOf { calculation(value1, value2) }
    }
    return derivedValue
} 