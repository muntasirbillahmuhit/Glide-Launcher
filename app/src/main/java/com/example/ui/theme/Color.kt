package com.example.ui.theme

import androidx.compose.ui.graphics.Color

// Obsidian & Frosted Glass Palette for Glide Launcher
val ObsidianBg = Color(0xFF090D16)
val FrostedSurface = Color(0xFF131B2E)
val FrostedSurfaceVariant = Color(0xFF1C273E)
val FrostedGlassBorder = Color(0x33FFFFFF)
val FrostedGlassGlow = Color(0x2200E5FF)

val GlideAccent = Color(0xFF00E5FF)
val GlideSecondary = Color(0xFF8B5CF6)
val GlideEmerald = Color(0xFF10B981)
val GlideAmber = Color(0xFFF59E0B)

val TextPrimary = Color(0xFFF8FAFC)
val TextSecondary = Color(0xFF94A3B8)
val TextMuted = Color(0xFF64748B)

// Presets if user switches theme
enum class HudThemePreset(
    val title: String,
    val primary: Color,
    val primaryDim: Color,
    val secondary: Color,
    val accent: Color
) {
    GLIDE_CYAN("Glide Cyan", GlideAccent, Color(0x2200E5FF), GlideSecondary, GlideEmerald),
    VIOLET_DREAM("Violet Dream", Color(0xFFA855F7), Color(0x22A855F7), Color(0xFFEC4899), GlideAccent),
    EMERALD_MINT("Emerald Mint", Color(0xFF10B981), Color(0x2210B981), Color(0xFF06B6D4), Color(0xFFF59E0B)),
    SUNSET_AMBER("Sunset Amber", Color(0xFFF59E0B), Color(0x22F59E0B), Color(0xFFEF4444), Color(0xFF8B5CF6))
}
