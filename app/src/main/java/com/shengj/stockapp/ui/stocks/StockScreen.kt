package com.shengj.stockapp.ui.stocks

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.hilt.navigation.compose.hiltViewModel
import com.shengj.stockapp.model.SortColumn
import com.shengj.stockapp.model.Stock
import com.shengj.stockapp.model.TabType

@Composable
fun StockScreen(
    viewModel: StockViewModel = hiltViewModel()
) {
    val viewState by viewModel.viewState.observeAsState(StockViewState.Loading)
    val currentTabType by viewModel.currentTabType.observeAsState(TabType.ALL)
    val currentSortColumn by viewModel.sortColumn.observeAsState(SortColumn.LATEST)
    
    Scaffold(
        bottomBar = { BottomNavBar() }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF5F5F5))
        ) {
            TopTabBar(
                onTabSelected = { viewModel.switchTopTab(it) }
            )
            
            SubTabBar(
                currentTab = currentTabType,
                onTabSelected = { viewModel.loadStocks(it) }
            )
            
            when (val state = viewState) {
                is StockViewState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                is StockViewState.Success -> {
                    StockTable(
                        stocks = state.stocks,
                        currentSortColumn = currentSortColumn,
                        onSortColumnSelected = { viewModel.setSortColumn(it) }
                    )
                }
                is StockViewState.Error -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = state.message,
                            color = Color.Red
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TopTabBar(
    onTabSelected: (TopTabType) -> Unit
) {
    var selectedTab by remember { mutableStateOf(0) }
    
    val tabs = listOf(
        "自选股" to TopTabType.STOCK,
        "基金" to TopTabType.FUND,
        "组合" to TopTabType.PORTFOLIO
    )
    
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFFF5C00))
    ) {
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = Color(0xFFFF5C00),
            contentColor = Color.White,
            divider = {},
            modifier = Modifier.fillMaxWidth()
        ) {
            tabs.forEachIndexed { index, (title, type) ->
                Tab(
                    selected = selectedTab == index,
                    onClick = {
                        selectedTab = index
                        onTabSelected(type)
                    },
                    text = {
                        Text(
                            text = title,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                )
            }
        }
    }
}

@Composable
fun SubTabBar(
    currentTab: TabType,
    onTabSelected: (TabType) -> Unit
) {
    val tabs = listOf(
        "全部" to TabType.ALL,
        "持仓" to TabType.POSITION,
        "沪深京" to TabType.A,
        "港股" to TabType.HK,
        "美股" to TabType.US
    )
    
    ScrollableTabRow(
        selectedTabIndex = tabs.indexOfFirst { it.second == currentTab },
        containerColor = Color.White,
        contentColor = Color(0xFFFF5C00),
        divider = { Divider(thickness = 1.dp, color = Color.LightGray) },
        edgePadding = 0.dp
    ) {
        tabs.forEach { (title, type) ->
            Tab(
                selected = currentTab == type,
                onClick = { onTabSelected(type) },
                text = {
                    Text(
                        text = title,
                        fontWeight = if (currentTab == type) FontWeight.Bold else FontWeight.Normal,
                        color = if (currentTab == type) Color(0xFFFF5C00) else Color.Black
                    )
                },
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
            )
        }
    }
}

@Composable
fun StockTable(
    stocks: List<Stock>,
    currentSortColumn: SortColumn,
    onSortColumnSelected: (SortColumn) -> Unit
) {
    val horizontalScrollState = rememberScrollState()
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF5F5F5))
                .padding(vertical = 8.dp)
        ) {
            // 固定的股票名称和代码列
            Box(
                modifier = Modifier
                    .width(120.dp)
                    .padding(start = 8.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = "名称/代码",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
            
            // 可滑动的其他列头
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(horizontalScrollState)
            ) {
                TableHeader("最新", SortColumn.LATEST, currentSortColumn, onSortColumnSelected)
                TableHeader("涨幅", SortColumn.CHANGE_PERCENT, currentSortColumn, onSortColumnSelected)
                TableHeader("涨跌", SortColumn.CHANGE, currentSortColumn, onSortColumnSelected)
                TableHeader("涨速", SortColumn.MOMENTUM, currentSortColumn, onSortColumnSelected)
                TableHeader("总量", SortColumn.VOLUME, currentSortColumn, onSortColumnSelected)
                TableHeader("现量", SortColumn.CURRENT_VOLUME, currentSortColumn, onSortColumnSelected)
                TableHeader("金额", SortColumn.AMOUNT, currentSortColumn, onSortColumnSelected)
                TableHeader("量比", SortColumn.VOLUME_RATIO, currentSortColumn, onSortColumnSelected)
                TableHeader("最高", SortColumn.HIGH, currentSortColumn, onSortColumnSelected)
                TableHeader("最低", SortColumn.LOW, currentSortColumn, onSortColumnSelected)
                TableHeader("振幅", SortColumn.AMPLITUDE, currentSortColumn, onSortColumnSelected)
                TableHeader("换手", SortColumn.TURNOVER_RATE, currentSortColumn, onSortColumnSelected)
            }
        }
        
        LazyColumn {
            items(stocks) { stock ->
                StockRow(stock, horizontalScrollState)
                Divider(color = Color(0xFFEEEEEE), thickness = 0.5.dp)
            }
        }
    }
}

