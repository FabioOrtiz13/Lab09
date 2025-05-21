package com.tecsup.lab09

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch

@Composable
fun ScreenPosts(navController: NavHostController, servicio: PostApiService) {
    var listaPosts: SnapshotStateList<PostModel> = remember { mutableStateListOf() }

    LaunchedEffect(Unit) {
        val listado = servicio.getUserPosts()
        listaPosts.clear()
        listaPosts.addAll(listado)
    }

    LazyColumn {
        items(listaPosts) { item ->
            Row(modifier = Modifier.padding(8.dp)) {
                Text(
                    text = item.id.toString(),
                    Modifier.weight(0.05f),
                    textAlign = TextAlign.End
                )
                Spacer(Modifier.padding(horizontal = 1.dp))
                Text(
                    text = item.title,
                    Modifier.weight(0.7f)
                )
                IconButton(
                    onClick = {
                        navController.navigate("postsVer/${item.id}")
                        Log.e("POSTS", "ID = ${item.id}")
                    },
                    modifier = Modifier.weight(0.1f)
                ) {
                    Icon(imageVector = Icons.Outlined.Search, contentDescription = "Ver")
                }
            }
        }
    }
}

@Composable
fun ScreenPost(navController: NavHostController, servicio: PostApiService, id: Int) {
    var post by remember { mutableStateOf<PostModel?>(null) }

    LaunchedEffect(Unit) {
        val xpost = servicio.getUserPostById(id)
        post = xpost.takeIf { xpost.body != null }
    }

    Column(
        Modifier
            .padding(8.dp)
            .fillMaxSize()
    ) {
        post?.let {
            OutlinedTextField(
                value = it.id.toString(),
                onValueChange = {},
                label = { Text("id") },
                readOnly = true
            )
            OutlinedTextField(
                value = it.userId.toString(),
                onValueChange = {},
                label = { Text("userId") },
                readOnly = true
            )
            OutlinedTextField(
                value = it.title,
                onValueChange = {},
                label = { Text("title") },
                readOnly = true
            )
            OutlinedTextField(
                value = it.body,
                onValueChange = {},
                label = { Text("body") },
                readOnly = true
            )
        }
    }
}

@Composable
fun ScreenAgregarPost(servicio: PostApiService, onSuccess: () -> Unit) {
    var userId by remember { mutableStateOf("") }
    var title by remember { mutableStateOf("") }
    var body by remember { mutableStateOf("") }

    val scope = rememberCoroutineScope()

    Column(modifier = Modifier.padding(16.dp)) {
        OutlinedTextField(
            value = userId,
            onValueChange = { userId = it },
            label = { Text("User ID") }
        )
        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Title") }
        )
        OutlinedTextField(
            value = body,
            onValueChange = { body = it },
            label = { Text("Body") }
        )

        Button(onClick = {
            val nuevoPost = PostModel(
                userId = userId.toIntOrNull() ?: 0,
                id = 0,
                title = title,
                body = body,
                link = null,
                commentCount = null
            )

            scope.launch {
                servicio.createPost(nuevoPost)
                onSuccess()
            }
        }) {
            Text("Agregar Post")
        }
    }
}



