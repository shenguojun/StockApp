package com.shengj.stockapp.ui.stocks

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.shengj.stockapp.model.SortColumn
import com.shengj.stockapp.model.Stock
import com.shengj.stockapp.model.StockType
import com.shengj.stockapp.model.TabType

private const val TAG = "StockScreen"

@Composable
fun StockScreen(
    viewModel: StockViewModel = hiltViewModel()
) {
    val viewState by viewModel.viewState.observeAsState(StockViewState.Loading)
    val currentTabType by viewModel.currentTabType.observeAsState(TabType.ALL)
    val currentSortColumn by viewModel.sortColumn.observeAsState(SortColumn.LATEST)
    val topTabType by viewModel.topTabType.observeAsState(TopTabType.STOCK)
    
    Log.d(TAG, "Current top tab: $topTabType")
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        TopTabBar(
            selectedTabType = topTabType,
            onTabSelected = { 
                Log.d(TAG, "Top tab selected: $it")
                viewModel.switchTopTab(it) 
            }
        )
        
        // 根据顶部标签类型显示不同的内容
        when (topTabType) {
            TopTabType.STOCK -> {
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
            TopTabType.FUND -> {
                FundScreen()
            }
            TopTabType.PORTFOLIO -> {
                PortfolioScreen()
            }
        }
    }
}

