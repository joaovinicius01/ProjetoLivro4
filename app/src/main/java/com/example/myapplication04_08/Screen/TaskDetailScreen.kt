package com.example.myapplication04_08.Screen

import com.example.myapplication04_08.data.AppDatabase
import com.example.myapplication04_08.data.Task

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch

@Composable
fun TaskDetailScreen(navController: NavHostController, taskId: String?) {

    // ~ TEM UM ERRO NESSA TELA ~
    // TASK = null

    val corroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val db = AppDatabase.getDatabase(context)

    val task: LiveData<Task> = db.taskDao().getTaskById(taskId?.toInt() ?: -1)

    var taskName by remember { mutableStateOf(task.value?.name ?: "?") }
    var isEdit = taskId != "null"

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = if (isEdit) "Editar Tarefa" else "Nova Tarefa", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = taskName,
            onValueChange = { taskName = it },
            placeholder = { Text("Nome da Tarefa") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                if (isEdit) {
                    corroutineScope.launch {
                        db.taskDao().updateTask(task.value!!.copy(name = taskName))
                    }

                } else {
                    corroutineScope.launch {
                        db.taskDao().insertTask(Task(name = taskName))
                    }
                }
                navController.navigate("taskList") { popUpTo("taskList") { inclusive = true } }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (isEdit) "Salvar Alterações" else "Adicionar Tarefa")
        }
    }
}
