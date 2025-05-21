package com.shengj.stockapp.ui


import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.shengj.stockapp.ui.community.CommunityScreen
import com.shengj.stockapp.ui.home.HomeScreen
import com.shengj.stockapp.ui.market.MarketScreen
import com.shengj.stockapp.ui.navigation.Route
import com.shengj.stockapp.ui.navigation.findBottomNavIndexByRoute
import com.shengj.stockapp.ui.stocks.StockTabHostScreen
import com.shengj.stockapp.ui.stocks.StockViewModel
import com.shengj.stockapp.ui.theme.Gray
import com.shengj.stockapp.ui.theme.Orange
import com.shengj.stockapp.ui.theme.White
import com.shengj.stockapp.ui.trade.TradeScreen
import com.shengj.stockapp.ui.wealth.WealthScreen

private const val TAG = "MainScreen"

/**
 * 底部导航项定义
 */
enum class BottomNavItem(
    val icon: ImageVector,
    val label: String,
    val route: String
) {
    HOME(Icons.Default.Home, "首页", Route.Home.route),
    COMMUNITY(Icons.Default.Person, "社区", Route.Community.route),
    STOCKS(Icons.Default.List, "自选", Route.Stocks.route),
    MARKET(Icons.Default.Search, "行情", Route.Market.route),
    WEALTH(Icons.Default.Settings, "理财", Route.Wealth.route),
    TRADE(Icons.Default.Home, "交易", Route.Trade.route)
}

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    
    // 监听当前导航目的地
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    
    Log.d(TAG, "Current Route: $currentRoute")
    
    // 根据当前路由设置选中的底部导航项
    val selectedIndex = remember(currentRoute) {
        findBottomNavIndexByRoute(currentRoute)
    }
    
    Scaffold(
        bottomBar = {
            BottomNavBar(
                navController = navController,
                selectedTabIndex = selectedIndex
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // 直接在MainScreen中实现NavHost，而不是使用AppNavigation
            MainNavHost(navController)
        }
    }
}

@Composable
fun MainNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Route.Stocks.route
    ) {
        // 首页
        composable(Route.Home.route) {
            HomeScreen()
        }
        
        // 社区
        composable(Route.Community.route) {
            CommunityScreen()
        }
        
        // 自选股导航图
        composable(Route.Stocks.route) {
            val viewModel: StockViewModel = hiltViewModel()
            val stockNavController = rememberNavController()
            StockTabHostScreen(viewModel, stockNavController)
        }
        
        // 行情
        composable(Route.Market.route) {
            MarketScreen()
        }
        
        // 理财
        composable(Route.Wealth.route) {
            WealthScreen()
        }
        
        // 交易
        composable(Route.Trade.route) {
            TradeScreen()
        }
    }
}

@Composable
private fun BottomNavBar(
    navController: NavHostController,
    selectedTabIndex: Int
) {
    Box(
        modifier = Modifier.navigationBarsPadding()
    ) {
        // 自定义底部导航栏
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(White),
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                BottomNavItem.entries.forEachIndexed { index, item ->
                    NavItem(
                        item = item,
                        isSelected = selectedTabIndex == index,
                        onClick = {
                            Log.d(TAG, "Tab clicked: ${item.label}, route: ${item.route}")
                            if (item.route != navController.currentDestination?.route) {
                                // 简化导航，避免使用graph
                                navController.navigate(item.route) {
                                    // 使用route字符串代替graph.findStartDestination().id
                                    popUpTo(Route.Stocks.route) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun NavItem(
    item: BottomNavItem,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val color = if (isSelected) Orange else Gray
    
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable(
                onClick = onClick,
                indication = null,
                interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() }
            )
            .padding(8.dp)
            .width(48.dp)
    ) {
        Icon(
            imageVector = item.icon,
            contentDescription = item.label,
            tint = color
        )
        
        Spacer(modifier = Modifier.height(4.dp))
        
        Text(
            text = item.label,
            color = color,
            fontSize = 10.sp,
            maxLines = 1,
            overflow = TextOverflow.Visible,
            textAlign = TextAlign.Center
        )
    }
} 