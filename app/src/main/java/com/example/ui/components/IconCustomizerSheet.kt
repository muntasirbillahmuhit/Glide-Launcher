package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.FormatShapes
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.PhotoSizeSelectLarge
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
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
import com.example.data.model.IconScale
import com.example.data.model.IconShape
import com.example.data.model.IconStyle
import com.example.ui.theme.FrostedGlassBorder
import com.example.ui.theme.FrostedSurface
import com.example.ui.theme.FrostedSurfaceVariant
import com.example.ui.theme.HudThemePreset
import com.example.ui.theme.LocalHudPreset
import com.example.ui.theme.ObsidianBg
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IconCustomizerSheet(
    config: IconDesignConfig,
    onUpdateShape: (IconShape) -> Unit,
    onUpdateStyle: (IconStyle) -> Unit,
    onUpdateScale: (IconScale) -> Unit,
    onToggleLabels: (Boolean) -> Unit,
    onUpdateTheme: (String) -> Unit,
    onDismiss: () -> Unit,
    sheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
) {
    val theme = LocalHudPreset.current

    val sampleApps = listOf(
        AppItem("com.android.chrome", "Browser", null, true, "Internet"),
        AppItem("com.android.camera2", "Camera", null, false, "Media"),
        AppItem("com.android.settings", "Settings", null, true, "Tools"),
        AppItem("com.android.music", "Music", null, false, "Media")
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = ObsidianBg.copy(alpha = 0.96f),
        dragHandle = null,
        modifier = Modifier.testTag("icon_customizer_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(top = 16.dp, start = 20.dp, end = 20.dp, bottom = 24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Drag handle pill
            Box(
                modifier = Modifier
                    .size(width = 36.dp, height = 4.dp)
                    .background(Color.White.copy(alpha = 0.25f), CircleShape)
                    .align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Sheet Title & Close
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(theme.primary.copy(alpha = 0.18f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = theme.primary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "App Icon Design",
                            style = MaterialTheme.typography.titleLarge.copy(
                                color = TextPrimary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )
                        )
                        Text(
                            text = "Customize shapes, styles & theme",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = TextSecondary,
                                fontSize = 12.sp
                            )
                        )
                    }
                }

                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.size(36.dp).testTag("close_customizer_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = TextSecondary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // LIVE PREVIEW CONTAINER
            Text(
                text = "LIVE PREVIEW",
                style = MaterialTheme.typography.labelSmall.copy(
                    color = theme.primary,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            )
            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(FrostedSurface.copy(alpha = 0.7f))
                    .border(1.dp, FrostedGlassBorder, RoundedCornerShape(20.dp))
                    .padding(vertical = 16.dp, horizontal = 12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    sampleApps.forEach { app ->
                        AppIconItem(
                            app = app,
                            config = config,
                            showLabel = config.showLabels,
                            onLaunch = {},
                            onTogglePin = {},
                            onOpenDetails = {}
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(22.dp))

            // 1. ICON SHAPE SELECTOR
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.FormatShapes, contentDescription = null, tint = theme.primary, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "ICON SHAPE",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = TextPrimary,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 0.8.sp
                    )
                )
            }
            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconShape.values().forEach { shape ->
                    val isSelected = config.shape == shape
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(14.dp))
                            .background(
                                if (isSelected) theme.primary.copy(alpha = 0.22f) else FrostedSurfaceVariant.copy(alpha = 0.5f)
                            )
                            .border(
                                width = if (isSelected) 1.5.dp else 1.dp,
                                color = if (isSelected) theme.primary else FrostedGlassBorder,
                                shape = RoundedCornerShape(14.dp)
                            )
                            .clickable { onUpdateShape(shape) }
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                            .testTag("shape_chip_${shape.name.lowercase()}")
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            if (isSelected) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = theme.primary,
                                    modifier = Modifier.size(14.dp).padding(end = 4.dp)
                                )
                            }
                            Text(
                                text = shape.displayName,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = if (isSelected) theme.primary else TextPrimary,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    fontSize = 12.5.sp
                                )
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 2. ICON STYLE / EFFECT
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Palette, contentDescription = null, tint = theme.primary, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "VISUAL EFFECT & FINISH",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = TextPrimary,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 0.8.sp
                    )
                )
            }
            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconStyle.values().forEach { style ->
                    val isSelected = config.style == style
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(14.dp))
                            .background(
                                if (isSelected) theme.primary.copy(alpha = 0.22f) else FrostedSurfaceVariant.copy(alpha = 0.5f)
                            )
                            .border(
                                width = if (isSelected) 1.5.dp else 1.dp,
                                color = if (isSelected) theme.primary else FrostedGlassBorder,
                                shape = RoundedCornerShape(14.dp)
                            )
                            .clickable { onUpdateStyle(style) }
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                            .testTag("style_chip_${style.name.lowercase()}")
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            if (isSelected) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = theme.primary,
                                    modifier = Modifier.size(14.dp).padding(end = 4.dp)
                                )
                            }
                            Text(
                                text = style.displayName,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = if (isSelected) theme.primary else TextPrimary,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    fontSize = 12.5.sp
                                )
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 3. ICON SIZE
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.PhotoSizeSelectLarge, contentDescription = null, tint = theme.primary, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "ICON SCALE",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = TextPrimary,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 0.8.sp
                    )
                )
            }
            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconScale.values().forEach { scale ->
                    val isSelected = config.scale == scale
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(14.dp))
                            .background(
                                if (isSelected) theme.primary.copy(alpha = 0.22f) else FrostedSurfaceVariant.copy(alpha = 0.5f)
                            )
                            .border(
                                width = if (isSelected) 1.5.dp else 1.dp,
                                color = if (isSelected) theme.primary else FrostedGlassBorder,
                                shape = RoundedCornerShape(14.dp)
                            )
                            .clickable { onUpdateScale(scale) }
                            .padding(vertical = 10.dp)
                            .testTag("scale_chip_${scale.name.lowercase()}")
                    ) {
                        Text(
                            text = "${scale.displayName} (${scale.dpVal}dp)",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = if (isSelected) theme.primary else TextPrimary,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                fontSize = 12.sp
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 4. SHOW LABELS TOGGLE
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(FrostedSurfaceVariant.copy(alpha = 0.5f))
                    .border(1.dp, FrostedGlassBorder, RoundedCornerShape(16.dp))
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.TextFields, contentDescription = null, tint = theme.primary, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Show App Labels",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                color = TextPrimary,
                                fontWeight = FontWeight.Medium
                            )
                        )
                        Text(
                            text = "Display app names below icons",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = TextSecondary,
                                fontSize = 11.sp
                            )
                        )
                    }
                }

                Switch(
                    checked = config.showLabels,
                    onCheckedChange = onToggleLabels,
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.Black,
                        checkedTrackColor = theme.primary,
                        uncheckedThumbColor = TextSecondary,
                        uncheckedTrackColor = FrostedSurface
                    )
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 5. ACCENT COLOR THEME
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.ColorLens, contentDescription = null, tint = theme.primary, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "ACCENT COLOR THEME",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = TextPrimary,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 0.8.sp
                    )
                )
            }
            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                HudThemePreset.values().forEach { preset ->
                    val isSelected = config.themePresetName == preset.name
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(
                                if (isSelected) preset.primary.copy(alpha = 0.22f) else FrostedSurfaceVariant.copy(alpha = 0.5f)
                            )
                            .border(
                                width = if (isSelected) 1.5.dp else 1.dp,
                                color = if (isSelected) preset.primary else FrostedGlassBorder,
                                shape = RoundedCornerShape(16.dp)
                            )
                            .clickable { onUpdateTheme(preset.name) }
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                            .testTag("theme_preset_${preset.name.lowercase()}")
                    ) {
                        Box(
                            modifier = Modifier
                                .size(14.dp)
                                .background(preset.primary, CircleShape)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = preset.title,
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = if (isSelected) preset.primary else TextPrimary,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                fontSize = 12.sp
                            )
                        )
                    }
                }
            }
        }
    }
}
