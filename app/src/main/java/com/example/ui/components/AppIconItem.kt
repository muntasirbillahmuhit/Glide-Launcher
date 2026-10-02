package com.example.ui.components

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppItem
import com.example.data.model.IconDesignConfig
import com.example.data.model.IconScale
import com.example.data.model.IconShape
import com.example.data.model.IconStyle
import com.example.ui.theme.FrostedGlassBorder
import com.example.ui.theme.FrostedSurface
import com.example.ui.theme.LocalHudPreset
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AppIconItem(
    app: AppItem,
    modifier: Modifier = Modifier,
    config: IconDesignConfig = IconDesignConfig(),
    iconSize: Dp? = null,
    showLabel: Boolean? = null,
    onLaunch: (AppItem) -> Unit,
    onTogglePin: (AppItem) -> Unit,
    onOpenDetails: (AppItem) -> Unit
) {
    val theme = LocalHudPreset.current
    var showMenu by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val effectiveSize = iconSize ?: config.scale.dpVal.dp
    val effectiveShowLabel = showLabel ?: config.showLabels

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.91f else 1f,
        animationSpec = spring(dampingRatio = 0.6f, stiffness = 420f),
        label = "icon_scale"
    )

    val iconBitmap = remember(app.icon) {
        app.icon?.let { drawableToBitmap(it) }
    }

    // Determine shape geometry
    val containerShape: Shape = remember(config.shape) {
        when (config.shape) {
            IconShape.SQUIRCLE -> RoundedCornerShape(18.dp)
            IconShape.CIRCLE -> CircleShape
            IconShape.TEARDROP -> RoundedCornerShape(topStart = 22.dp, topEnd = 22.dp, bottomEnd = 4.dp, bottomStart = 22.dp)
            IconShape.HEXAGON -> RoundedCornerShape(percent = 34)
            IconShape.ROUNDED -> RoundedCornerShape(10.dp)
        }
    }

    val innerShape: Shape = remember(config.shape) {
        when (config.shape) {
            IconShape.SQUIRCLE -> RoundedCornerShape(14.dp)
            IconShape.CIRCLE -> CircleShape
            IconShape.TEARDROP -> RoundedCornerShape(topStart = 18.dp, topEnd = 18.dp, bottomEnd = 4.dp, bottomStart = 18.dp)
            IconShape.HEXAGON -> RoundedCornerShape(percent = 30)
            IconShape.ROUNDED -> RoundedCornerShape(8.dp)
        }
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.testTag("app_item_${app.packageName.replace('.', '_')}")
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .scale(scale)
                .clip(RoundedCornerShape(16.dp))
                .combinedClickable(
                    interactionSource = interactionSource,
                    indication = null,
                    onClick = { onLaunch(app) },
                    onLongClick = { showMenu = true }
                )
                .padding(vertical = 4.dp, horizontal = 4.dp)
        ) {
            // Container with dynamic design style
            when (config.style) {
                IconStyle.STOCK_FLOATING -> {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(effectiveSize + 6.dp)
                            .shadow(
                                elevation = 8.dp,
                                shape = containerShape,
                                ambientColor = Color.Black.copy(alpha = 0.35f),
                                spotColor = Color.Black.copy(alpha = 0.5f)
                            )
                            .clip(containerShape)
                    ) {
                        RenderAppIcon(
                            bitmap = iconBitmap,
                            label = app.label,
                            size = effectiveSize,
                            shape = containerShape,
                            colorFilter = null,
                            fallbackTint = theme.primary
                        )
                        PinnedBadge(isPinned = app.isPinned, themeColor = theme.primary)
                    }
                }

                IconStyle.MONOCHROME -> {
                    val monoFilter = remember {
                        val matrix = ColorMatrix()
                        matrix.setToSaturation(0f)
                        ColorFilter.colorMatrix(matrix)
                    }
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(effectiveSize + 8.dp)
                            .shadow(
                                elevation = 6.dp,
                                shape = containerShape,
                                ambientColor = Color.Black.copy(alpha = 0.4f),
                                spotColor = Color.Black.copy(alpha = 0.6f)
                            )
                            .background(
                                brush = Brush.verticalGradient(
                                    colors = listOf(Color(0xFF1E2638), Color(0xFF111724))
                                ),
                                shape = containerShape
                            )
                            .border(
                                width = 1.dp,
                                color = theme.primary.copy(alpha = 0.45f),
                                shape = containerShape
                            )
                    ) {
                        RenderAppIcon(
                            bitmap = iconBitmap,
                            label = app.label,
                            size = effectiveSize,
                            shape = innerShape,
                            colorFilter = monoFilter,
                            fallbackTint = theme.primary
                        )
                        PinnedBadge(isPinned = app.isPinned, themeColor = theme.primary)
                    }
                }

                IconStyle.NEON_GLOW -> {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(effectiveSize + 8.dp)
                            .shadow(
                                elevation = 10.dp,
                                shape = containerShape,
                                ambientColor = theme.primary.copy(alpha = 0.35f),
                                spotColor = theme.primary.copy(alpha = 0.6f)
                            )
                            .background(
                                brush = Brush.radialGradient(
                                    colors = listOf(
                                        theme.primary.copy(alpha = 0.22f),
                                        FrostedSurface.copy(alpha = 0.95f)
                                    )
                                ),
                                shape = containerShape
                            )
                            .border(
                                width = 1.5.dp,
                                brush = Brush.linearGradient(
                                    colors = listOf(theme.primary, theme.secondary.copy(alpha = 0.7f))
                                ),
                                shape = containerShape
                            )
                    ) {
                        RenderAppIcon(
                            bitmap = iconBitmap,
                            label = app.label,
                            size = effectiveSize,
                            shape = innerShape,
                            colorFilter = null,
                            fallbackTint = theme.primary
                        )
                        PinnedBadge(isPinned = app.isPinned, themeColor = theme.primary)
                    }
                }

                IconStyle.DARK_OBSIDIAN -> {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(effectiveSize + 8.dp)
                            .shadow(
                                elevation = 8.dp,
                                shape = containerShape,
                                ambientColor = Color.Black.copy(alpha = 0.6f),
                                spotColor = Color.Black.copy(alpha = 0.8f)
                            )
                            .background(
                                brush = Brush.verticalGradient(
                                    colors = listOf(Color(0xFF191D28), Color(0xFF0A0C12))
                                ),
                                shape = containerShape
                            )
                            .border(
                                width = 1.dp,
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        Color.White.copy(alpha = 0.22f),
                                        Color.White.copy(alpha = 0.05f)
                                    )
                                ),
                                shape = containerShape
                            )
                    ) {
                        RenderAppIcon(
                            bitmap = iconBitmap,
                            label = app.label,
                            size = effectiveSize,
                            shape = innerShape,
                            colorFilter = null,
                            fallbackTint = theme.primary
                        )
                        PinnedBadge(isPinned = app.isPinned, themeColor = theme.primary)
                    }
                }

                IconStyle.FROSTED_GLASS -> {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(effectiveSize + 8.dp)
                            .shadow(
                                elevation = 6.dp,
                                shape = containerShape,
                                ambientColor = Color.Black.copy(alpha = 0.4f),
                                spotColor = Color.Black.copy(alpha = 0.6f)
                            )
                            .background(
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        FrostedSurface.copy(alpha = 0.85f),
                                        FrostedSurface.copy(alpha = 0.95f)
                                    )
                                ),
                                shape = containerShape
                            )
                            .border(
                                width = 1.dp,
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        Color.White.copy(alpha = 0.26f),
                                        FrostedGlassBorder
                                    )
                                ),
                                shape = containerShape
                            )
                    ) {
                        RenderAppIcon(
                            bitmap = iconBitmap,
                            label = app.label,
                            size = effectiveSize,
                            shape = innerShape,
                            colorFilter = null,
                            fallbackTint = theme.primary
                        )
                        PinnedBadge(isPinned = app.isPinned, themeColor = theme.primary)
                    }
                }
            }

            if (effectiveShowLabel) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = app.label,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = TextPrimary,
                        fontSize = when (config.scale) {
                            IconScale.COMPACT -> 11.sp
                            IconScale.STANDARD -> 12.sp
                            IconScale.LARGE -> 13.sp
                        },
                        fontWeight = FontWeight.Normal,
                        letterSpacing = 0.1.sp
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.width(effectiveSize + 22.dp)
                )
            }
        }

        // Modern Context Dropdown
        DropdownMenu(
            expanded = showMenu,
            onDismissRequest = { showMenu = false },
            modifier = Modifier
                .background(FrostedSurface, RoundedCornerShape(16.dp))
                .border(1.dp, FrostedGlassBorder, RoundedCornerShape(16.dp))
        ) {
            DropdownMenuItem(
                text = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.PlayArrow, contentDescription = null, tint = theme.primary, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(10.dp))
                        Text("Open", color = TextPrimary, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium))
                    }
                },
                onClick = {
                    showMenu = false
                    onLaunch(app)
                }
            )

            DropdownMenuItem(
                text = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.PushPin, contentDescription = null, tint = theme.secondary, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(if (app.isPinned) "Unpin from Home" else "Pin to Home", color = TextPrimary, style = MaterialTheme.typography.bodyMedium)
                    }
                },
                onClick = {
                    showMenu = false
                    onTogglePin(app)
                }
            )

            DropdownMenuItem(
                text = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Info, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(10.dp))
                        Text("App Info", color = TextSecondary, style = MaterialTheme.typography.bodyMedium)
                    }
                },
                onClick = {
                    showMenu = false
                    onOpenDetails(app)
                }
            )
        }
    }
}

