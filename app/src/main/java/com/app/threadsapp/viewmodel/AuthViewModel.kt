package com.app.threadsapp.viewmodel

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.threadsapp.model.UserModel
import com.app.threadsapp.utils.SharedPref
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.storage
import java.util.UUID

class AuthViewModel: ViewModel() {

    var signUpInProgress = mutableStateOf(false)

    val auth = FirebaseAuth.getInstance()
    private val db = FirebaseDatabase.getInstance()
    val userRef = db.getReference("Users")
    private val storageRef = Firebase.storage.reference
    private val imageRef = storageRef.child("Users/${UUID.randomUUID()}.jpg")

    private val _firebaseUser = MutableLiveData<FirebaseUser?>()
    val firebaseUser: MutableLiveData<FirebaseUser?> = _firebaseUser

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    init {
        _firebaseUser.value = auth.currentUser
    }

    fun login(
        email: String,
        password: String,
        context: Context
    ){
        signUpInProgress.value = true
        auth.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener {
                signUpInProgress.value = true
                if (it.isSuccessful){
                    _firebaseUser.postValue(auth.currentUser)
                    getData(auth.currentUser!!.uid, context)
                } else {
                    _error.postValue(it.exception!!.message)
                }
            }
    }

    private fun getData(
        uid: String,
        context: Context
    ) {
        userRef.child(uid)
            .addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val userData = snapshot.getValue(UserModel::class.java)
                    SharedPref.storeData(
                        userData!!.name,
                        userData!!.userName,
                        userData!!.bio,
                        userData!!.email,
                        userData!!.password,
                        userData!!.imageUrl,
                        context
                    )
                }

                override fun onCancelled(error: DatabaseError) {

                }
            }
        )
    }

    fun register(
        name: String,
        userName: String,
        bio: String,
        email: String,
        password: String,
        imageUri: Uri,
        context: Context
    ){
        signUpInProgress.value = true
        auth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener {
                signUpInProgress.value = false
                if (it.isSuccessful){
                    _firebaseUser.postValue(auth.currentUser)
                    saveImage(
                        name,
                        userName,
                        bio,
                        email,
                        password,
                        imageUri,
                        auth.currentUser?.uid,
                        context
                    )
                } else {
                    _error.postValue(it.exception!!.message)
                }
            }
    }

    private fun saveImage(
        name: String,
        userName: String,
        bio: String,
        email: String,
        password: String,
        imageUri: Uri,
        uid: String?,
        context: Context
    ) {
        val uploadTask = imageRef.putFile(imageUri)
        uploadTask.addOnSuccessListener {
            imageRef.downloadUrl.addOnSuccessListener {
                saveData(
                    name,
                    userName,
                    bio,
                    email,
                    password,
                    it.toString(),
                    uid,
                    context
                )
            }
        }
    }

    private fun saveData(
        name: String,
        userName: String,
        bio: String,
        email: String,
        password: String,
        imageUrl: String,
        uid: String?,
        context: Context
    ) {
        val userData = UserModel(name, userName, bio, email, password, imageUrl,uid!!)
        val firestoreDb = Firebase.firestore
        val followersRef = firestoreDb.collection("Followers").document(uid)
        val followingRef = firestoreDb.collection("Following").document(uid)

        followersRef.set(mapOf("FollowersIds" to listOf<String>()))
        followingRef.set(mapOf("FollowingIds" to listOf<String>()))


        userRef.child(uid).setValue(userData)
            .addOnSuccessListener {
                SharedPref.storeData(
                    name,
                    userName,
                    bio,
                    email,
                    password,
                    imageUrl,
                    context
                )
            } .addOnFailureListener{

            }
    }

    fun logout(){
        auth.signOut()
        _firebaseUser.postValue(null)
    }
}