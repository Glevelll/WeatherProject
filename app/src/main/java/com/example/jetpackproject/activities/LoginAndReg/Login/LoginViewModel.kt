package com.example.jetpackproject.activities.LoginAndReg.Login

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.jetpackproject.activities.greetings.Greetings
import com.example.jetpackproject.data.local.UserDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel : ViewModel() {
    private val maxChar = 18
    val user = mutableStateOf("")
    val pass = mutableStateOf("")

    fun userInput(newValue: String) {
        if (newValue.length <= maxChar) {
            user.value = newValue
        }
    }

    fun passInput(newValue: String) {
        if (newValue.length <= maxChar) {
            pass.value = newValue
        }
    }

    fun onLoginClicked(userDao: UserDao, context: Context, coroutineScope: CoroutineScope) {
        coroutineScope.launch(Dispatchers.IO) {
            if (user.value.isEmpty() || pass.value.isEmpty()) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Заполните все поля", Toast.LENGTH_SHORT).show()
                }
            } else {
                val userDB = userDao.getUserByUsername(user.value)
                withContext(Dispatchers.Main) {
                    if (userDB != null && userDB.password == pass.value) {
                        Toast.makeText(context, "Успешная авторизация", Toast.LENGTH_SHORT).show()
                        val intent = Intent(context, Greetings::class.java)
                        context.startActivity(intent)
                        user.value = ""
                        pass.value = ""
                    } else {
                        Toast.makeText(context, "Неверный логин или пароль", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}