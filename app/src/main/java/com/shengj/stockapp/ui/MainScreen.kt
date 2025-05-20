package com.shengj.stockapp.ui

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.BottomAppBar
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shengj.stockapp.ui.community.CommunityScreen
import com.shengj.stockapp.ui.home.HomeScreen
import com.shengj.stockapp.ui.market.MarketScreen
import com.shengj.stockapp.ui.stocks.StockScreen
import com.shengj.stockapp.ui.trade.TradeScreen
import com.shengj.stockapp.ui.wealth.WealthScreen

private const val TAG = "MainScreen"

enum class BottomNavItem(
    val icon: ImageVector,
    val label: String
) {
    HOME(Icons.Default.Home, "首页"),
    COMMUNITY(Icons.Default.Person, "社区"),
    STOCKS(Icons.Default.List, "自选"),
    MARKET(Icons.Default.Search, "行情"),
    WEALTH(Icons.Default.Settings, "理财"),
    TRADE(Icons.Default.Home, "交易")
}

@Composable
fun MainScreen() {
    var currentTab by remember { mutableStateOf(BottomNavItem.STOCKS) }
    
    Log.d(TAG, "CurrentTab: ${currentTab.label}")
    
    Scaffold(
        modifier = Modifier.statusBarsPadding(),
        bottomBar = {
            Column(modifier = Modifier.navigationBarsPadding()) {
                BottomAppBar(
                    backgroundColor = Color.White,
                    elevation = 8.dp
                ) {
                    BottomNavItem.values().forEach { item ->
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clickable { 
                                    Log.d(TAG, "Tab clicked: ${item.label}")
                                    currentTab = item 
                                }
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    imageVector = item.icon,
                                    contentDescription = item.label,
                                    tint = if (currentTab == item) Color(0xFFFF5C00) else Color.Gray
                                )
                                Text(
                                    text = item.label,
                                    color = if (currentTab == item) Color(0xFFFF5C00) else Color.Gray,
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (currentTab) {
                BottomNavItem.HOME -> HomeScreen()
                BottomNavItem.COMMUNITY -> CommunityScreen()
                BottomNavItem.STOCKS -> StockScreen()
                BottomNavItem.MARKET -> MarketScreen()
                BottomNavItem.WEALTH -> WealthScreen()
                BottomNavItem.TRADE -> TradeScreen()
            }
        }
    }
} 