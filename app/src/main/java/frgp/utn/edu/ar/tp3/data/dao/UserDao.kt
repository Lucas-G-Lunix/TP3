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

    @Query("SELECT * from users WHERE username = :username")
    fun getUser(username: String): Flow<User>

    @Query("SELECT * from users")
    fun getAllUsers(): Flow<List<User>>

    @Query("SELECT COUNT(*) > 0 FROM users WHERE users.mail = :mail")
    suspend fun isMailAddressRegistered(mail: String): Boolean

    @Query("SELECT COUNT(*) > 0 FROM users WHERE users.username = :username")
    suspend fun isUsernameTaken(username: String): Boolean

}