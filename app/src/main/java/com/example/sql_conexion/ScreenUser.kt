package com.example.sql_conexion

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.room.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun prueb(){
    Text(text = "hello")
}
@Composable
fun ScreenUserWithScaffold() {
    val context = LocalContext.current
    var db: UserDatabase
    var id by remember { mutableStateOf("") }
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var dataUser = remember { mutableStateOf("") }

    db = crearDatabase(context)
    val dao = db.userDao()
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            Column(modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
                ) {
                Spacer(modifier = Modifier.height(28.dp))
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = {
                            val user = User(0, firstName, lastName)
                            coroutineScope.launch {
                                AgregarUsuario(user = user, dao = dao)
                            }
                            firstName = ""
                            lastName = ""
                        }
                    ) {
                        Text("Agregar Usuario", fontSize = 16.sp)
                    }

                    Button(
                        onClick = {
                            coroutineScope.launch {
                                val data = getUsers(dao = dao)
                                dataUser.value = data
                            }
                        }
                    ) {
                        Text("Listar Usuarios", fontSize = 16.sp)
                    }
                }
                Text(
                    text = dataUser.value,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
        },
        bottomBar = {
            Button(
                onClick = {
                    coroutineScope.launch {
                        val lastId = dao.getMaxId()
                        eliminarUsuario(lastId, dao)
                        withContext(Dispatchers.Main) {
                            Toast.makeText(context, "Usuario con ID $lastId eliminado", Toast.LENGTH_SHORT).show()
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 24.dp, end = 24.dp, bottom = 44.dp)
            ) {
                Text("Eliminar Usuario", fontSize = 16.sp)
            }
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.image1),
                    contentDescription = "imagen de content",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(400.dp)
                        .clip(RoundedCornerShape(0.dp, 300.dp, 0.dp, 300.dp))
                )
                Spacer(Modifier.height(24.dp))
                TextField(
                    value = firstName,
                    onValueChange = { firstName = it },
                    label = { Text("First Name: ") },
                    singleLine = true
                )
                Spacer(Modifier.height(16.dp))
                TextField(
                    value = lastName,
                    onValueChange = { lastName = it },
                    label = { Text("Last Name:") },
                    singleLine = true
                )
            }
        }
    )
}
@Composable
fun crearDatabase(context: Context): UserDatabase{
    return Room.databaseBuilder(
        context,
        UserDatabase::class.java,
        "user_db"
    ).build()
}


suspend fun getUsers(dao: UserDao): String{
   var rpta: String=""
    //LaunchedEffect(Unit) {
           val users = dao.getAll()
        users.forEach {user ->
            val fila = user.firstName+"-"+user.lastName+"\n"
            rpta += fila
        }
   // }
    return rpta
}






suspend fun  AgregarUsuario(user: User, dao: UserDao): Unit{
    //LaunchedEffect(Unit) {
    try {
        dao.insert(user)
    }
    catch (e: Exception) {
        Log.e("User","Error: insert: ${e.message}")
    }
    //}

}

// Eliminar un usuario
suspend fun eliminarUsuario(userId : Long, dao: UserDao): Unit{
    try{
        dao.deleteByUserId(userId)
    }
    catch (e: Exception){
        Log.e("User","Error: delete: ${e.message}")
    }
}


