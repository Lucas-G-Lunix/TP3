package frgp.utn.edu.ar.tp3.activity.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import frgp.utn.edu.ar.tp3.data.entity.User
import frgp.utn.edu.ar.tp3.data.logic.AuthManager
import frgp.utn.edu.ar.tp3.ui.theme.TP3Theme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MainView : ComponentActivity() {
    private val authManager: AuthManager by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TP3Theme {
                Scaffold(
                    topBar = {
                        TopBarMainView(authManager)
                    },
                    modifier = Modifier.fillMaxSize(),
                ) { innerPadding ->
                    MainViewPage(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarMainView(authManager: AuthManager) {
    var expanded by remember { mutableStateOf(false) }
    val us = remember {
        mutableStateOf<User?>(null)
    }
    LaunchedEffect(key1 = Unit) {
        authManager.getCurrentUser().collect { user ->
            us.value = user
        }
    }
    val username: String = us.value?.username?:""
    val name: String = us.value?.name?:"Sin sesión iniciada"
    val mail: String = us.value?.mail?:""
    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet {
                Column(
                    modifier = Modifier
                        .background(Color.Red)
                        .fillMaxWidth()
                ) {
                    Image(imageVector = Icons.Default.AccountCircle, contentDescription = "userImage", modifier = Modifier
                        .padding(16.dp)
                        .size(60.dp))
                    Text(name, modifier = Modifier.padding(8.dp), color = Color.White)
                    Text(mail, modifier = Modifier.padding(8.dp), color = Color.White)
                }
                HorizontalDivider()
                NavigationDrawerItem(
                    label = { Text(text = "Parqueos") },
                    selected = false,
                    onClick = { /*TODO*/ },
                    icon = { Icon(Icons.Default.Place, "parkingIcon", modifier = Modifier.padding(16.dp)) }
                )
                NavigationDrawerItem(
                    label = { Text(text = "Mi cuenta") },
                    selected = false,
                    onClick = { /*TODO*/ },
                    icon = { Icon(Icons.Default.Person, "accountIcon", modifier = Modifier.padding(16.dp)) }
                )
            }
        }
    ) {
        TopAppBar(
            title = { Text(text = "Parking Control") },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Red,
                titleContentColor = Color.White
            ),
            actions = {
                IconButton(onClick = { expanded = true }) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "Opciones",
                        tint = Color.White
                    )
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Cerrar sesion") },
                        onClick = {
                            expanded = false
                        }
                    )
                }
            }
        )
    }
}

@Composable
fun MainViewPage(modifier: Modifier = Modifier) {

}