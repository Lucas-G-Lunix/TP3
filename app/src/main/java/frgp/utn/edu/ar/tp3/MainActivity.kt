package frgp.utn.edu.ar.tp3

import android.content.Intent
import android.os.Bundle
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import frgp.utn.edu.ar.tp3.ui.theme.TP3Theme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
                        modifier = Modifier.padding(innerPadding)
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
fun MainPage(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    Column (
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
            Icon(painter = painterResource(id = R.drawable.car_logo), contentDescription = "Car Logo", modifier = Modifier.width(80.dp))
        }
        Row {
            OutlinedTextField(value = "", onValueChange = {}, label = { Text("Nombre de Usuario") })
        }
        Row {
            OutlinedTextField(value = "", onValueChange = {}, label = { Text("Contraseña") })
        }
        Row {
            Button(onClick = {

            }, shape = MaterialTheme.shapes.medium, modifier = Modifier.width(150.dp)) {
                Text("Iniciar Sesión")
            }
        }
        Row {
            OutlinedButton(onClick = {
                val intent = Intent(context, SignUp::class.java)
                context.startActivity(intent)
            }, shape = MaterialTheme.shapes.medium, modifier = Modifier.width(150.dp)) {
                Text("Registrarse")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun MainPagePreview() {
    TP3Theme {
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
    }
}