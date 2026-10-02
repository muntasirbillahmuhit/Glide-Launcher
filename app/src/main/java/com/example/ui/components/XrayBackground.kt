package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.R
import com.example.ui.theme.ObsidianBg

@Composable
fun XrayBackground(
    modifier: Modifier = Modifier,
    showGrid: Boolean = true,
    showParticles: Boolean = false
) {
    Box(modifier = modifier.fillMaxSize().background(ObsidianBg)) {
        // High-res sleek wallpaper
        Image(
            painter = painterResource(id = R.drawable.wallpaper_glide),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .alpha(0.65f)
        )

        // Soft gradient overlay for contrast and depth
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            ObsidianBg.copy(alpha = 0.35f),
                            ObsidianBg.copy(alpha = 0.2f),
                            ObsidianBg.copy(alpha = 0.75f)
                        )
                    )
                )
        )
    }
}
