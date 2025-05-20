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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
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
        bottomBar = {
            Box(
                modifier = Modifier.navigationBarsPadding()
            ) {
                // 自定义底部导航栏
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        BottomNavItem.entries.forEach { item ->
                            NavItem(
                                item = item,
                                isSelected = currentTab == item,
                                onClick = {
                                    Log.d(TAG, "Tab clicked: ${item.label}")
                                    currentTab = item
                                }
                            )
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

@Composable
private fun NavItem(
    item: BottomNavItem,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val color = if (isSelected) Color(0xFFFF5C00) else Color.Gray
    
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable(onClick = onClick)
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