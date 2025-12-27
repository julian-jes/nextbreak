package com.julianjesacher.nextbreak.ui

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowInsetsControllerCompat

@Composable //TODO Needs review!
fun SystemBars() {
    val view = LocalView.current
    if (!view.isInEditMode) {
        val window = (view.context as Activity).window
        val controller = WindowInsetsControllerCompat(window, window.decorView)

        // Make icons white (dark icons = false)
        controller.isAppearanceLightStatusBars = false
        controller.isAppearanceLightNavigationBars = false

        // Use transparent background without deprecated APIs
        window.statusBarColor = Color.Transparent.value.toInt()
        window.navigationBarColor = Color.Transparent.value.toInt()
    }
}