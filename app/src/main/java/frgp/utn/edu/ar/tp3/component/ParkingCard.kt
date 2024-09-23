package frgp.utn.edu.ar.tp3.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import frgp.utn.edu.ar.tp3.data.entity.Parking

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

fun metoTime(minutes: Int): String {
    val days = minutes / 1440
    val remainingMinutesAfterDays = minutes % 1440
    val hours = remainingMinutesAfterDays / 60
    val remainingMinutes = remainingMinutesAfterDays % 60
    val parts = mutableListOf<String>()
    if (days > 0) parts.add("$days días")
    if (hours > 0) parts.add("$hours horas")
    if (remainingMinutes > 0) parts.add("$remainingMinutes minutos")
    return if (parts.isEmpty()) "Sin duración" else parts.joinToString(", ")
}

@Composable
fun ParkingCard (data: Parking, onUpdateRequest: () -> Unit, onDeleteRequest: () -> Unit) {
    var expanded by remember { mutableStateOf<Boolean>(false)  }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        onClick = {
            expanded = true
        }
    ) {
        Text(
            modifier = Modifier.padding(16.dp),
            text = "${data.mat}\n${mtoTime(data.duration)}"
        )
    }

    if(expanded)
        Dialog(onDismissRequest = { expanded = false }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(320.dp)
                    .padding(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(12.dp),
            ) {
                Column(
                    modifier = Modifier.fillMaxSize().padding(8.dp)
                ) {
                    Text(
                        text = "Parking N.º ${data.id}",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .wrapContentSize(Alignment.Center),
                        textAlign = TextAlign.Center
                    )
                    ListItem(
                        headlineContent = { Text(data.mat) },
                        supportingContent = { Text("Matrícula") }
                    )
                    ListItem(
                        headlineContent = { Text(metoTime(data.duration)) },
                        supportingContent = { Text("Tiempo") }
                    )
                    TextButton(onClick = { onDeleteRequest() }) {
                        Text("Eliminar")
                    }
                }
            }
        }
}