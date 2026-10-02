package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppItem
import com.example.data.model.IconDesignConfig
import com.example.ui.LauncherScreen
import com.example.ui.components.AppIconItem
import com.example.ui.theme.FrostedGlassBorder
import com.example.ui.theme.FrostedSurface
import com.example.ui.theme.FrostedSurfaceVariant
import com.example.ui.theme.LocalHudPreset
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import kotlinx.coroutines.launch

@Composable
fun AppDrawerScreen(
    apps: List<AppItem>,
    searchQuery: String,
    selectedCategory: String,
    config: IconDesignConfig = IconDesignConfig(),
    onOpenCustomizer: () -> Unit = {},
    onSearchChange: (String) -> Unit,
    onCategoryChange: (String) -> Unit,
    onNavigate: (LauncherScreen) -> Unit,
    onLaunchApp: (AppItem) -> Unit,
    onTogglePin: (AppItem) -> Unit,
    onOpenDetails: (AppItem) -> Unit,
    modifier: Modifier = Modifier
) {
    val theme = LocalHudPreset.current
    val gridState = rememberLazyGridState()
    val scope = rememberCoroutineScope()

    BackHandler {
        onNavigate(LauncherScreen.HOME)
    }

    val categories = listOf("All", "Tools", "Media", "Social", "Internet", "Games", "General")
    val alphabet = remember { ('A'..'Z').toList() }

    Column(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
            .imePadding()
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        // Top Search Bar Pill
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = 8.dp,
                    shape = RoundedCornerShape(28.dp),
                    ambientColor = Color.Black.copy(alpha = 0.3f),
                    spotColor = Color.Black.copy(alpha = 0.5f)
                )
                .clip(RoundedCornerShape(28.dp))
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            FrostedSurface.copy(alpha = 0.9f),
                            FrostedSurface.copy(alpha = 0.98f)
                        )
                    )
                )
                .border(
                    width = 1.dp,
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.3f),
                            FrostedGlassBorder
                        )
                    ),
                    shape = RoundedCornerShape(28.dp)
                )
                .padding(horizontal = 8.dp, vertical = 2.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { onNavigate(LauncherScreen.HOME) },
                modifier = Modifier.size(36.dp).testTag("drawer_back_button")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back to Home",
                    tint = theme.primary,
                    modifier = Modifier.size(20.dp)
                )
            }

            TextField(
                value = searchQuery,
                onValueChange = onSearchChange,
                placeholder = {
                    Text(
                        text = "Search all apps...",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = TextSecondary,
                            fontSize = 13.5.sp
                        )
                    )
                },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary,
                    cursorColor = theme.primary
                ),
                singleLine = true,
                textStyle = MaterialTheme.typography.bodyMedium.copy(fontSize = 13.5.sp),
                modifier = Modifier
                    .weight(1f)
                    .testTag("drawer_search_input")
            )

            if (searchQuery.isNotEmpty()) {
                IconButton(
                    onClick = { onSearchChange("") },
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Clear",
                        tint = TextSecondary,
                        modifier = Modifier.size(18.dp)
                    )
                }
            } else {
                IconButton(
                    onClick = onOpenCustomizer,
                    modifier = Modifier.size(32.dp).testTag("drawer_open_customizer_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = "Customize Icon Design",
                        tint = theme.primary,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Category Filter Chips
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            categories.forEach { cat ->
                val isSelected = selectedCategory.equals(cat, ignoreCase = true)
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(
                            if (isSelected) theme.primary.copy(alpha = 0.2f) else FrostedSurfaceVariant.copy(alpha = 0.6f)
                        )
                        .border(
                            width = 1.dp,
                            color = if (isSelected) theme.primary.copy(alpha = 0.6f) else FrostedGlassBorder,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .clickable { onCategoryChange(cat) }
                        .padding(horizontal = 14.dp, vertical = 6.dp)
                        .testTag("filter_chip_$cat")
                ) {
                    Text(
                        text = cat,
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = if (isSelected) theme.primary else TextSecondary,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 12.sp
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // All Apps Grid + Right Alphabet Index Bar
        Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
            if (apps.isEmpty()) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        tint = TextSecondary.copy(alpha = 0.5f),
                        modifier = Modifier.size(40.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "No apps found",
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = TextSecondary,
                            fontWeight = FontWeight.Medium
                        )
                    )
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(4),
                    state = gridState,
                    contentPadding = PaddingValues(end = if (searchQuery.isEmpty() && apps.size > 8) 24.dp else 4.dp, bottom = 96.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize().testTag("app_drawer_grid")
                ) {
                    items(apps, key = { "${it.packageName}_${it.label}" }) { app ->
                        AppIconItem(
                            app = app,
                            config = config,
                            onLaunch = onLaunchApp,
                            onTogglePin = onTogglePin,
                            onOpenDetails = onOpenDetails
                        )
                    }
                }

                // Vertical Alphabet Fast Scroller (only when full list is active)
                if (searchQuery.isEmpty() && apps.size > 8) {
                    Column(
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .fillMaxHeight()
                            .width(20.dp)
                            .padding(bottom = 96.dp),
                        verticalArrangement = Arrangement.SpaceBetween,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                    alphabet.forEach { char ->
                        Text(
                            text = char.toString(),
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontSize = 8.5.sp,
                                color = TextSecondary.copy(alpha = 0.6f),
                                fontWeight = FontWeight.Medium
                            ),
                            modifier = Modifier
                                .clip(CircleShape)
                                .clickable {
                                    val index = apps.indexOfFirst {
                                        it.label.firstOrNull()?.uppercaseChar() == char
                                    }
                                    if (index >= 0) {
                                        scope.launch {
                                            gridState.animateScrollToItem(index)
                                        }
                                    }
                                }
                        )
                    }
                }
            }
        }
    }
}
}
