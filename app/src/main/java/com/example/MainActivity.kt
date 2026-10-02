package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.LauncherScreen
import com.example.ui.LauncherViewModel
import com.example.ui.components.DockBar
import com.example.ui.components.IconCustomizerSheet
import com.example.ui.components.XrayBackground
import com.example.ui.screens.AppDrawerScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.theme.HudThemePreset
import com.example.ui.theme.ObsidianBg
import com.example.ui.theme.XRayLauncherTheme

class MainActivity : ComponentActivity() {

    private val viewModel: LauncherViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        @OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
        setContent {
            val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()
            val filteredApps by viewModel.filteredApps.collectAsStateWithLifecycle()
            val pinnedApps by viewModel.pinnedApps.collectAsStateWithLifecycle()
            val dockApps by viewModel.dockApps.collectAsStateWithLifecycle()
            val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
            val selectedCategory by viewModel.selectedCategory.collectAsStateWithLifecycle()
            val iconConfig by viewModel.iconConfig.collectAsStateWithLifecycle()

            var showCustomizerSheet by remember { mutableStateOf(false) }

            val themePreset = remember(iconConfig.themePresetName) {
                try {
                    HudThemePreset.valueOf(iconConfig.themePresetName)
                } catch (e: Exception) {
                    HudThemePreset.GLIDE_CYAN
                }
            }

            XRayLauncherTheme(themePreset = themePreset) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = ObsidianBg
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        XrayBackground()

                        AnimatedContent(
                            targetState = currentScreen,
                            transitionSpec = {
                                fadeIn(animationSpec = tween(220)) togetherWith
                                fadeOut(animationSpec = tween(180))
                            },
                            label = "screen_transition",
                            modifier = Modifier.fillMaxSize()
                        ) { screen ->
                            when (screen) {
                                LauncherScreen.HOME -> {
                                    HomeScreen(
                                        pinnedApps = pinnedApps,
                                        config = iconConfig,
                                        onOpenCustomizer = { showCustomizerSheet = true },
                                        onNavigate = viewModel::navigateTo,
                                        onLaunchApp = viewModel::launchApp,
                                        onTogglePin = viewModel::togglePin,
                                        onOpenDetails = viewModel::openAppDetails
                                    )
                                }
                                LauncherScreen.DRAWER -> {
                                    AppDrawerScreen(
                                        apps = filteredApps,
                                        searchQuery = searchQuery,
                                        selectedCategory = selectedCategory,
                                        config = iconConfig,
                                        onOpenCustomizer = { showCustomizerSheet = true },
                                        onSearchChange = viewModel::setSearchQuery,
                                        onCategoryChange = viewModel::setCategory,
                                        onNavigate = viewModel::navigateTo,
                                        onLaunchApp = viewModel::launchApp,
                                        onTogglePin = viewModel::togglePin,
                                        onOpenDetails = viewModel::openAppDetails
                                    )
                                }
                            }
                        }

                        // Bottom Minimal Dock
                        DockBar(
                            dockApps = dockApps,
                            currentScreen = currentScreen,
                            config = iconConfig,
                            onNavigate = viewModel::navigateTo,
                            onLaunchApp = viewModel::launchApp,
                            onTogglePin = viewModel::togglePin,
                            onOpenDetails = viewModel::openAppDetails,
                            modifier = Modifier.align(Alignment.BottomCenter)
                        )

                        // Icon Design Customizer Bottom Sheet
                        if (showCustomizerSheet) {
                            IconCustomizerSheet(
                                config = iconConfig,
                                onUpdateShape = viewModel::updateIconShape,
                                onUpdateStyle = viewModel::updateIconStyle,
                                onUpdateScale = viewModel::updateIconScale,
                                onToggleLabels = viewModel::toggleShowLabels,
                                onUpdateTheme = viewModel::updateThemePreset,
                                onDismiss = { showCustomizerSheet = false }
                            )
                        }
                    }
                }
            }
        }
    }
}
