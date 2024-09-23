package frgp.utn.edu.ar.tp3.activity.main

import android.app.Application
import android.content.ContextWrapper
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import frgp.utn.edu.ar.tp3.data.ParkingDatabase
import frgp.utn.edu.ar.tp3.data.dao.ParkingDao
import frgp.utn.edu.ar.tp3.data.entity.Parking
import frgp.utn.edu.ar.tp3.data.entity.User
import frgp.utn.edu.ar.tp3.data.logic.AuthManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.launch

class MainViewModel(application: Application): AndroidViewModel(application) {

    private val db: ParkingDatabase = Room.databaseBuilder(application.applicationContext, ParkingDatabase::class.java, "parking").fallbackToDestructiveMigration().build()
    val dao: ParkingDao = db.parkingDao()
    private val am: AuthManager = AuthManager(application)

    private val _parkings = MutableLiveData<List<Parking>>()
    val parkings: LiveData<List<Parking>> = _parkings

    fun update() {
        viewModelScope.launch {
            val currentUser = am.getCurrentUser().firstOrNull()
            if (currentUser != null) {
                val userParkings = dao.getByUsername(currentUser.username)
                Log.i(userParkings.firstOrNull().toString(), userParkings.firstOrNull()?.firstOrNull().toString())
                _parkings.value = userParkings.firstOrNull()
            } else {
                _parkings.value = emptyList()
            }
        }
    }

    fun del(data: Parking) {
        val t = Toast.makeText(this.getApplication<Application>().applicationContext, "Parking eliminado. ", Toast.LENGTH_SHORT)
        viewModelScope.launch {
            CoroutineScope(Dispatchers.IO).launch {
                dao.delete(data)
                update()
                t.show()
            }
        }
    }

    init {
        update()
    }



}
