package com.shengj.stockapp.ui.stocks

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController

/**
 * 自选股页面的入口组件
 */
@Composable
fun StockTabHostScreen(
    viewModel: StockViewModel,
    navController: NavHostController
) {
    // 直接显示StockScreen，由内部状态控制显示内容
    StockScreen(viewModel, navController)
} 