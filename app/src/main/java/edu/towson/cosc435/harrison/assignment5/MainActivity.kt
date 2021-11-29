package edu.towson.cosc435.harrison.assignment5

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import edu.towson.cosc435.harrison.assignment5.ui.theme.Assignment5Theme
import coil.compose.rememberImagePainter as composeRememberImagePainter

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Assignment5Theme {
                // A surface container using the 'background' color from the theme
                val vm: RestAPIViewModel by viewModels {
                    RestAPIViewModel.factory(application)
                }
                RestAPIScreen(vm)
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Assignment5Theme {
        Greeting("Android")
    }
}

@Composable
fun RestAPIScreen(
    vm: RestAPIViewModel
) {
//    LazyColumn(
//        modifier = Modifier.fillMaxWidth()
//    ) {
//        items(vm.users.value) { user ->
//            val painter = coil.compose.rememberImagePainter(
//                data = user.thumbnailUrl,
//                builder = {
//                    crossfade(true)
//                    placeholder(R.drawable.ic_launcher_foreground)
//                },
//            )
//
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//            ) {
//                Column(
//                    modifier = Modifier
//                        .height(128.dp)
//                        .weight(1.0f),
//                    verticalArrangement = Arrangement.Center,
//                    horizontalAlignment = Alignment.CenterHorizontally
//                ) {
//                    Text(text = user.title, style = MaterialTheme.typography.h5)
//                }
//                LaunchedEffect(key1 = user.id) {
//                    vm.fetchThumbnail(user.thumbnailUrl)
//                }
//                Box(
//                    modifier = Modifier.weight(1.0f)
//                ) {
//                    Image(
//                        modifier = Modifier.size(128.dp),
//                        bitmap = user.thumbnail.value.asImageBitmap(), contentDescription = null)
//                }
//            }
//        }
//    }

        //I cant figure out how to get the information from the view models and data classes to put in here

    }
}