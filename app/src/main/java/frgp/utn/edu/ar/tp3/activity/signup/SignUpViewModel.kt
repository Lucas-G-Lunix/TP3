package frgp.utn.edu.ar.tp3.activity.signup

import android.app.Application
import android.content.Intent
import android.widget.Toast
import android.widget.Toast.makeText
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import frgp.utn.edu.ar.tp3.activity.login.Login
import frgp.utn.edu.ar.tp3.data.ParkingDatabase
import frgp.utn.edu.ar.tp3.data.dao.UserDao
import frgp.utn.edu.ar.tp3.data.entity.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DataError {
    private var _message: String = ""
    private var _error: Boolean = false
    constructor(message: String) {
        this._message = message
        this._error = true
    }
    constructor(message: String, error: Boolean) {
        this._message = message
        this._error = error
    }
    val message: String get() = _message
    val error: Boolean get() = _error
}

class SignUpViewModel(application: Application): AndroidViewModel(application) {

    private val db: ParkingDatabase = Room.databaseBuilder(application.applicationContext, ParkingDatabase::class.java, "parking").build()
    private val dao: UserDao = db.userDao()

    private val _name = MutableLiveData("")
    private val _nameError = MutableLiveData(ok())
    private val _mail = MutableLiveData("")
    private val _mailError = MutableLiveData(ok())
    private val _password = MutableLiveData("")
    private val _passwordError = MutableLiveData(ok())
    private val _repeatPassword = MutableLiveData("")
    private val _repeatPasswordError = MutableLiveData(ok())
    private val _username = MutableLiveData("")
    private val _usernameError = MutableLiveData(ok())
    private val _general = MutableLiveData<Boolean>(false)

    val name: LiveData<String> get() = _name
    val nameError: LiveData<DataError> get() = _nameError
    val mail: LiveData<String> get() = _mail
    val mailError: LiveData<DataError> get() = _mailError
    val password: LiveData<String> get() = _password
    val passwordError: LiveData<DataError> get() = _passwordError
    val repeatPassword: LiveData<String> get() = _repeatPassword
    val repeatPasswordError: LiveData<DataError> get() = _repeatPasswordError
    val username: LiveData<String> get() = _username
    val usernameError: LiveData<DataError> get() = _usernameError
    val general: LiveData<Boolean> get() = _general

    init {
        _username.value = "maximo"
        _name.value = "Máximo"
        _mail.value = "maximo@canedo.com.ar"
        _password.value = "Abc.1234"
        _repeatPassword.value = "Abc.1234"
    }

    fun err(message: String): DataError {
        return DataError(message)
    }
    fun ok(): DataError {
        return DataError("", false)
    }

    fun validateName() {
        if(name.value?.isBlank() == true) {
            _nameError.value = err("Ingrese un nombre")
        }
        else {
            _nameError.value = ok()
        }
    }
    fun validateMail() {
        if(!Regex(".+@.+\\..+").containsMatchIn(mail.value?:""))
            _mailError.value = err("Ingrese una dirección de correo electrónico válida. ")
        else {
            postValidateMail()
        }
    }
    fun postValidateMail() {
        CoroutineScope(Dispatchers.IO).launch {
            if(dao.isMailAddressRegistered(mail.value?:""))
                _mailError.postValue(err("El correo electrónico ya está registrado. "))
            else _mailError.postValue(ok())
        }
    }
    fun validatePassword() {
        if(!Regex(".*[A-Z].*").containsMatchIn(password.value ?: "")) {
            _passwordError.value = err("Ingrese al menos una mayúscula. ")
        }
        else if(!Regex(".*[a-z].*").containsMatchIn(password.value ?: "")) {
            _passwordError.value = err("Ingrese al menos una minúscula. ")
        }
        else if(!Regex(".*[0-9].*").containsMatchIn(password.value?:"")) {
            _passwordError.value = err("Ingrese al menos un dígito. ")
        }
        else if(!Regex(".*[^a-zA-Z0-9].*").containsMatchIn(password.value?:"")) {
            _passwordError.value = err("Ingrese al menos un caracter especial. ")
        }
        else if((password.value?:"").trim().length < 8)
            _passwordError.value = err("La contraseña debe contener al menos ocho caracteres. ")
        else
            _passwordError.value = ok()
    }

    fun validateUsername() {
        if(!Regex("^[a-zA-Z]").containsMatchIn(username.value?:""))
            _usernameError.value = err("Debe comenzar con una letra. ")
        else if(!Regex("^[a-zA-Z][a-zA-Z0-9.-_]{0,300}").containsMatchIn(username.value?:""))
            _usernameError.value = err("Debe contener sólo letras, números, guiones y/o puntos.")
        else if(!Regex("^[a-zA-Z][a-zA-Z0-9.-_]{2,30}").containsMatchIn(username.value?:""))
            _usernameError.value = err("Debe tener entre 2 y 30 caracteres. ")
        else
            _usernameError.value = ok()
        CoroutineScope(Dispatchers.IO).launch {
            if(dao.isUsernameAlreadyTaken(username.value?:""))
                _usernameError.postValue(err("Nombre de usuario no disponible. "))
            else
                _usernameError.postValue(ok())
        }
    }

    fun validateRepeatedPassword() {
        if((repeatPassword.value?:"").equals(password.value)) _repeatPasswordError.value = ok()
        else _repeatPasswordError.value = err("Las contraseñas no coinciden. ")
    }

    fun changeName(newName: String) {
        _name.value = newName
        validateName()
        checkGen()
    }
    fun changeMail(newMail: String) {
        _mail.value = newMail
        validateMail()
        checkGen()
    }
    fun changePassword(newPassword: String) {
        _password.value = newPassword
        validatePassword()
        checkGen()
    }
    fun changeRepeatPassword(newPassword: String) {
        _repeatPassword.value = newPassword
        validateRepeatedPassword()
        checkGen()
    }
    fun changeUsername(newUsername: String) {
        _username.value = newUsername
        validateUsername()
        checkGen()
    }

    fun checkGen(): Boolean {
        if(booleanArrayOf(
                (usernameError.value?:ok()).error,
                (nameError.value?:ok()).error,
                (mailError.value?:ok()).error,
                (passwordError.value?:ok()).error,
                (repeatPasswordError.value?:ok()).error
            ).contains(true)) {
            _general.value = (false)
            return false
        }
        val fields = arrayOf(name, mail, password, repeatPassword, username)
        if(fields.any { (it.value?:"").trim().isEmpty() }) {
            _general.value = (false)
            return false
        }
        _general.value = true
        return true
    }

    fun signup() {
        if(!checkGen()) return
        CoroutineScope(Dispatchers.IO).launch {
            dao.insert(User(name = name.value?:"", mail = mail.value?:"", password = password.value?:"", username=username.value?:""))
        }
        changeName("")
        changeMail("")
        changePassword("")
        changeRepeatPassword("")
        changeUsername("")
        val context = getApplication<Application>().applicationContext
        val intent = Intent(context, Login::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        makeText(context, "Usuario Creado Correctamente!", Toast.LENGTH_SHORT).show()
        startActivity(getApplication<Application>().applicationContext, intent, null)
    }

}