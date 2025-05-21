package com.shengj.stockapp.ui.theme

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed

/**
 * 一组自定义Modifier，用于提高布局代码的可重用性
 */
object Modifiers {
    
    /**
     * 创建无涟漪效果的点击修饰符
     */
    fun Modifier.noRippleClickable(onClick: () -> Unit): Modifier = composed {
        clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null,
            onClick = onClick
        )
    }
} 