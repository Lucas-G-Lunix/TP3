package frgp.utn.edu.ar.tp3.component

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ExitToApp
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import frgp.utn.edu.ar.tp3.activity.login.Login
import frgp.utn.edu.ar.tp3.activity.main.MainView
import frgp.utn.edu.ar.tp3.activity.me.MyAccount
import frgp.utn.edu.ar.tp3.data.entity.User
import frgp.utn.edu.ar.tp3.data.logic.AuthManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

val DrawerMenuDefaultOptions = listOf(
    DrawerMenuItem(label = "Parqueos", id = "parking", icon = Icons.Default.Place, activity = MainView::class.java),
    DrawerMenuItem(label = "Mi cuenta", id = "myAccount", icon = Icons.Default.Person, activity = MyAccount::class.java)
)

@Composable
fun DrawerMenu(
    authManager: AuthManager,
    menuItems: List<DrawerMenuItem>,
    onMenuItemClick: (String) -> Unit,
    selected: String
) {
    val context: Context = LocalContext.current
    val us = remember { mutableStateOf<User?>(null) }
    var selectedMenuItem by remember { mutableStateOf<String?>(selected) }

    LaunchedEffect(key1 = Unit) {
        authManager.getCurrentUser().collect { user ->
            us.value = user
        }
    }

    val username: String = us.value?.username ?: ""
    val name: String = us.value?.name ?: "Sin sesión iniciada"
    val mail: String = us.value?.mail ?: ""

    ModalDrawerSheet {
        Column(
            modifier = Modifier
                .background(Color.Red)
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Image(imageVector = Icons.Default.AccountCircle, contentDescription = "userImage", modifier = Modifier
                .padding(16.dp)
                .size(60.dp)
            )
            Text(name, modifier = Modifier.padding(8.dp), color = Color.White)
            Text(mail, modifier = Modifier.padding(8.dp), color = Color.White)
        }
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            menuItems.forEach { item ->
                NavigationDrawerItem(
                    label = { Text(text = item.label) },
                    selected = selectedMenuItem == item.id,
                    onClick = {
                        selectedMenuItem = item.id
                        onMenuItemClick(item.label)
                        if (context is Activity) {
                            val intent = Intent(context, item.activity)
                            // intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                            context.startActivity(intent)
                        }
                    },
                    icon = { Icon(item.icon, contentDescription = item.label) }
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            NavigationDrawerItem(
                label = { Text("Cerrar sesión") },
                selected = false,
                onClick = {
                    CoroutineScope(Dispatchers.IO).launch {
                        authManager.logout()
                        withContext(Dispatchers.Main) {
                            if (context is Activity) {
                                val intent = Intent(context, Login::class.java).apply {
                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                }
                                context.startActivity(intent)
                            }
                        }
                    }
                },
                icon = { Icon(Icons.AutoMirrored.Rounded.ExitToApp, "logoutIcon", modifier = Modifier.padding(16.dp)) }
            )
        }
    }

}