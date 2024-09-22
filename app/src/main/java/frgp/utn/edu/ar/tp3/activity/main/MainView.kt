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
import androidx.compose.foundation.layout.paddingFromBaseline
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
                ModalNavigationDrawer(
                    drawerContent = { DrawerMenu(authManager, DrawerMenuDefaultOptions, {}, "parking") }
                ) {
                    Scaffold(
                        topBar = {
                            TopBarMainView(authManager, vm)
                        },

                        modifier = Modifier.fillMaxSize(),
                        floatingActionButton = { AddParkingButton(authManager, vm.dao) }
                    ) {
                        MainViewPage(modifier = Modifier.padding(it), vm = vm)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarMainView(authManager: AuthManager, vm: MainViewModel) {
    var expanded by remember { mutableStateOf(false) }
    val us = remember {
        mutableStateOf<User?>(null)
    }
    LaunchedEffect(key1 = Unit) {
        authManager.getCurrentUser().collect { user ->
            us.value = user
        }
    }
    TopAppBar(
        title = { Text(text = "Parking Control") },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Red,
            titleContentColor = Color.White
        )
    )
}

fun mtoTime(minutes: Int): String {
    val days = minutes / 1440
    val remainingMinutesAfterDays = minutes % 1440
    val hours = remainingMinutesAfterDays / 60
    val remainingMinutes = remainingMinutesAfterDays % 60
    val parts = mutableListOf<String>()
    if (days > 0) parts.add("$days d")
    if (hours > 0) parts.add("$hours h")
    if (remainingMinutes > 0) parts.add("$remainingMinutes m")
    return if (parts.isEmpty()) "0 m" else parts.joinToString(", ")
}



@Composable
fun MainViewPage(modifier: Modifier = Modifier, vm: MainViewModel) {
    val data by vm.parkings.observeAsState(emptyList())
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(data) {
                println("Item: ${it.mat} - Duration: ${it.duration}")
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp)
                ) {
                    Text(
                        modifier = Modifier.padding(16.dp),
                        text = "${it.mat}\n${mtoTime(it.duration)}"
                    )
                }
            }
        }
    if(data.isEmpty())
        Text("No hay parqueos. ")
}