@Composable
fun TableHeader(
    title: String,
    column: SortColumn,
    currentSortColumn: SortColumn,
    onSortColumnSelected: (SortColumn) -> Unit
) {
    Box(
        modifier = Modifier
            .width(80.dp)
            .clickable { onSortColumnSelected(column) }
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            fontSize = 12.sp,
            color = if (currentSortColumn == column) Color(0xFFFF5C00) else Color.Gray,
            fontWeight = if (currentSortColumn == column) FontWeight.Bold else FontWeight.Normal
        )
    }
}

@Composable
fun StockRow(
    stock: Stock,
    horizontalScrollState: androidx.compose.foundation.ScrollState
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        // 固定的股票名称和代码列
        Column(
            modifier = Modifier
                .width(120.dp)
                .padding(start = 8.dp)
        ) {
            Text(
                text = stock.name,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = stock.code,
                fontSize = 12.sp,
                color = Color.Gray
            )
        }
        
        // 可滑动的其他数据列
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(horizontalScrollState)
        ) {
            StockValueCell(stock.price.toString(), stock.changePercent >= 0)
            StockChangePercentCell(stock.changePercent)
            StockValueCell(stock.change.toString(), stock.change >= 0)
            StockValueCell("--", true) // 涨速数据占位
            StockValueCell(formatNumber(stock.volume), true)
            StockValueCell(formatNumber(stock.currentVolume), true)
            StockValueCell(formatNumber(stock.amount), true)
            StockValueCell(stock.volumeRatio.toString(), true)
            StockValueCell(stock.high.toString(), true)
            StockValueCell(stock.low.toString(), true)
            StockValueCell(stock.amplitude.toString() + "%", true)
            StockValueCell(stock.turnoverRate.toString() + "%", true)
        }
    }
}

@Composable
fun StockValueCell(
    value: String,
    isPositive: Boolean
) {
    Box(
        modifier = Modifier
            .width(80.dp)
            .padding(horizontal = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = value,
            fontSize = 13.sp,
            color = when {
                value == "--" -> Color.Gray
                isPositive -> Color.Red
                else -> Color.Green
            },
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun StockChangePercentCell(changePercent: Double) {
    val displayText = if (changePercent >= 0) "+${changePercent}%" else "${changePercent}%"
    val backgroundColor = when {
        changePercent > 0 -> Color.Red
        changePercent < 0 -> Color.Green
        else -> Color.Gray
    }
    
    Box(
        modifier = Modifier
            .width(80.dp)
            .padding(horizontal = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = displayText,
            fontSize = 13.sp,
            color = Color.White,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .background(
                    color = backgroundColor,
                    shape = RoundedCornerShape(4.dp)
                )
                .padding(horizontal = 4.dp, vertical = 2.dp)
        )
    }
}

@Composable
fun BottomNavBar() {
    BottomAppBar(
        containerColor = Color.White,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            NavBarItem(
                icon = Icons.Default.Home,
                label = "首页",
                modifier = Modifier.weight(1f)
            )
            NavBarItem(
                icon = Icons.Default.Person,
                label = "社区",
                modifier = Modifier.weight(1f)
            )
            NavBarItem(
                icon = Icons.Default.List,
                label = "自选",
                isSelected = true,
                modifier = Modifier.weight(1f)
            )
            NavBarItem(
                icon = Icons.Default.Search,
                label = "行情",
                modifier = Modifier.weight(1f)
            )
            NavBarItem(
                icon = Icons.Default.Settings,
                label = "理财",
                modifier = Modifier.weight(1f)
            )
            NavBarItem(
                icon = Icons.Default.Home,
                label = "交易",
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun NavBarItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    isSelected: Boolean = false,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(8.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = if (isSelected) Color(0xFFFF5C00) else Color.Gray
        )
        Text(
            text = label,
            color = if (isSelected) Color(0xFFFF5C00) else Color.Gray,
            fontSize = 12.sp
        )
    }
}

// 辅助函数
fun formatNumber(number: Long): String {
    return when {
        number >= 100000000 -> String.format("%.2f亿", number / 100000000.0)
        number >= 10000 -> String.format("%.2f万", number / 10000.0)
        else -> number.toString()
    }
}

fun formatNumber(number: Double): String {
    return when {
        number >= 100000000 -> String.format("%.2f亿", number / 100000000.0)
        number >= 10000 -> String.format("%.2f万", number / 10000.0)
        else -> number.toString()
    }
} 