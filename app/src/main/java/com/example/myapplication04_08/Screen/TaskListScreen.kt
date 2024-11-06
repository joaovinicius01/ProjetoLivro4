package com.example.myapplication04_08.Screen


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import androidx.navigation.NavHostController
import com.example.myapplication04_08.data.AppDatabase
import com.example.myapplication04_08.data.Task
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun TaskListScreen(navController: NavHostController) {

    val corroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val db = AppDatabase.getDatabase(context)

    val tasksLiveData: LiveData<List<Task>> = db.taskDao().getAllTasks()
    val tasks by tasksLiveData.observeAsState(emptyList())

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        Text("Minhas Tarefas", style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(16.dp))

        TaskInputField { taskName ->
            corroutineScope.launch {
                db.taskDao().insertTask(Task(0, taskName))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        TaskList(tasks, corroutineScope, db, navController)
    }
}


@Composable
fun TaskList(tasks: List<Task>, corroutineScope: CoroutineScope, db: AppDatabase, navController: NavHostController) {
    LazyColumn {
        items(tasks) { task ->
            TaskItem(
                task = task,
                onCheckedChange = { isChecked ->
                    corroutineScope.launch {
                        db.taskDao().updateTask(task.copy(isCompleted = isChecked))
                    }
                },
                onDelete = {
                    corroutineScope.launch {
                        db.taskDao().deleteTask(task)
                    }
                },
                onEditClick = {
                    navController.navigate("taskDetail/${task.id}")
                }
            )
        }
    }
}

@Composable
fun TaskItem(task: Task, onCheckedChange: (Boolean) -> Unit, onDelete: () -> Unit,  onEditClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = task.isCompleted,
            onCheckedChange = { onCheckedChange(it) }
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = task.name,
            style = MaterialTheme.typography.bodySmall,
            textDecoration = if (task.isCompleted) TextDecoration.LineThrough else null
        )
        Spacer(modifier = Modifier.weight(1f))
        IconButton(onClick = onEditClick) {
            Icon(imageVector = Icons.Default.Build, contentDescription = "Edit Task")
        }

        IconButton(onClick = onDelete) {
            Icon(imageVector = Icons.Default.Close, contentDescription = "Delete Task")
        }

    }
}

@Composable
fun TaskInputField(onAddTask: (String) -> Unit) {
    var newTask by remember { mutableStateOf("") }
    Column {
        TextField(
            value = newTask,
            onValueChange = { newTask = it },
            placeholder = { Text("Digite a tarefa") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = {
                if (newTask.isNotBlank()) {
                    onAddTask(newTask)
                    newTask = ""
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Adicionar")
        }
    }
}