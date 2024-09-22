package frgp.utn.edu.ar.tp3.component

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
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
fun ParkingCard (data: Parking, onUpdateRequest: () -> Unit) {
    var expanded by remember { mutableStateOf<Boolean>(false)  }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
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
                    .height(200.dp)
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
            ) {
                Text(
                    text = "Id: ${data.id}\nMatrícula: ${data.mat}\nTiempo: ${metoTime(data.duration)}",
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentSize(Alignment.Center),
                    textAlign = TextAlign.Center
                )
            }
        }
}