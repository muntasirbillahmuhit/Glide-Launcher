package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.LauncherRepository
import com.example.data.model.AppItem
import com.example.data.model.IconDesignConfig
import com.example.data.model.IconScale
import com.example.data.model.IconShape
import com.example.data.model.IconStyle
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class LauncherScreen {
    HOME,
    DRAWER
}

class LauncherViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = LauncherRepository(application)

    private val _currentScreen = MutableStateFlow(LauncherScreen.HOME)
    val currentScreen: StateFlow<LauncherScreen> = _currentScreen.asStateFlow()

    private val _iconConfig = MutableStateFlow(repository.getIconConfig())
    val iconConfig: StateFlow<IconDesignConfig> = _iconConfig.asStateFlow()

    private val _allApps = MutableStateFlow<List<AppItem>>(
        listOf(
            AppItem("com.android.settings", "Settings", null, true, "Tools"),
            AppItem("com.android.chrome", "Chrome", null, true, "Internet"),
            AppItem("com.android.camera2", "Camera", null, true, "Media"),
            AppItem("com.android.calculator2", "Calculator", null, true, "Tools")
        )
    )
    val allApps: StateFlow<List<AppItem>> = _allApps.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow("All")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    val filteredApps: StateFlow<List<AppItem>> = combine(
        _allApps,
        _searchQuery,
        _selectedCategory
    ) { apps, query, category ->
        var list = apps.distinctBy { it.packageName }

        if (category != "All") {
            list = list.filter { it.category.equals(category, ignoreCase = true) }
        }

        if (query.isNotBlank()) {
            val q = query.trim().lowercase()
            list = list.filter {
                it.label.lowercase().contains(q) || it.packageName.lowercase().contains(q)
            }
        }

        list.sortedBy { it.label.lowercase() }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val pinnedApps: StateFlow<List<AppItem>> = _allApps
        .map { apps ->
            val distinct = apps.distinctBy { it.packageName }
            val pinned = distinct.filter { it.isPinned }
            if (pinned.isNotEmpty()) pinned else distinct.take(8)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val dockApps: StateFlow<List<AppItem>> = _allApps
        .map { apps ->
            val distinct = apps.distinctBy { it.packageName }
            val pinned = distinct.filter { it.isPinned }
            if (pinned.isNotEmpty()) pinned.take(4) else distinct.take(4)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        loadApps()
    }

    fun loadApps() {
        viewModelScope.launch {
            _allApps.value = repository.getInstalledApps()
        }
    }

    fun navigateTo(screen: LauncherScreen) {
        _currentScreen.value = screen
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setCategory(category: String) {
        _selectedCategory.value = category
    }

    fun launchApp(app: AppItem) {
        repository.launchApp(app.packageName)
    }

    fun openAppDetails(app: AppItem) {
        repository.openAppDetails(app.packageName)
    }

    fun togglePin(app: AppItem) {
        val isPinned = repository.togglePin(app.packageName)
        _allApps.value = _allApps.value.map {
            if (it.packageName == app.packageName) it.copy(isPinned = isPinned) else it
        }
    }

    fun updateIconShape(shape: IconShape) {
        val newConfig = _iconConfig.value.copy(shape = shape)
        _iconConfig.value = newConfig
        repository.saveIconConfig(newConfig)
    }

    fun updateIconStyle(style: IconStyle) {
        val newConfig = _iconConfig.value.copy(style = style)
        _iconConfig.value = newConfig
        repository.saveIconConfig(newConfig)
    }

    fun updateIconScale(scale: IconScale) {
        val newConfig = _iconConfig.value.copy(scale = scale)
        _iconConfig.value = newConfig
        repository.saveIconConfig(newConfig)
    }

    fun toggleShowLabels(show: Boolean) {
        val newConfig = _iconConfig.value.copy(showLabels = show)
        _iconConfig.value = newConfig
        repository.saveIconConfig(newConfig)
    }

    fun updateThemePreset(presetName: String) {
        val newConfig = _iconConfig.value.copy(themePresetName = presetName)
        _iconConfig.value = newConfig
        repository.saveIconConfig(newConfig)
    }
}