@Composable
fun TopTabBar(
    selectedTabType: TopTabType,
    onTabSelected: (TopTabType) -> Unit
) {
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
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
        ) {
            // 顶部栏包含Logo和标签
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp, horizontal = 16.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 左侧Logo - "东方财富"
                Column(
                    modifier = Modifier.padding(end = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "东方",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    Text(
                        text = "财富",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
                
                // Tab栏
                tabs.forEach { (title, type) ->
                    val isSelected = selectedTabType == type
                    Box(
                        modifier = Modifier
                            .padding(end = 24.dp)
                            .clickable { onTabSelected(type) },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = title,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = if (isSelected) 20.sp else 16.sp,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                }
                
                // 右侧空间，保持布局平衡
                Spacer(modifier = Modifier.weight(1f))
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
    
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
    ) {
        // 使用Row替代ScrollableTabRow，实现更接近截图的布局
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.Start
        ) {
            tabs.forEach { (title, type) ->
                val isSelected = currentTab == type
                Box(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .clickable { onTabSelected(type) }
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = title,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) Color(0xFFFF5C00) else Color.Black,
                            fontSize = 14.sp
                        )
                        if (isSelected) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Box(
                                modifier = Modifier
                                    .width(20.dp)
                                    .height(3.dp)
                                    .background(Color(0xFFFF5C00))
                            )
                        }
                    }
                }
            }
        }
        
        // 底部分隔线
        Divider(
            color = Color(0xFFEEEEEE), 
            thickness = 1.dp,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
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
        // 表头行
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF5F5F5))
                .padding(vertical = 12.dp)
        ) {
            // 名称/代码列头
            Box(
                modifier = Modifier
                    .width(120.dp)
                    .padding(start = 16.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = "名称/代码",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
            
            // 可滑动的其他表头列
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(horizontalScrollState)
            ) {
                TableHeader("最新", SortColumn.LATEST, currentSortColumn, onSortColumnSelected, Color(0xFFFF5C00))
                TableHeader("涨幅", SortColumn.CHANGE_PERCENT, currentSortColumn, onSortColumnSelected, Color(0xFF666666))
                TableHeader("涨跌", SortColumn.CHANGE, currentSortColumn, onSortColumnSelected, Color(0xFF666666))
                TableHeader("涨速", SortColumn.MOMENTUM, currentSortColumn, onSortColumnSelected, Color(0xFF666666))
                TableHeader("总量", SortColumn.VOLUME, currentSortColumn, onSortColumnSelected, Color(0xFF666666))
                TableHeader("现量", SortColumn.CURRENT_VOLUME, currentSortColumn, onSortColumnSelected, Color(0xFF666666))
                TableHeader("金额", SortColumn.AMOUNT, currentSortColumn, onSortColumnSelected, Color(0xFF666666))
                TableHeader("量比", SortColumn.VOLUME_RATIO, currentSortColumn, onSortColumnSelected, Color(0xFF666666))
                TableHeader("最高", SortColumn.HIGH, currentSortColumn, onSortColumnSelected, Color(0xFF666666))
                TableHeader("最低", SortColumn.LOW, currentSortColumn, onSortColumnSelected, Color(0xFF666666))
                TableHeader("振幅", SortColumn.AMPLITUDE, currentSortColumn, onSortColumnSelected, Color(0xFF666666))
                TableHeader("换手", SortColumn.TURNOVER_RATE, currentSortColumn, onSortColumnSelected, Color(0xFF666666))
            }
        }
        
        // 股票列表
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
    onSortColumnSelected: (SortColumn) -> Unit,
    textColor: Color
) {
    Box(
        modifier = Modifier
            .width(100.dp)
            .clickable { onSortColumnSelected(column) }
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            fontSize = 14.sp,
            color = if (currentSortColumn == column) Color(0xFFFF5C00) else textColor,
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
            .padding(vertical = 16.dp)
    ) {
        // 左侧固定的股票名称和代码列
        Column(
            modifier = Modifier
                .width(120.dp)
                .padding(start = 16.dp)
        ) {
            Text(
                text = stock.name,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                color = Color.Black,
                overflow = TextOverflow.Ellipsis
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Row(verticalAlignment = Alignment.CenterVertically) {
                // 显示股票代码
                Text(
                    text = stock.code,
                    fontSize = 12.sp,
                    color = Color.Gray
                )
                
                // 根据股票类型显示标签
                if (stock.type == StockType.HK) {
                    Box(
                        modifier = Modifier
                            .padding(start = 4.dp)
                            .background(
                                color = Color(0xFF9E9E9E),
                                shape = RoundedCornerShape(2.dp)
                            )
                            .padding(horizontal = 2.dp)
                    ) {
                        Text(
                            text = "HK",
                            color = Color.White,
                            fontSize = 10.sp
                        )
                    }
                } else if (stock.type == StockType.US) {
                    Box(
                        modifier = Modifier
                            .padding(start = 4.dp)
                            .background(
                                color = Color(0xFF9E9E9E),
                                shape = RoundedCornerShape(2.dp)
                            )
                            .padding(horizontal = 2.dp)
                    ) {
                        Text(
                            text = "US",
                            color = Color.White,
                            fontSize = 10.sp
                        )
                    }
                }
            }
        }
        
        // 右侧可滑动部分
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(horizontalScrollState),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 最新价格
            PriceCell(stock.price.toString(), stock.changePercent >= 0)
            
            // 涨幅百分比
            ChangePercentCell(stock.changePercent)
            
            // 涨跌值
            PriceCell(if (stock.change >= 0) stock.change.toString() else "${stock.change}", stock.change >= 0)
            
            // 涨速 (占位)
            PriceCell("--", true)
            
            // 总量
            PriceCell(formatNumber(stock.volume), true)
            
            // 现量
            PriceCell(formatNumber(stock.currentVolume), true)
            
            // 金额
            PriceCell(formatNumber(stock.amount), true)
            
            // 量比
            PriceCell(stock.volumeRatio.toString(), true)
            
            // 最高
            PriceCell(stock.high.toString(), true)
            
            // 最低
            PriceCell(stock.low.toString(), true)
            
            // 振幅
            PriceCell(stock.amplitude.toString() + "%", true)
            
            // 换手
            PriceCell(stock.turnoverRate.toString() + "%", true)
        }
    }
}

@Composable
fun PriceCell(
    value: String,
    isPositive: Boolean
) {
    Box(
        modifier = Modifier.width(100.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = value,
            fontSize = 15.sp,
            color = when {
                value == "--" -> Color.Gray
                isPositive -> Color.Red
                else -> Color(0xFF00C800)
            },
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun ChangePercentCell(changePercent: Double) {
    Box(
        modifier = Modifier.width(100.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .background(
                    color = if (changePercent >= 0) Color.Red else Color(0xFF00C800),
                    shape = RoundedCornerShape(4.dp)
                )
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Text(
                text = if (changePercent >= 0) "+${changePercent}%" else "${changePercent}%",
                color = Color.White,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )
        }
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