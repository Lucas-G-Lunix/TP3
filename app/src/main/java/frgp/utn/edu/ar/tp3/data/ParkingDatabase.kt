package frgp.utn.edu.ar.tp3.data

import androidx.room.Database
import androidx.room.RoomDatabase
import frgp.utn.edu.ar.tp3.data.dao.UserDao
import frgp.utn.edu.ar.tp3.data.entity.User

@Database(entities = [User::class], version = 1)
abstract class ParkingDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}