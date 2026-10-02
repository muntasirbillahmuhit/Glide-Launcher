package com.example.data.model

enum class IconShape(val displayName: String) {
    SQUIRCLE("Squircle"),
    CIRCLE("Circle"),
    TEARDROP("Teardrop"),
    HEXAGON("Hexagon"),
    ROUNDED("Rounded")
}

enum class IconStyle(val displayName: String) {
    FROSTED_GLASS("Frosted Glass"),
    NEON_GLOW("Neon Glow"),
    MONOCHROME("Monochrome"),
    STOCK_FLOATING("Stock Floating"),
    DARK_OBSIDIAN("Dark Obsidian")
}

enum class IconScale(val displayName: String, val dpVal: Int) {
    COMPACT("Compact", 44),
    STANDARD("Standard", 52),
    LARGE("Large", 60)
}

data class IconDesignConfig(
    val shape: IconShape = IconShape.SQUIRCLE,
    val style: IconStyle = IconStyle.FROSTED_GLASS,
    val scale: IconScale = IconScale.STANDARD,
    val showLabels: Boolean = true,
    val themePresetName: String = "GLIDE_CYAN"
)
