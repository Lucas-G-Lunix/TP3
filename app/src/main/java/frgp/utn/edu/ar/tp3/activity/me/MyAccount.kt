package frgp.utn.edu.ar.tp3.activity.me

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import frgp.utn.edu.ar.tp3.component.DrawerMenu
import frgp.utn.edu.ar.tp3.component.DrawerMenuDefaultOptions
import frgp.utn.edu.ar.tp3.data.entity.User
import frgp.utn.edu.ar.tp3.data.logic.AuthManager
import frgp.utn.edu.ar.tp3.ui.theme.TP3Theme

class MyAccount: ComponentActivity()  {
    private val authManager: AuthManager by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TP3Theme {
                ModalNavigationDrawer(
                    drawerContent = { DrawerMenu(authManager, DrawerMenuDefaultOptions, {}, "myAccount") }
                ) {
                Scaffold(
                    topBar = { TopBar() },
                    modifier = Modifier.fillMaxSize(),
                ) {innerPadding ->
                    MyAccountView(modifier = Modifier.padding(innerPadding), authManager)
                }
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun TopBar() {
    TopAppBar(
        title = { Text(text = "Parking Control") },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Red,
            titleContentColor = Color.White
        )
    )
}

@Composable
fun MyAccountView(modifier: Modifier, authManager: AuthManager) {
    val context = LocalContext.current
    val us = remember {
        mutableStateOf<User?>(null)
    }
    LaunchedEffect(key1 = Unit) {
        authManager.getCurrentUser().collect { user ->
            us.value = user
        }
    }
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .padding(top = 32.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row {
            Text("Nombre: ${us.value?.name}", fontSize = 20.sp)
        }
        Row {
            Text("Nombre de Usuario: ${us.value?.username}", fontSize = 20.sp)
        }
        Row {
            Text("Mail: ${us.value?.mail}", fontSize = 20.sp)
        }
    }
    }
}