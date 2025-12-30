package com.julianjesacher.nextbreak

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import com.julianjesacher.nextbreak.backend.FileManager
import com.julianjesacher.nextbreak.ui.InfoDialog
import com.julianjesacher.nextbreak.ui.NextBreakApp
import com.julianjesacher.nextbreak.viewmodel.MainViewModel
import com.julianjesacher.nextbreak.ui.SystemBars
import com.julianjesacher.nextbreak.ui.theme.NextBreakTheme

class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        FileManager.init(this)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        enableEdgeToEdge()
        setContent {
            NextBreakTheme {
                val infoWindow by viewModel.infoWindow.collectAsState()
                SystemBars()
                NextBreakApp(viewModel)
                if(infoWindow) {
                    InfoDialog(viewModel)
                }
            }
        }
        viewModel.loadData()
    }
}