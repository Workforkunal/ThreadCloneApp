package com.app.threadsapp.viewmodel

import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.threadsapp.model.ThreadModel
import com.google.firebase.Firebase
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.storage
import java.util.UUID

class AddThreadViewModel: ViewModel() {

    var postInProgress = mutableStateOf(false)

    private val db = FirebaseDatabase.getInstance()
    val userRef = db.getReference("Threads")
    private val storageRef = Firebase.storage.reference
    private val imageRef = storageRef.child("Threads/${UUID.randomUUID()}.jpg")

    private val _isPosted = MutableLiveData<Boolean>()
    val isPosted: LiveData<Boolean> = _isPosted


    fun saveImage(
        thread: String,
        userId: String,
        imageUri: Uri
    ) {
        postInProgress.value = true
        val uploadTask = imageRef.putFile(imageUri)
        uploadTask.addOnSuccessListener {
            postInProgress.value = true
            imageRef.downloadUrl.addOnSuccessListener {
                saveData(
                    thread,
                    userId,
                    it.toString(),
                )
            }
        }
    }

    fun saveData(
        thread: String,
        userId: String,
        image: String
    ) {
        val threadData = ThreadModel(thread, userId, image, System.currentTimeMillis().toString())

        userRef.child(userRef.push().key!!).setValue(threadData)
            .addOnSuccessListener {
                _isPosted.postValue(true)
            } .addOnFailureListener{
                _isPosted.postValue(false)
            }
    }
}