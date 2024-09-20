package frgp.utn.edu.ar.tp3.activity.me

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import frgp.utn.edu.ar.tp3.component.DrawerMenu
import frgp.utn.edu.ar.tp3.component.DrawerMenuDefaultOptions
import frgp.utn.edu.ar.tp3.data.logic.AuthManager
import frgp.utn.edu.ar.tp3.ui.theme.TP3Theme

class MyAccount: ComponentActivity()  {
    private val authManager: AuthManager by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TP3Theme {
                Scaffold(
                    topBar = { DrawerComp(Modifier, authManager) },
                    modifier = Modifier.fillMaxSize(),
                ) { innerPadding ->
                    MyAccountView(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun DrawerComp(modifier: Modifier, authManager: AuthManager) {
    ModalNavigationDrawer(
        drawerContent = { DrawerMenu(authManager, DrawerMenuDefaultOptions, {}, "myAccount") }
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

@Composable
fun MyAccountView(modifier: Modifier) {

    Text("Hallo!")
}