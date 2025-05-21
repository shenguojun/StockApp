package com.shengj.stockapp.ui.navigation

/**
 * Tab参数名常量
 */
private const val TAB_TYPE_ARG = "tabType"

/**
 * 路由定义
 */
sealed class Route(val route: String) {
    // 主界面底部Tab路由
    object Home : Route("home")
    object Community : Route("community")
    object Stocks : Route("stocks")
    object Market : Route("market")
    object Wealth : Route("wealth")
    object Trade : Route("trade")
    
    // 自选股内部Tab路由
    object StocksTab : Route("stocks/tab/{$TAB_TYPE_ARG}") {
        val routeWithArgs: String = route
        
        fun createRoute(tabType: String): String {
            return "stocks/tab/$tabType"
        }
    }
}

/**
 * 根据路由获取目标Tab索引
 */
fun findBottomNavIndexByRoute(route: String?): Int {
    return when {
        route?.startsWith(Route.Home.route) == true -> 0
        route?.startsWith(Route.Community.route) == true -> 1
        route?.startsWith(Route.Stocks.route) == true -> 2
        route?.startsWith(Route.Market.route) == true -> 3
        route?.startsWith(Route.Wealth.route) == true -> 4
        route?.startsWith(Route.Trade.route) == true -> 5
        else -> 2 // 默认自选
    }
}