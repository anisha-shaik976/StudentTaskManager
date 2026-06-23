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

    var students by remember {
        mutableStateOf(listOf<User>())
    }

    var isLoading by remember {
        mutableStateOf(false)
    }

    var errorMessage by remember {
        mutableStateOf("")
    }

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

                if (username == "admin" && password == "1234") {

                    isLoading = true
                    errorMessage = ""

                    RetrofitClient.apiService.getUsers()
                        .enqueue(object : Callback<List<User>> {

                            override fun onResponse(
                                call: Call<List<User>>,
                                response: Response<List<User>>
                            ) {

                                isLoading = false

                                if (response.isSuccessful) {
                                    students = response.body() ?: emptyList()
                                } else {
                                    errorMessage = "Failed to load data"
                                }
                            }

                            override fun onFailure(
                                call: Call<List<User>>,
                                t: Throwable
                            ) {

                                isLoading = false
                                errorMessage = "Failed to load data"
                            }
                        })

                } else {

                    errorMessage = "Invalid Username or Password"
                    students = emptyList()
                }
            }
        ) {
            Text("Load Students")
        }

        Spacer(modifier = Modifier.height(20.dp))

        if (isLoading) {
            CircularProgressIndicator()
        }

        if (errorMessage.isNotEmpty()) {
            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = errorMessage
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        students.forEach {
            Text(text = it.name)
        }
    }
}