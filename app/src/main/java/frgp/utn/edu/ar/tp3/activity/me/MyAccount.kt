package frgp.utn.edu.ar.tp3.activity.me

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
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
import androidx.core.content.ContextCompat
import androidx.room.Room
import frgp.utn.edu.ar.tp3.activity.login.Login
import frgp.utn.edu.ar.tp3.component.DrawerMenu
import frgp.utn.edu.ar.tp3.component.DrawerMenuDefaultOptions
import frgp.utn.edu.ar.tp3.data.ParkingDatabase
import frgp.utn.edu.ar.tp3.data.dao.UserDao
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
            Row {
                Button(onClick = {
                    us.value?.let {
                        if(deleteUser(it)) {
                            Toast.makeText(context, "Usuario ${us.value?.username} eliminado Correctamente!", Toast.LENGTH_SHORT).show()
                            val intent = Intent(context, Login::class.java).apply {
                                flags = Intent.FLAG_ACTIVITY_NEW_TASK
                            }
                            ContextCompat.startActivity(context, intent, null)
                        }
                        else Toast.makeText(context, "Error al eliminar usuario: ${us.value?.username} ", Toast.LENGTH_SHORT).show()
                    }
                }) {
                    Text("Borrar Usuario")
                }
            }
        }
    }

    fun deleteUser(user: User): Boolean{
        val db: ParkingDatabase = Room.databaseBuilder(application.applicationContext, ParkingDatabase::class.java, "parking")
            .allowMainThreadQueries().build()
        val dao: UserDao = db.userDao()
        try {
            dao.delete(user)
            return true
        } catch (e: Exception) {
            return false
        }
    }

}