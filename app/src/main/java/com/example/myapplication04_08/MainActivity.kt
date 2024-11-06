package com.example.myapplication04_08

import com.example.myapplication04_08.Screen.LoginScreen
import com.example.myapplication04_08.Screen.TaskDetailScreen
import com.example.myapplication04_08.Screen.TaskListScreen
import com.example.myapplication04_08.ui.theme.MyApplication0408Theme

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplication0408Theme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {

                    TodoApp()

                }
            }
        }
    }
}

@Composable
fun TodoApp() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "login") {
        composable("login") { LoginScreen(navController) }
        composable("taskList") { TaskListScreen(navController) }
        composable("taskDetail/{taskId}") { backStackEntry ->
            val taskId = backStackEntry.arguments?.getString("taskId")
            Log.d("task 1-- ", "$taskId")
            TaskDetailScreen(navController, taskId)
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyApplication0408Theme {
        Surface( modifier = Modifier.fillMaxSize() ) {
            TodoApp()
        }
    }
}
