package frgp.utn.edu.ar.tp3

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

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

class SignUpViewModel: ViewModel() {

    private val _name = MutableLiveData<String>("")
    private val _nameError = MutableLiveData<DataError>()
    private val _mail = MutableLiveData<String>()
    private val _mailError = MutableLiveData<DataError>()
    private val _password = MutableLiveData<String>()
    private val _passwordError = MutableLiveData<DataError>()
    private val _repeatPassword = MutableLiveData<String>()
    private val _repeatPasswordError = MutableLiveData<DataError>()

    val name: LiveData<String> get() = _name
    val nameError: LiveData<DataError> get() = _nameError
    val mail: LiveData<String> get() = _mail
    val mailError: LiveData<DataError> get() = _mailError
    val password: LiveData<String> get() = _password
    val passwordError: LiveData<DataError> get() = _passwordError
    val repeatPassword: LiveData<String> get() = _repeatPassword
    val repeatPasswordError: LiveData<DataError> get() = _repeatPasswordError

    init {
        _name.value = ""
        _mail.value = ""
        _password.value = ""
        _repeatPassword.value = ""
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
    fun validateRepeatedPassword() {
        if((repeatPassword.value?:"").equals(password.value)) _repeatPasswordError.value = ok()
        else _repeatPasswordError.value = err("Las contraseñas no coinciden. ")
    }

    fun changeName(newName: String) {
        _name.value = newName
        validateName()
    }
    fun changeMail(newMail: String) {
        _mail.value = newMail
        validateMail()
    }
    fun changePassword(newPassword: String) {
        _password.value = newPassword
        validatePassword()
    }
    fun changeRepeatPassword(newPassword: String) {
        _repeatPassword.value = newPassword
        validateRepeatedPassword()
    }

}