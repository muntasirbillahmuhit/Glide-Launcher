package com.example.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.data.model.AppItem
import com.example.data.model.IconDesignConfig
import com.example.ui.LauncherScreen
import com.example.ui.theme.FrostedGlassBorder
import com.example.ui.theme.FrostedSurface
import com.example.ui.theme.LocalHudPreset
import com.example.ui.theme.TextPrimary

@Composable
fun DockBar(
    dockApps: List<AppItem>,
    currentScreen: LauncherScreen,
    config: IconDesignConfig = IconDesignConfig(),
    onNavigate: (LauncherScreen) -> Unit,
    onLaunchApp: (AppItem) -> Unit,
    onTogglePin: (AppItem) -> Unit,
    onOpenDetails: (AppItem) -> Unit,
    modifier: Modifier = Modifier
) {
    val theme = LocalHudPreset.current

    Box(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 24.dp, vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = 16.dp,
                    shape = RoundedCornerShape(32.dp),
                    ambientColor = Color.Black.copy(alpha = 0.5f),
                    spotColor = Color.Black.copy(alpha = 0.7f)
                )
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            FrostedSurface.copy(alpha = 0.88f),
                            FrostedSurface.copy(alpha = 0.96f)
                        )
                    ),
                    shape = RoundedCornerShape(32.dp)
                )
                .border(
                    width = 1.dp,
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.3f),
                            FrostedGlassBorder
                        )
                    ),
                    shape = RoundedCornerShape(32.dp)
                )
                .padding(horizontal = 14.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Dock Favorite Apps (Up to 4)
            dockApps.distinctBy { it.packageName }.take(4).forEach { app ->
                AppIconItem(
                    app = app,
                    config = config,
                    iconSize = 44.dp,
                    showLabel = false,
                    onLaunch = onLaunchApp,
                    onTogglePin = onTogglePin,
                    onOpenDetails = onOpenDetails
                )
            }

            // All Apps Drawer / Home Switcher Pill
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                theme.primary.copy(alpha = 0.25f),
                                theme.secondary.copy(alpha = 0.25f)
                            )
                        ),
                        shape = CircleShape
                    )
                    .border(1.dp, theme.primary.copy(alpha = 0.6f), CircleShape)
                    .clickable {
                        if (currentScreen == LauncherScreen.DRAWER) {
                            onNavigate(LauncherScreen.HOME)
                        } else {
                            onNavigate(LauncherScreen.DRAWER)
                        }
                    }
                    .testTag("dock_toggle_drawer_button")
            ) {
                AnimatedContent(
                    targetState = currentScreen,
                    transitionSpec = { fadeIn(tween(150)) togetherWith fadeOut(tween(150)) },
                    label = "drawer_icon_anim"
                ) { screen ->
                    Icon(
                        imageVector = if (screen == LauncherScreen.DRAWER) Icons.Default.Home else Icons.Default.Apps,
                        contentDescription = "Toggle Drawer",
                        tint = theme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}
