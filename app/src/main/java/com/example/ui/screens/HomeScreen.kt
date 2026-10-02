package com.example.ui.screens

import android.content.Intent
import android.speech.RecognizerIntent
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
import com.example.ui.theme.GlideAmber
import com.example.ui.theme.LocalHudPreset
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun HomeScreen(
    pinnedApps: List<AppItem>,
    config: IconDesignConfig = IconDesignConfig(),
    onOpenCustomizer: () -> Unit = {},
    onNavigate: (LauncherScreen) -> Unit,
    onLaunchApp: (AppItem) -> Unit,
    onTogglePin: (AppItem) -> Unit,
    onOpenDetails: (AppItem) -> Unit,
    modifier: Modifier = Modifier
) {
    val theme = LocalHudPreset.current
    val context = LocalContext.current

    // Launcher home screen consumes back navigation
    BackHandler(enabled = true) {
        // Already on home screen
    }

    var currentTime by remember {
        mutableStateOf(SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date()))
    }
    var currentDate by remember {
        mutableStateOf(SimpleDateFormat("EEEE, MMMM d", Locale.getDefault()).format(Date()))
    }
    var greeting by remember {
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        mutableStateOf(
            when (hour) {
                in 5..11 -> "Good morning"
                in 12..16 -> "Good afternoon"
                in 17..21 -> "Good evening"
                else -> "Good night"
            }
        )
    }

    LaunchedEffect(Unit) {
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val dateFormat = SimpleDateFormat("EEEE, MMMM d", Locale.getDefault())
        while (true) {
            val now = Date()
            currentTime = timeFormat.format(now)
            currentDate = dateFormat.format(now)

            val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
            greeting = when (hour) {
                in 5..11 -> "Good morning"
                in 12..16 -> "Good afternoon"
                in 17..21 -> "Good evening"
                else -> "Good night"
            }
            delay(1000L)
        }
    }

    val infiniteTransition = rememberInfiniteTransition(label = "chevron_anim")
    val chevronOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = -6f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "chevron_bounce"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(horizontal = 20.dp)
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        // Top Clock & Weather Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Column {
                Text(
                    text = if (currentTime.isNotBlank()) currentTime else "12:00",
                    style = MaterialTheme.typography.displayLarge.copy(
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 58.sp,
                        letterSpacing = (-1.5).sp,
                        lineHeight = 60.sp
                    )
                )

                Text(
                    text = if (currentDate.isNotBlank()) currentDate else "Monday, September 25",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = theme.primary,
                        fontWeight = FontWeight.Medium,
                        fontSize = 15.sp,
                        letterSpacing = 0.2.sp
                    )
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                // Icon Style Customizer Pill
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(FrostedSurface.copy(alpha = 0.85f))
                        .border(1.dp, FrostedGlassBorder, RoundedCornerShape(20.dp))
                        .clickable { onOpenCustomizer() }
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                        .testTag("open_icon_customizer_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = "Customize Icon Design",
                        tint = theme.primary,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = "Icons",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = TextPrimary,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 11.5.sp
                        )
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Weather Pill
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(FrostedSurface.copy(alpha = 0.8f))
                        .border(1.dp, FrostedGlassBorder, RoundedCornerShape(20.dp))
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.WbSunny,
                        contentDescription = null,
                        tint = GlideAmber,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "22°C",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = TextPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Greeting Prompt
        Text(
            text = greeting,
            style = MaterialTheme.typography.titleMedium.copy(
                color = TextSecondary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal
            )
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Glassmorphic Search Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = 10.dp,
                    shape = RoundedCornerShape(28.dp),
                    ambientColor = Color.Black.copy(alpha = 0.3f),
                    spotColor = Color.Black.copy(alpha = 0.5f)
                )
                .clip(RoundedCornerShape(28.dp))
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            FrostedSurface.copy(alpha = 0.85f),
                            FrostedSurface.copy(alpha = 0.95f)
                        )
                    )
                )
                .border(
                    width = 1.dp,
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.28f),
                            FrostedGlassBorder
                        )
                    ),
                    shape = RoundedCornerShape(28.dp)
                )
                .clickable { onNavigate(LauncherScreen.DRAWER) }
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .testTag("home_search_bar_trigger"),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(theme.primary.copy(alpha = 0.15f), shape = CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = theme.primary,
                        modifier = Modifier.size(18.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Search apps & web...",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = TextSecondary,
                        fontSize = 13.5.sp
                    )
                )
            }

            IconButton(
                onClick = {
                    try {
                        val voiceIntent = Intent(RecognizerIntent.ACTION_WEB_SEARCH).apply {
                            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        }
                        context.startActivity(voiceIntent)
                    } catch (e: Exception) {
                        onNavigate(LauncherScreen.DRAWER)
                    }
                },
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Mic,
                    contentDescription = "Voice Search",
                    tint = TextSecondary,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Pinned / Favorite Apps Grid
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                contentPadding = PaddingValues(top = 4.dp, bottom = 96.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(pinnedApps, key = { "${it.packageName}_${it.label}" }) { app ->
                    AppIconItem(
                        app = app,
                        config = config,
                        onLaunch = onLaunchApp,
                        onTogglePin = onTogglePin,
                        onOpenDetails = onOpenDetails
                    )
                }
            }

            // Subtle Swipe Up Gesture Indicator leading to Drawer
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 88.dp)
                    .clickable { onNavigate(LauncherScreen.DRAWER) },
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowUp,
                    contentDescription = "Open App Drawer",
                    tint = theme.primary.copy(alpha = 0.8f),
                    modifier = Modifier
                        .size(20.dp)
                        .offset(y = chevronOffset.dp)
                )
            }
        }
    }
}
