package frgp.utn.edu.ar.tp3.component

import android.app.Activity
import androidx.compose.ui.graphics.vector.ImageVector

data class DrawerMenuItem(
    val label: String,
    val id: String,
    val activity: Class<out Activity>,
    val icon: ImageVector
)