@Composable
private fun RenderAppIcon(
    bitmap: Bitmap?,
    label: String,
    size: Dp,
    shape: Shape,
    colorFilter: ColorFilter?,
    fallbackTint: Color
) {
    if (bitmap != null) {
        Image(
            bitmap = bitmap.asImageBitmap(),
            contentDescription = label,
            colorFilter = colorFilter,
            modifier = Modifier
                .size(size)
                .clip(shape)
        )
    } else {
        Icon(
            imageVector = Icons.Default.Apps,
            contentDescription = label,
            tint = fallbackTint,
            modifier = Modifier.size(size * 0.65f)
        )
    }
}

@Composable
private fun androidx.compose.foundation.layout.BoxScope.PinnedBadge(
    isPinned: Boolean,
    themeColor: Color
) {
    if (isPinned) {
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(2.dp)
                .size(13.dp)
                .background(themeColor, shape = CircleShape)
                .border(1.dp, Color.Black.copy(alpha = 0.4f), shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.PushPin,
                contentDescription = "Pinned",
                tint = Color.Black,
                modifier = Modifier.size(8.dp)
            )
        }
    }
}

private fun drawableToBitmap(drawable: Drawable): Bitmap? {
    return try {
        if (drawable is BitmapDrawable && drawable.bitmap != null && !drawable.bitmap.isRecycled) {
            return drawable.bitmap
        }
        val width = if (drawable.intrinsicWidth > 0) drawable.intrinsicWidth.coerceIn(32, 256) else 96
        val height = if (drawable.intrinsicHeight > 0) drawable.intrinsicHeight.coerceIn(32, 256) else 96
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        bitmap
    } catch (t: Throwable) {
        null
    }
}
