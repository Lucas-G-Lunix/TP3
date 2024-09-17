package frgp.utn.edu.ar.tp3.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import frgp.utn.edu.ar.tp3.data.entity.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert
    suspend fun add(user: User)
    @Update
    suspend fun update(user: User)
    @Delete
    suspend fun delete(user: User)

    @Query("SELECT * from users WHERE id = :id")
    fun getUser(id: Int): Flow<User>

    @Query("SELECT * from users ORDER BY id ASC")
    fun getAllUsers(): Flow<List<User>>

    @Query("SELECT COUNT(*) > 0 FROM users WHERE users.mail = :mail")
    fun isMailAddressRegistered(mail: String): Boolean

}