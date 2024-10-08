package frgp.utn.edu.ar.tp3.data

import androidx.room.Database
import androidx.room.RoomDatabase
import frgp.utn.edu.ar.tp3.data.dao.ParkingDao
import frgp.utn.edu.ar.tp3.data.dao.UserDao
import frgp.utn.edu.ar.tp3.data.entity.Parking
import frgp.utn.edu.ar.tp3.data.entity.User

@Database(entities = [User::class, Parking::class], version = 2)
abstract class ParkingDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun parkingDao(): ParkingDao
}