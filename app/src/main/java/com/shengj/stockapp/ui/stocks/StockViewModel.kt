package com.shengj.stockapp.ui.stocks

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shengj.stockapp.data.StockRepository
import com.shengj.stockapp.model.SortColumn
import com.shengj.stockapp.model.Stock
import com.shengj.stockapp.model.TabType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StockViewModel @Inject constructor(
    private val stockRepository: StockRepository
) : ViewModel() {

    private val _viewState = MutableLiveData<StockViewState>(StockViewState.Loading)
    val viewState: LiveData<StockViewState> = _viewState

    private val _currentTabType = MutableLiveData<TabType>(TabType.ALL)
    val currentTabType: LiveData<TabType> = _currentTabType

    private val _sortColumn = MutableLiveData<SortColumn>(SortColumn.LATEST)
    val sortColumn: LiveData<SortColumn> = _sortColumn

    init {
        loadStocks(TabType.ALL)
    }

    fun loadStocks(tabType: TabType) {
        _currentTabType.value = tabType
        viewModelScope.launch {
            stockRepository.getStocks(tabType)
                .onStart { _viewState.value = StockViewState.Loading }
                .catch { e -> _viewState.value = StockViewState.Error(e.message ?: "未知错误") }
                .collect { stocks ->
                    _viewState.value = StockViewState.Success(stocks)
                }
        }
    }

    fun setSortColumn(column: SortColumn) {
        _sortColumn.value = column
        (_viewState.value as? StockViewState.Success)?.let { state ->
            val sortedStocks = when (column) {
                SortColumn.LATEST -> state.stocks.sortedByDescending { it.price }
                SortColumn.CHANGE_PERCENT -> state.stocks.sortedByDescending { it.changePercent }
                SortColumn.CHANGE -> state.stocks.sortedByDescending { it.change }
                SortColumn.VOLUME -> state.stocks.sortedByDescending { it.volume }
                SortColumn.CURRENT_VOLUME -> state.stocks.sortedByDescending { it.currentVolume }
                SortColumn.AMOUNT -> state.stocks.sortedByDescending { it.amount }
                SortColumn.VOLUME_RATIO -> state.stocks.sortedByDescending { it.volumeRatio }
                SortColumn.HIGH -> state.stocks.sortedByDescending { it.high }
                SortColumn.LOW -> state.stocks.sortedByDescending { it.low }
                SortColumn.AMPLITUDE -> state.stocks.sortedByDescending { it.amplitude }
                SortColumn.TURNOVER_RATE -> state.stocks.sortedByDescending { it.turnoverRate }
                else -> state.stocks
            }
            _viewState.value = StockViewState.Success(sortedStocks)
        }
    }

    fun switchTopTab(topTabType: TopTabType) {
        // 实际应用中，这里应该处理自选股、基金和组合的切换逻辑
        // 本示例中仅实现自选股
    }
}

enum class TopTabType {
    STOCK, FUND, PORTFOLIO
}

sealed class StockViewState {
    object Loading : StockViewState()
    data class Success(val stocks: List<Stock>) : StockViewState()
    data class Error(val message: String) : StockViewState()
} 