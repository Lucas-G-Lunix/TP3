package frgp.utn.edu.ar.tp3.activity.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import frgp.utn.edu.ar.tp3.component.AddParkingButton
import frgp.utn.edu.ar.tp3.component.DrawerMenu
import frgp.utn.edu.ar.tp3.component.DrawerMenuDefaultOptions
import frgp.utn.edu.ar.tp3.data.entity.User
import frgp.utn.edu.ar.tp3.data.logic.AuthManager
import frgp.utn.edu.ar.tp3.ui.theme.TP3Theme

class MainView : ComponentActivity() {
    private val authManager: AuthManager by viewModels()
    private val vm: MainViewModel by viewModels()
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
                    floatingActionButton = { AddParkingButton(authManager, vm.dao) }
                ) { innerPadding ->
                    MainViewPage(modifier = Modifier.padding(innerPadding), vm = vm)
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
    ModalNavigationDrawer(
        drawerContent = { DrawerMenu(authManager, DrawerMenuDefaultOptions, {}, "parking") }
    ) {
        TopAppBar(
            title = { Text(text = "Parking Control") },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Red,
                titleContentColor = Color.White
            )
        )
    }
}

fun mtoTime(minutes: Int): String {
    if(minutes < 60) return "$minutes m";
    else {
        val h: Int = (minutes / 60 - minutes % 60)
        if(h < 24) return "$h h, $minutes m";
        else {
            val d: Int = (h / 24 - h % 24)
            return "$d d, $h h, $minutes m"
        }
    }
}

@Composable
fun MainViewPage(modifier: Modifier = Modifier, vm: MainViewModel) {
    val items by vm.parkings.observeAsState(emptyList())
    if(items.isNotEmpty())
        LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(items) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp)
                ) {
                    Text(it.mat + "\n" + mtoTime(it.duration))
                }
            }
        }
    else
        Text("No hay parqueos. ")
}