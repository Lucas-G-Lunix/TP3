package frgp.utn.edu.ar.tp3.data.Dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update
import frgp.utn.edu.ar.tp3.data.Entity.User

@Dao
interface UserDao {
    @Insert
    suspend fun add(user: User)
    @Update
    suspend fun update(user: User)
    @Delete
    suspend fun delete(user: User)
}