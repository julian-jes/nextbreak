package com.julianjesacher.nextbreak

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import com.julianjesacher.nextbreak.data.FileManager
import com.julianjesacher.nextbreak.ui.NextBreakApp
import com.julianjesacher.nextbreak.ui.theme.NextBreakTheme
import com.julianjesacher.nextbreak.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        FileManager.init(this)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            NextBreakTheme {
                NextBreakApp(viewModel)
            }
        }
        viewModel.loadData()
    }
}