package frgp.utn.edu.ar.tp3.data.logic;

import android.content.Context
import frgp.utn.edu.ar.tp3.data.ParkingDatabase
import frgp.utn.edu.ar.tp3.data.dao.UserDao
import frgp.utn.edu.ar.tp3.data.entity.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class UserLogicImpl(context: Context) {
    private val userDao: UserDao

    init {
        val database = ParkingDatabase.getDatabase(context)
        userDao = database.userDao()
    }

    fun addUser(user: User) {
        CoroutineScope(Dispatchers.IO).launch {
            userDao.add(user)
        }
    }

    fun updateUser(user: User) {
        CoroutineScope(Dispatchers.IO).launch {
            userDao.update(user)
        }
    }

    fun deleteUser(user: User) {
        CoroutineScope(Dispatchers.IO).launch {
            userDao.delete(user)
        }
    }

    fun getUserById(username: String): Flow<User> {
        return userDao.getUser(username)
    }

    fun getAllUsers(): Flow<List<User>> {
        return userDao.getAllUsers()
    }

    fun isMailRegistered(mail: String): Boolean {
        return userDao.isMailAddressRegistered(mail)
    }

    fun isUsernameTaken(username: String): Boolean {
        return userDao.isUsernameTaken(username)
    }

}