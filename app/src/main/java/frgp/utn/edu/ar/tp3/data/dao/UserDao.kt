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
    fun insert(vararg users: User)

    @Delete
    fun delete(user: User)

    @Query("SELECT * FROM users")
    fun getAll(): List<User>

    @Query("SELECT COUNT(*) > 0 FROM users WHERE users.mail = :mail")
    fun isMailAddressRegistered(mail: String): Boolean

    @Query("SELECT COUNT(*) > 0 FROM users WHERE users.username = :username")
    fun isUsernameAlreadyTaken(username: String): Boolean

}