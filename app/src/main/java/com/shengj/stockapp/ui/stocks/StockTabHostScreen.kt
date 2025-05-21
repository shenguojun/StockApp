package com.shengj.stockapp.ui.stocks

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.shengj.stockapp.ui.navigation.Route

/**
 * 自选股页面的Tab切换导航
 */
@Composable
fun StockTabHostScreen(
    viewModel: StockViewModel,
    navController: NavHostController
) {
    // 在自选股主屏幕内嵌套一个NavHost，用于处理顶部标签页的导航
    NavHost(
        navController = navController,
        startDestination = Route.StocksTab.createRoute(TopTabType.STOCK.name)
    ) {
        // 自选股标签
        composable(Route.StocksTab.createRoute(TopTabType.STOCK.name)) {
            StockScreen(viewModel, navController)
        }
        
        // 基金标签
        composable(Route.StocksTab.createRoute(TopTabType.FUND.name)) {
            viewModel.switchTopTab(TopTabType.FUND)
            FundScreen()
        }
        
        // 组合标签
        composable(Route.StocksTab.createRoute(TopTabType.PORTFOLIO.name)) {
            viewModel.switchTopTab(TopTabType.PORTFOLIO)
            PortfolioScreen()
        }
    }
} 