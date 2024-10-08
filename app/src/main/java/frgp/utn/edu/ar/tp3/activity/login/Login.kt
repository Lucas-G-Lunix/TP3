package frgp.utn.edu.ar.tp3.activity.login

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import android.widget.Toast.makeText
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.room.Room
import frgp.utn.edu.ar.tp3.activity.main.MainView
import frgp.utn.edu.ar.tp3.R
import frgp.utn.edu.ar.tp3.activity.signup.SignUp
import frgp.utn.edu.ar.tp3.data.ParkingDatabase
import frgp.utn.edu.ar.tp3.data.dao.UserDao
import frgp.utn.edu.ar.tp3.data.logic.AuthManager
import frgp.utn.edu.ar.tp3.ui.theme.TP3Theme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class Login : ComponentActivity() {
    private lateinit var db: ParkingDatabase
    private lateinit var dao: UserDao

    private val authManager: AuthManager by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = Room.databaseBuilder(application.applicationContext, ParkingDatabase::class.java, "parking").fallbackToDestructiveMigration().build()
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
                        authManager
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
fun MainPage(modifier: Modifier = Modifier, authManager: AuthManager) {
    val context = LocalContext.current
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val handler = Handler(Looper.getMainLooper())
    LaunchedEffect(key1 = Unit) {
        authManager.getCurrentUser().collect {
            if(it != null) {
                handler.post {
                    makeText(context, "¡Bienvenido de vuelta!", Toast.LENGTH_SHORT).show()
                }
                val intent = Intent(context, MainView::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }
                context.startActivity(intent)
            }
        }
    }

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
            var showPassword by remember { mutableStateOf(value = false) }
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña") },
                visualTransformation = if (showPassword) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation()
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    if (showPassword) {
                        IconButton(onClick = { showPassword = false }) {
                            Icon(
                                imageVector = Icons.Filled.Visibility,
                                contentDescription = "hide_password"
                            )
                        }
                    } else {
                        IconButton(
                            onClick = { showPassword = true }) {
                            Icon(
                                imageVector = Icons.Filled.VisibilityOff,
                                contentDescription = "hide_password"
                            )
                        }
                    }
                }
            )
        }
        Row {
            Button(
                onClick = {
                    val handler = Handler(Looper.getMainLooper())
                    CoroutineScope(Dispatchers.IO).launch {
                        if(username.isNotBlank() && password.isNotBlank()) {
                            try {
                                authManager.login(username, password)
                                withContext(Dispatchers.Main) {
                                    val intent = Intent(context, MainView::class.java).apply {
                                        flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                    }
                                    context.startActivity(intent)
                                }
                            } catch(exception: IllegalArgumentException) {
                                handler.post {
                                    makeText(context, "Ingrese sus credenciales. ", Toast.LENGTH_SHORT).show()
                                }
                            } catch(exception: Exception) {
                                handler.post {
                                    makeText(context, exception.message, Toast.LENGTH_LONG).show()
                                }
                            }
                        }
                        else {
                            withContext(Dispatchers.Main) {
                                makeText(context, "Campos incompletos", Toast.LENGTH_SHORT).show()
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