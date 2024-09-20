package frgp.utn.edu.ar.tp3.data.logic

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.AndroidViewModel
import androidx.room.Room
import frgp.utn.edu.ar.tp3.data.ParkingDatabase
import frgp.utn.edu.ar.tp3.data.dao.UserDao
import frgp.utn.edu.ar.tp3.data.entity.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import java.util.prefs.Preferences

val Application.dataStore by preferencesDataStore(name = "auth")

class AuthManager(application: Application): AndroidViewModel(application) {

    private lateinit var db: ParkingDatabase
    private lateinit var dao: UserDao
    private val dataStore = application.dataStore
    private val CURRENT_USER = stringPreferencesKey("current")

    init {
        db = Room.databaseBuilder(application.applicationContext, ParkingDatabase::class.java, "parking").build()
        dao = db.userDao()
    }

    suspend fun login(username: String, password: String) {
        if(arrayOf(username, password).any { it.trim().isEmpty() })
            throw IllegalArgumentException("El nombre de usuario o la contraseña están vacíos. ")
        val isUserValid = dao.verifyUser(username, password)
        if(isUserValid) {
            dataStore.edit { it[CURRENT_USER] = username }
        } else {
            throw Exception("Credenciales incorrectas")
        }
    }

    suspend fun logout() {
        dataStore.edit { it[CURRENT_USER] = "" }
    }

    suspend fun getCurrentUser(): Flow<User?> {
        var username: String = ""
        dataStore.data.map {
            username = it[CURRENT_USER]?:""
        }
        if(username.trim().isEmpty()) return flow { emit(null) }
        else return dao.getUser(username)
    }

}