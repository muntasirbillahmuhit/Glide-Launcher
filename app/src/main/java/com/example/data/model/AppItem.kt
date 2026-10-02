package com.example.data.model

import android.graphics.drawable.Drawable

data class AppItem(
    val packageName: String,
    val label: String,
    val icon: Drawable? = null,
    val isPinned: Boolean = false,
    val category: String = "All"
)
