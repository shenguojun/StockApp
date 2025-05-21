package com.shengj.stockapp.ui.stocks

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shengj.stockapp.R
import com.shengj.stockapp.StockApplication
import com.shengj.stockapp.data.StockRepository
import com.shengj.stockapp.model.SortColumn
import com.shengj.stockapp.model.Stock
import com.shengj.stockapp.model.TabType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "StockViewModel"

@HiltViewModel
class StockViewModel @Inject constructor(
    private val stockRepository: StockRepository
) : ViewModel() {

    private val _viewState = MutableStateFlow<StockViewState>(StockViewState.Loading)
    val viewState: StateFlow<StockViewState> = _viewState

    private val _currentTabType = MutableStateFlow(TabType.ALL)
    val currentTabType: StateFlow<TabType> = _currentTabType

    private val _sortColumn = MutableStateFlow(SortColumn.LATEST)
    val sortColumn: StateFlow<SortColumn> = _sortColumn
    
    private val _topTabType = MutableStateFlow(TopTabType.STOCK)
    val topTabType: StateFlow<TopTabType> = _topTabType

    init {
        loadStocks(TabType.ALL)
    }

    fun loadStocks(tabType: TabType) {
        Log.d(TAG, "Loading stocks for tab: $tabType")
        _currentTabType.value = tabType
        viewModelScope.launch {
            stockRepository.getStocks(tabType)
                .onStart { _viewState.value = StockViewState.Loading }
                .catch { e -> _viewState.value = StockViewState.Error(e.message ?: StockApplication.instance.getString(R.string.error_unknown)) }
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
        Log.d(TAG, "Switching top tab to: $topTabType")
        _topTabType.value = topTabType
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