package frgp.utn.edu.ar.tp3.data.logic

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.AndroidViewModel
import androidx.room.Room
import frgp.utn.edu.ar.tp3.data.ParkingDatabase
import frgp.utn.edu.ar.tp3.data.dao.UserDao
import frgp.utn.edu.ar.tp3.data.dao.UserDao_Impl
import frgp.utn.edu.ar.tp3.data.entity.User
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import java.util.prefs.Preferences

val Application.dataStore by preferencesDataStore(name = "auth")

class AuthManager(application: Application): AndroidViewModel(application) {

    private var db: ParkingDatabase =
        Room.databaseBuilder(application.applicationContext, ParkingDatabase::class.java, "parking").fallbackToDestructiveMigration().build()
    private var dao: UserDao_Impl = db.userDao() as UserDao_Impl
    private val dataStore = application.dataStore
    private val CURRENT_USER = stringPreferencesKey("current")

    suspend fun login(username: String, password: String) {
        Log.d("Login", "Intentando iniciar sesión. Usuario: $username")
        if (arrayOf(username, password).any { it.trim().isEmpty() }) {
            Log.e("Login", "Error: El nombre de usuario o la contraseña están vacíos.")
            throw IllegalArgumentException("El nombre de usuario o la contraseña están vacíos.")
        }
        try {
            val isUserValid = dao.verifyUser(username, password)
            if (isUserValid) {
                Log.d("Login", "Credenciales correctas para el usuario: $username")
                dataStore.edit { it[CURRENT_USER] = username }
            } else {
                Log.e("Login", "Error: Credenciales incorrectas para el usuario: $username")
            }
        } catch (e: Exception) {
            Log.e("Login", "Error durante el proceso de inicio de sesión: ${e.message}")
            throw Exception("Error durante el proceso de inicio de sesión: ${e.message}")
        }
    }

    suspend fun logout() {
        dataStore.edit { it[CURRENT_USER] = "" }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun getCurrentUser(): Flow<User?> {
        return dataStore.data.map { preferences ->
            val username = preferences[CURRENT_USER] ?: ""
            if (username.trim().isEmpty()) {
                null
            } else {
                dao.getUser(username)
            }
        }.flatMapConcat { user ->
            user?: flowOf(null)
        }
    }

}