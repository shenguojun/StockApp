package com.shengj.stockapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/**
 * 通用的全屏占位符组件，用于显示简单的文字内容
 */
@Composable
fun CommonPlaceholder(
    textResId: Int,
    backgroundColor: Color = Color.White,
    textColor: Color = Color(0xFFFF5C00),
    fontSize: Int = 24
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(id = textResId),
            fontSize = fontSize.sp,
            fontWeight = FontWeight.Bold,
            color = textColor
        )
    }
} 