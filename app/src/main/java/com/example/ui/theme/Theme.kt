package com.example.ui.theme

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

val LocalHudPreset = staticCompositionLocalOf { HudThemePreset.GLIDE_CYAN }

private fun Context.findActivity(): Activity? {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) return context
        context = context.baseContext
    }
    return null
}

@Composable
fun XRayLauncherTheme(
    themePreset: HudThemePreset = HudThemePreset.GLIDE_CYAN,
    content: @Composable () -> Unit
) {
    val colorScheme = darkColorScheme(
        primary = themePreset.primary,
        onPrimary = Color.Black,
        primaryContainer = themePreset.primaryDim,
        onPrimaryContainer = themePreset.primary,
        secondary = themePreset.secondary,
        onSecondary = Color.White,
        background = ObsidianBg,
        onBackground = TextPrimary,
        surface = FrostedSurface,
        onSurface = TextPrimary,
        surfaceVariant = FrostedSurfaceVariant,
        onSurfaceVariant = TextSecondary,
        outline = FrostedGlassBorder
    )

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val activity = view.context.findActivity()
            if (activity != null) {
                val window = activity.window
                window.statusBarColor = Color.Transparent.toArgb()
                window.navigationBarColor = Color.Transparent.toArgb()
                val insetsController = WindowCompat.getInsetsController(window, view)
                insetsController.isAppearanceLightStatusBars = false
                insetsController.isAppearanceLightNavigationBars = false
            }
        }
    }

    CompositionLocalProvider(LocalHudPreset provides themePreset) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            shapes = GlideShapes,
            content = content
        )
    }
}
