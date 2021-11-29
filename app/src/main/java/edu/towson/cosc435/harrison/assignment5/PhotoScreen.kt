package edu.towson.cosc435.harrison.assignment5

import android.app.Application
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel

class PhotoScreen {

}

class PhotoViewModel(app: Application) : AndroidViewModel(app) {
    private val _photoUsers: MutableState<List<PhotoUser>> = mutableStateOf(listOf())
    val photoUsers : State<List<PhotoUser>> = _photoUsers

    private val _photoResourceUsers: MutableState<List<PhotoUser>> = mutableStateOf(listOf())
    val photoResourceUsers: MutableState<List<PhotoUser>> = _photoResourceUsers

    init{

    }
}