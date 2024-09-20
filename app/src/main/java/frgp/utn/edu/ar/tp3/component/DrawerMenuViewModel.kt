package frgp.utn.edu.ar.tp3.component

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class DrawerMenuViewModel: ViewModel() {
    private val _selectedItem = mutableStateOf("Parqueos")
    val selectedItem: State<String> = _selectedItem

    fun selectItem(label: String) {
        _selectedItem.value = label
    }
}