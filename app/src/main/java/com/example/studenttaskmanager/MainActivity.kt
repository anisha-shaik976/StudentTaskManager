package com.example.studenttaskmanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            LoginScreen()
        }
    }
}

@Composable
fun LoginScreen() {

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var students by remember { mutableStateOf(listOf<User>()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(40.dp))

        Text(
            text = "Student Login",
            fontSize = 28.sp
        )

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") }
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                RetrofitClient.apiService.getUsers()
                    .enqueue(object : Callback<List<User>> {

                        override fun onResponse(
                            call: Call<List<User>>,
                            response: Response<List<User>>
                        ) {
                            students = response.body() ?: emptyList()
                        }

                        override fun onFailure(
                            call: Call<List<User>>,
                            t: Throwable
                        ) {
                        }
                    })
            }
        ) {
            Text("Load Students")
        }

        Spacer(modifier = Modifier.height(20.dp))

        students.forEach {
            Text(text = it.name)
        }
    }
}