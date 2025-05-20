package com.shengj.stockapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.shengj.stockapp.ui.MainScreen
import com.shengj.stockapp.ui.theme.StockAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // 启用边缘到边缘显示
        enableEdgeToEdge()
        
        // 配置WindowInsets控制器 - 设置为true，让系统UI不与内容重叠
        WindowCompat.setDecorFitsSystemWindows(window, true)
        
        setContent {
            val systemUiController = rememberSystemUiController()
            
            SideEffect {
                // 设置状态栏为App主题橙色，使用亮色图标
                systemUiController.setStatusBarColor(
                    color = Color(0xFFFF5C00),
                    darkIcons = false
                )
                
                // 设置导航栏为白色，使用暗色图标
                systemUiController.setNavigationBarColor(
                    color = Color.White,
                    darkIcons = true
                )
                
                // 确保系统栏是可见的
                systemUiController.systemBarsDarkContentEnabled = false
            }
            
            StockAppTheme {
                MainScreen()
            }
        }
    }
}