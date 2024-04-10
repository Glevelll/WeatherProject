package com.example.jetpackproject.activities.LoginAndReg

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jetpackproject.activities.greetings.Greetings
import com.example.jetpackproject.data.JetpackDatabase
import com.example.jetpackproject.data.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun RegisterPage(db: JetpackDatabase, context: Context, coroutineScope: CoroutineScope) {
    val maxChar = 18
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Регистрация", fontSize = 30.sp, color = Color.Black)

        var user by remember { mutableStateOf("") }
        var pass by remember { mutableStateOf("") }
        var confirm_pass by remember { mutableStateOf("") }

        val passwordVisible by rememberSaveable() { mutableStateOf(false) }
        TextField(
            value = user,
            onValueChange = { newValue ->
                if (newValue.length <= maxChar) {
                    user = newValue
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(66.dp)
                .padding(start = 64.dp, end = 64.dp, top = 4.dp, bottom = 4.dp)
                .border(
                    1.dp,
                    Color(android.graphics.Color.parseColor("#7d32a8")),
                    shape = RoundedCornerShape(50)
                ),
            shape = RoundedCornerShape(50),
            textStyle = TextStyle(
                textAlign = TextAlign.Start,
                fontSize = 16.sp
            ),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            ),
            singleLine = true,
            placeholder = { Text(text = "Введите логин") }
        )

        TextField(
            value = pass,
            onValueChange = { newValue ->
                if (newValue.length <= maxChar) {
                    pass = newValue
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(66.dp)
                .padding(start = 64.dp, end = 64.dp, top = 4.dp, bottom = 4.dp)
                .border(
                    1.dp,
                    Color(android.graphics.Color.parseColor("#7d32a8")),
                    shape = RoundedCornerShape(50)
                ),
            shape = RoundedCornerShape(50),
            textStyle = TextStyle(
                textAlign = TextAlign.Start,
                fontSize = 16.sp
            ),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            ),
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            singleLine = true,
            placeholder = { Text(text = "Введите пароль") }
        )

        TextField(
            value = confirm_pass,
            onValueChange = { newValue ->
                if (newValue.length <= maxChar) {
                    confirm_pass = newValue
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(66.dp)
                .padding(start = 64.dp, end = 64.dp, top = 4.dp, bottom = 4.dp)
                .border(
                    1.dp,
                    Color(android.graphics.Color.parseColor("#7d32a8")),
                    shape = RoundedCornerShape(50)
                ),
            shape = RoundedCornerShape(50),
            textStyle = TextStyle(
                textAlign = TextAlign.Start,
                fontSize = 16.sp
            ),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            ),
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            singleLine = true,
            placeholder = { Text(text = "Повторите пароль") }
        )

        Button(
            onClick = {
                coroutineScope.launch(Dispatchers.IO) {
                    val existingUser = db.userDao().getUserByUsername(user)
                    if (existingUser != null) {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(context, "Пользователь уже зарегистрирован", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        if (pass != confirm_pass) {
                            withContext(Dispatchers.Main) {
                                Toast.makeText(context, "Пароли не совпадают", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            val newUser = User(username = user, password = pass)
                            db.userDao().insertUser(newUser)
                            withContext(Dispatchers.Main) {
                                Toast.makeText(context, "Успешная регистрация", Toast.LENGTH_SHORT).show()
                                val intent = Intent(context, Greetings::class.java)
                                context.startActivity(intent)
                            }
                        }
                    }
                }
            },
            Modifier
                .fillMaxWidth()
                .height(66.dp)
                .padding(start = 64.dp, end = 64.dp, top = 8.dp, bottom = 8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Blue
            ),
            shape = RoundedCornerShape(50)
        ) {
            Text(
                text = "Зарегистрироваться",
                color = Color.White,
                fontSize = 18.sp
            )
        }
    }
}