package com.shengj.stockapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.shengj.stockapp.ui.MainScreen
import com.shengj.stockapp.ui.theme.Orange
import com.shengj.stockapp.ui.theme.StockAppTheme
import com.shengj.stockapp.ui.theme.White
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // 启用边缘到边缘显示
        enableEdgeToEdge()
        
        // 配置WindowInsets控制器
        WindowCompat.setDecorFitsSystemWindows(window, false)
        
        // 设置状态栏颜色（适用于低版本Android）
        window.statusBarColor = android.graphics.Color.parseColor("#FF5C00")
        
        setContent {
            val systemUiController = rememberSystemUiController()
            
            SideEffect {
                // 设置状态栏为App主题橙色，使用亮色图标
                systemUiController.setStatusBarColor(
                    color = Orange,
                    darkIcons = false
                )
                
                // 确保导航栏也一致处理
                systemUiController.setNavigationBarColor(
                    color = White,
                    darkIcons = true
                )
            }
            
            StockAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = White
                ) {
                    MainScreen()
                }
            }
        }
    }
}