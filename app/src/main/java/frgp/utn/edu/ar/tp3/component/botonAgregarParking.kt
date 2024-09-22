package frgp.utn.edu.ar.tp3.component
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import frgp.utn.edu.ar.tp3.data.dao.ParkingDao
import frgp.utn.edu.ar.tp3.data.entity.Parking
import frgp.utn.edu.ar.tp3.data.logic.AuthManager

@Composable
fun AddParkingButton(authManager: AuthManager, dao: ParkingDao) {
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }
    var matricula by remember { mutableStateOf("") }
    var tiempo by remember { mutableStateOf("") }
    var username by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        authManager.getCurrentUser().collect { user ->
            username = user?.username
        }
    }
    FloatingActionButton(
        onClick = { showDialog = true },
        containerColor = Color.Red,
        modifier = Modifier
            .wrapContentSize(align = Alignment.BottomEnd)
            .padding(16.dp),
        elevation = FloatingActionButtonDefaults.elevation(8.dp)
    ) {
        Icon(Icons.Filled.Add, contentDescription = "Add Parking", tint = Color.White)
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                Button(
                    onClick = {
                        if(username == null) return@Button
                        if (matricula.isNotEmpty() && tiempo.isNotEmpty()) {
                            val t = Toast.makeText(
                                context,
                                "Parking agregado para $username con matrícula $matricula por $tiempo minutos",
                                Toast.LENGTH_SHORT
                            )
                            CoroutineScope(Dispatchers.IO).launch {
                                dao.insert(Parking(user = username!!, mat = matricula, duration = tiempo.toInt()))
                                t.show()
                                showDialog = false
                            }
                        } else {
                            Toast.makeText(context, "Completa todos los campos", Toast.LENGTH_SHORT).show()
                        }
                    }
                ) {
                    Text("Agregar")
                }
            },
            dismissButton = {
                Button(onClick = { showDialog = false }) {
                    Icon(Icons.Filled.Close, contentDescription = "Cerrar diálogo")
                }
            },
            title = { Text("Agregar Parking") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = matricula,
                        onValueChange = { matricula = it },
                        label = { Text("Matrícula") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = tiempo,
                        onValueChange = { tiempo = it },
                        label = { Text("Tiempo (minutos)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        )
    }
}

