package frgp.utn.edu.ar.tp3.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import frgp.utn.edu.ar.tp3.data.dao.UserDao
import frgp.utn.edu.ar.tp3.data.entity.User

@Database(entities = [User::class], version = 1, exportSchema = false)
abstract class ParkingDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: ParkingDatabase? = null

        fun getDatabase(context: Context): ParkingDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ParkingDatabase::class.java,
                    "parking_db"
                ).fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}