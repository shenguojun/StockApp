package com.shengj.stockapp.ui.stocks

import android.util.Log
import androidx.compose.foundation.background
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
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.shengj.stockapp.R
import com.shengj.stockapp.model.SortColumn
import com.shengj.stockapp.model.Stock
import com.shengj.stockapp.model.StockType
import com.shengj.stockapp.model.TabType
import com.shengj.stockapp.ui.components.ErrorState
import com.shengj.stockapp.ui.components.LoadingState
import com.shengj.stockapp.ui.components.rememberDerivedState
import com.shengj.stockapp.ui.theme.Background
import com.shengj.stockapp.ui.theme.Modifiers.noRippleClickable
import com.shengj.stockapp.ui.theme.Orange
import com.shengj.stockapp.ui.theme.White
import com.shengj.stockapp.utils.formatNumberWithResource
import com.shengj.stockapp.utils.formatPercentWithResource

private const val TAG = "StockScreen"

@Composable
fun StockScreen(
    viewModel: StockViewModel,
    navController: NavHostController = rememberNavController()
) {
    val viewState by viewModel.viewState.collectAsState()
    val currentTabType by viewModel.currentTabType.collectAsState()
    val currentSortColumn by viewModel.sortColumn.collectAsState()
    val topTabType by viewModel.topTabType.collectAsState()
    
    Log.d(TAG, "Current top tab: $topTabType")
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {
        TopTabBar(
            selectedTabType = topTabType,
            onTabSelected = { 
                Log.d(TAG, "Top tab selected: $it")
                // 直接切换标签，不导航到新页面
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
                        LoadingState()
                    }
                    is StockViewState.Success -> {
                        StockTable(
                            stocks = state.stocks,
                            currentSortColumn = currentSortColumn,
                            onSortColumnSelected = { viewModel.setSortColumn(it) }
                        )
                    }
                    is StockViewState.Error -> {
                        ErrorState(message = state.message)
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
        stringResource(R.string.toptab_stock) to TopTabType.STOCK,
        stringResource(R.string.toptab_fund) to TopTabType.FUND,
        stringResource(R.string.toptab_portfolio) to TopTabType.PORTFOLIO
    )
    
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Orange)
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
                        text = stringResource(R.string.logo_top),
                        color = White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    Text(
                        text = stringResource(R.string.logo_bottom),
                        color = White,
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
                            .noRippleClickable { onTabSelected(type) },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = title,
                            color = White,
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
        stringResource(R.string.tab_all) to TabType.ALL,
        stringResource(R.string.tab_position) to TabType.POSITION,
        stringResource(R.string.tab_a) to TabType.A,
        stringResource(R.string.tab_hk) to TabType.HK,
        stringResource(R.string.tab_us) to TabType.US
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
                        .noRippleClickable { onTabSelected(type) }
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
                .background(Color.White)
                .padding(vertical = 2.dp),
            verticalAlignment = Alignment.CenterVertically  // 确保所有内容垂直居中
        ) {
            // 名称/代码列头
            Box(
                modifier = Modifier
                    .width(130.dp)
                    .height(32.dp)  // 明确指定高度
                    .padding(start = 16.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = stringResource(R.string.stock_column_name_code),
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
                TableHeader(stringResource(R.string.stock_column_latest), SortColumn.LATEST, currentSortColumn, onSortColumnSelected, Color(0xFFFF5C00))
                TableHeader(stringResource(R.string.stock_column_change_percent), SortColumn.CHANGE_PERCENT, currentSortColumn, onSortColumnSelected, Color(0xFF666666))
                TableHeader(stringResource(R.string.stock_column_change), SortColumn.CHANGE, currentSortColumn, onSortColumnSelected, Color(0xFF666666))
                TableHeader(stringResource(R.string.stock_column_momentum), SortColumn.MOMENTUM, currentSortColumn, onSortColumnSelected, Color(0xFF666666))
                TableHeader(stringResource(R.string.stock_column_volume), SortColumn.VOLUME, currentSortColumn, onSortColumnSelected, Color(0xFF666666))
                TableHeader(stringResource(R.string.stock_column_current_volume), SortColumn.CURRENT_VOLUME, currentSortColumn, onSortColumnSelected, Color(0xFF666666))
                TableHeader(stringResource(R.string.stock_column_amount), SortColumn.AMOUNT, currentSortColumn, onSortColumnSelected, Color(0xFF666666))
                TableHeader(stringResource(R.string.stock_column_volume_ratio), SortColumn.VOLUME_RATIO, currentSortColumn, onSortColumnSelected, Color(0xFF666666))
                TableHeader(stringResource(R.string.stock_column_high), SortColumn.HIGH, currentSortColumn, onSortColumnSelected, Color(0xFF666666))
                TableHeader(stringResource(R.string.stock_column_low), SortColumn.LOW, currentSortColumn, onSortColumnSelected, Color(0xFF666666))
                TableHeader(stringResource(R.string.stock_column_amplitude), SortColumn.AMPLITUDE, currentSortColumn, onSortColumnSelected, Color(0xFF666666))
                TableHeader(stringResource(R.string.stock_column_turnover_rate), SortColumn.TURNOVER_RATE, currentSortColumn, onSortColumnSelected, Color(0xFF666666))
            }
        }
        
        // 添加一条分隔线
        Divider(color = Color(0xFFEEEEEE), thickness = 0.5.dp)
        
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
            .width(90.dp)
            .height(32.dp)  // 明确指定高度
            .noRippleClickable { onSortColumnSelected(column) }
            .padding(horizontal = 8.dp),
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
    // 先在Composable上下文中获取资源字符串
    val emptyPlaceholder = stringResource(R.string.format_empty)
    val percentFormat = stringResource(R.string.format_percent)
    
    // 在Composable上下文中预格式化所有值
    val formattedVolume = formatNumberWithResource(stock.volume)
    val formattedCurrentVolume = formatNumberWithResource(stock.currentVolume)
    val formattedAmount = formatNumberWithResource(stock.amount)
    val formattedAmplitude = String.format(percentFormat, stock.amplitude.toString())
    val formattedTurnoverRate = String.format(percentFormat, stock.turnoverRate.toString())
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
    ) {
        // 左侧固定的股票名称和代码列
        Column(
            modifier = Modifier
                .width(130.dp)
                .padding(start = 16.dp)
        ) {
            // 使用派生状态计算字体大小和字母间距
            val nameLength = stock.name.length
            val (fontSize, letterSpacing) = rememberDerivedState(nameLength) { length ->
                val size = when {
                    length <= 4 -> 15.sp  // 短名称使用较大字体
                    length <= 6 -> 14.sp  // 中等长度使用中等字体
                    length <= 10 -> 13.sp // 较长名称使用小字体
                    else -> 12.sp         // 非常长的名称使用最小字体
                }
                
                val spacing = when {
                    length <= 4 -> 0.sp   // 短名称使用正常间距
                    length <= 6 -> (-0.3).sp // 中等长度稍微压缩
                    length <= 10 -> (-0.5).sp // 较长名称压缩间距
                    else -> (-0.8).sp     // 非常长的名称最大压缩间距
                }
                
                size to spacing
            }.value
            
            Text(
                text = stock.name,
                fontSize = fontSize,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                color = Color.Black,
                overflow = TextOverflow.Ellipsis,
                letterSpacing = letterSpacing
            )
            
            Spacer(modifier = Modifier.height(2.dp))
            
            Row(verticalAlignment = Alignment.CenterVertically) {
                // 显示股票代码
                Text(
                    text = stock.code,
                    fontSize = 11.sp,
                    color = Color.Gray
                )
                
                // 根据股票类型显示标签
                when (stock.type) {
                    StockType.HK -> StockTypeTag("HK")
                    StockType.US -> StockTypeTag("US")
                    else -> {} // 不显示标签
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
            // 使用安全的方式预格式化值
            val price = stock.price.toString()
            val change = if (stock.change >= 0) stock.change.toString() else "${stock.change}"
            val volumeRatio = stock.volumeRatio.toString()
            val high = stock.high.toString()
            val low = stock.low.toString()
            
            // 最新价格
            PriceCell(price, stock.changePercent >= 0)
            
            // 涨幅百分比
            ChangePercentCell(stock.changePercent)
            
            // 涨跌值
            PriceCell(change, stock.change >= 0)
            
            // 涨速 (占位)
            PriceCell(emptyPlaceholder, true)
            
            // 总量
            PriceCell(formattedVolume, true)
            
            // 现量
            PriceCell(formattedCurrentVolume, true)
            
            // 金额
            PriceCell(formattedAmount, true)
            
            // 量比
            PriceCell(volumeRatio, true)
            
            // 最高
            PriceCell(high, true)
            
            // 最低
            PriceCell(low, true)
            
            // 振幅
            PriceCell(formattedAmplitude, true)
            
            // 换手
            PriceCell(formattedTurnoverRate, true)
        }
    }
}

@Composable
fun StockTypeTag(type: String) {
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
            text = type,
            color = Color.White,
            fontSize = 9.sp
        )
    }
}

@Composable
fun PriceCell(
    value: String,
    isPositive: Boolean
) {
    Box(
        modifier = Modifier
            .width(90.dp)
            .height(32.dp),  // 明确指定高度
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = value,
            fontSize = 14.sp,
            color = when {
                value == "--" -> Color.Gray
                isPositive -> Color.Red
                else -> Color(0xFF00C800)
            },
            fontWeight = FontWeight.Normal
        )
    }
}

@Composable
fun ChangePercentCell(changePercent: Double) {
    Box(
        modifier = Modifier
            .width(90.dp)
            .height(32.dp),  // 明确指定高度
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .background(
                    color = if (changePercent >= 0) Color.Red else Color(0xFF00C800),
                    shape = RoundedCornerShape(2.dp)
                )
                .padding(horizontal = 6.dp, vertical = 2.dp)
        ) {
            Text(
                text = formatPercentWithResource(changePercent),
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal
            )
        }
    }
} 