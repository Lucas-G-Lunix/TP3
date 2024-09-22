package frgp.utn.edu.ar.tp3.activity.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.room.Room
import frgp.utn.edu.ar.tp3.data.ParkingDatabase
import frgp.utn.edu.ar.tp3.data.dao.ParkingDao

class MainViewModel(application: Application): AndroidViewModel(application) {

    private val db: ParkingDatabase = Room.databaseBuilder(application.applicationContext, ParkingDatabase::class.java, "parking").build()
    private val dao: ParkingDao = db.parkingDao()

    


}