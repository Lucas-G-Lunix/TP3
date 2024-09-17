package frgp.utn.edu.ar.tp3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import frgp.utn.edu.ar.tp3.ui.theme.TP3Theme

class SignUp : ComponentActivity() {

    private val signUpViewModel: SignUpViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val signUpViewModel: SignUpViewModel by viewModels()

        enableEdgeToEdge()
        setContent {
            TP3Theme {
                Scaffold(
                    topBar = {
                        TopBarSignUpPage()
                    },
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    SignUpPage(modifier = Modifier.padding(innerPadding), signUpViewModel)
                }
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun SignUpPagePreview() {
        TP3Theme {
            SignUpPage(Modifier, signUpViewModel)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarSignUpPage() {
    TopAppBar(
        title = { Text(text = "Parking Control Registro") },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Red,
            titleContentColor = Color.White
        )
    )
}
@Composable
fun Gap() {
    Spacer(modifier = Modifier.height(12.dp))
}
@Composable
fun SignUpPage(modifier: Modifier = Modifier, signUpViewModel: SignUpViewModel) {
    val name by signUpViewModel.name.observeAsState("")
    val mail by signUpViewModel.mail.observeAsState("")
    val pass by signUpViewModel.password.observeAsState("")
    val rpss by signUpViewModel.repeatPassword.observeAsState(initial = "")

    val nameError by signUpViewModel.nameError.observeAsState(signUpViewModel.ok())
    val mailError by signUpViewModel.mailError.observeAsState(signUpViewModel.ok())
    val passError by signUpViewModel.passwordError.observeAsState(signUpViewModel.ok())
    val rpssError by signUpViewModel.repeatPasswordError.observeAsState(signUpViewModel.ok())

    Column (
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row {
            Text(text = "Registro")
        }
        Gap()
        Row {
            TextField(
                value = name,
                onValueChange = { newName: String -> signUpViewModel.changeName(newName) },
                label = { Text("Nombre") },
                isError = nameError.error,
                supportingText = { Text(nameError.message) }
            )
        }
        Gap()
        Row {
            TextField(
                value = mail,
                onValueChange = { newValue: String -> signUpViewModel.changeMail(newValue) },
                label = { Text("Correo electrónico") },
                isError = mailError.error,
                supportingText = { Text(mailError.message) }
            )
        }
        Gap()
        Row {
            TextField(
                value = pass,
                onValueChange = { newValue: String -> signUpViewModel.changePassword(newValue) },
                label = { Text("Contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                isError = passError.error,
                supportingText = { Text(passError.message) }
            )
        }
        Gap()
        Row {
            TextField(
                value = rpss,
                onValueChange = { newValue: String -> signUpViewModel.changeRepeatPassword(newValue) },
                label = { Text("Repetir contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                isError = rpssError.error,
                supportingText = { Text(rpssError.message) }
            )
        }
        Gap()
        Row {
            Button(onClick = {}) {
                Text("Aceptar")
            }
        }
    }
}

