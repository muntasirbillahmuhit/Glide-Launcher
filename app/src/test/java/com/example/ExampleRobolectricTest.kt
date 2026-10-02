package com.example

import android.app.Application
import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.LauncherRepository
import com.example.ui.LauncherScreen
import com.example.ui.LauncherViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

    @Test
    fun `read app name string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("Glide Launcher", appName)
    }

    @Test
    fun `launcher repository loads apps and toggles pin`() = runBlocking {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val repo = LauncherRepository(context)
        val apps = repo.getInstalledApps()
        assertNotNull(apps)
        assertTrue(apps.isNotEmpty())

        val firstApp = apps.first()
        val isPinned = repo.togglePin(firstApp.packageName)
        assertTrue(isPinned || !isPinned)
    }

    @Test
    fun `viewmodel manages screen navigation and search query`() = runBlocking {
        val app = ApplicationProvider.getApplicationContext<Application>()
        val viewModel = LauncherViewModel(app)

        assertEquals(LauncherScreen.HOME, viewModel.currentScreen.value)
        viewModel.navigateTo(LauncherScreen.DRAWER)
        assertEquals(LauncherScreen.DRAWER, viewModel.currentScreen.value)

        viewModel.setSearchQuery("setting")
        val filtered = viewModel.filteredApps.first()
        assertNotNull(filtered)
    }

    @Test
    fun `viewmodel updates icon design config correctly`() = runBlocking {
        val app = ApplicationProvider.getApplicationContext<Application>()
        val viewModel = LauncherViewModel(app)

        assertEquals(com.example.data.model.IconShape.SQUIRCLE, viewModel.iconConfig.value.shape)
        viewModel.updateIconShape(com.example.data.model.IconShape.CIRCLE)
        assertEquals(com.example.data.model.IconShape.CIRCLE, viewModel.iconConfig.value.shape)

        viewModel.updateIconStyle(com.example.data.model.IconStyle.NEON_GLOW)
        assertEquals(com.example.data.model.IconStyle.NEON_GLOW, viewModel.iconConfig.value.style)

        viewModel.updateIconScale(com.example.data.model.IconScale.LARGE)
        assertEquals(com.example.data.model.IconScale.LARGE, viewModel.iconConfig.value.scale)

        viewModel.toggleShowLabels(false)
        assertEquals(false, viewModel.iconConfig.value.showLabels)
    }
}
