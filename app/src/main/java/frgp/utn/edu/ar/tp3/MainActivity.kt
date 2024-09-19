package frgp.utn.edu.ar.tp3

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.room.Room
import frgp.utn.edu.ar.tp3.data.ParkingDatabase
import frgp.utn.edu.ar.tp3.data.dao.UserDao
import frgp.utn.edu.ar.tp3.ui.theme.TP3Theme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainActivity : ComponentActivity() {
    private lateinit var db: ParkingDatabase
    private lateinit var dao: UserDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = Room.databaseBuilder(application.applicationContext, ParkingDatabase::class.java, "parking").build()
        dao = db.userDao()

        enableEdgeToEdge()
        setContent {
            TP3Theme {
                Scaffold(
                    topBar = {
                        TopBarMainPage()
                    },
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    MainPage(
                        modifier = Modifier.padding(innerPadding),
                        dao
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarMainPage() {
    TopAppBar(
        title = { Text(text = "Parking Control") },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Red,
            titleContentColor = Color.White
        )
    )
}

@Composable
fun MainPage(modifier: Modifier = Modifier, dao: UserDao) {
    val context = LocalContext.current
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .padding(top = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row {
            Text("Bienvenido a Parking System", fontSize = 26.sp)
        }
        Row {
            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                color = MaterialTheme.colorScheme.primary
            )
        }
        Row {
            Icon(
                painter = painterResource(id = R.drawable.car_logo),
                contentDescription = "Car Logo",
                modifier = Modifier.width(80.dp)
            )
        }
        Row {
            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Nombre de Usuario") }
            )
        }
        Row {
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña") }
            )
        }
        Row {
            Button(
                onClick = {
                    CoroutineScope(Dispatchers.IO).launch {
                        if(username.isNotBlank() && password.isNotBlank()) {
                            val isUserValid = dao.verifyUser(username, password)
                            withContext(Dispatchers.Main) {
                                if (isUserValid) {
                                    val intent = Intent(context, MainView::class.java)
                                    context.startActivity(intent)
                                } else {
                                    Toast.makeText(context, "Credenciales invalidas", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                        else {
                            withContext(Dispatchers.Main) {
                                Toast.makeText(context, "Campos incompletos", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                },
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.width(150.dp)
            ) {
                Text("Iniciar Sesión")
            }
        }
        Row {
            OutlinedButton(
                onClick = {
                    val intent = Intent(context, SignUp::class.java)
                    context.startActivity(intent)
                },
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.width(150.dp)
            ) {
                Text("Registrarse")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun MainPagePreview() {
    /*TP3Theme {
        Scaffold(
            topBar = {
                TopBarMainPage()
            },
            modifier = Modifier.fillMaxSize()
        ) { innerPadding ->
            MainPage(
                modifier = Modifier.padding(innerPadding)
            )
        }
    }*/
}