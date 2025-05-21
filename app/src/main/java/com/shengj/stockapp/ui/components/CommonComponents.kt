package com.shengj.stockapp.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.shengj.stockapp.ui.theme.Orange

/**
 * 通用的加载状态组件
 */
@Composable
fun LoadingState(
    modifier: Modifier = Modifier,
    color: Color = Orange
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = color)
    }
}

/**
 * 通用的错误状态组件
 */
@Composable
fun ErrorState(
    message: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Red
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = message,
            color = color
        )
    }
} 