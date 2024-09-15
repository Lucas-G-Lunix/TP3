package frgp.utn.edu.ar.tp3.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import frgp.utn.edu.ar.tp3.data.Dao.UserDao
import frgp.utn.edu.ar.tp3.data.Entity.User

@Database(entities = [User::class], version = 1)
abstract class ParkingDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object {
        const val DATABASE_NAME = "parking_db"

        @Volatile
        private var Instance: ParkingDatabase? = null
        fun getDatabase(context: Context): ParkingDatabase {
            return (Instance ?: synchronized(this) {
                Room.databaseBuilder(
                        context,
                        ParkingDatabase::class.java,
                        DATABASE_NAME
                    ).fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            })
        }
    }
}