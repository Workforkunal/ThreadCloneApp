package com.app.threadsapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.threadsapp.model.ThreadModel
import com.app.threadsapp.model.UserModel
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.firestore

class UserViewModel: ViewModel() {


    private val db = FirebaseDatabase.getInstance()
    val threadRef = db.getReference("Threads")
    val userRef = db.getReference("Users")

    private val _threads = MutableLiveData(listOf<ThreadModel>())
    val threads : LiveData<List<ThreadModel>> get() =_threads

    private val _users = MutableLiveData(UserModel())
    val users : LiveData<UserModel> get() = _users

    private val _followerList = MutableLiveData(listOf<String>())
    val followerList : LiveData<List<String>> get() =_followerList

    private val _followingList = MutableLiveData(listOf<String>())
    val followingList : LiveData<List<String>> get() =_followingList

    val firestoreDb = Firebase.firestore


    fun fetchUsers(
        uid: String
    ){
        userRef.child(uid).addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val user = snapshot.getValue(UserModel::class.java)
                    _users.postValue(user)
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
    }

    fun fetchThreads(
        uid: String
    ){
        threadRef.orderByChild("userId").equalTo(uid).addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val threadList = snapshot.children.mapNotNull {
                    it.getValue(ThreadModel::class.java)
                }
                _threads.postValue(threadList)
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    fun followUsers(
        userId: String,
        currentUserId: String
    ){

        val ref = firestoreDb.collection("Following").document(currentUserId)
        val followerRef = firestoreDb.collection("Followers").document(userId)

        ref.update("FollowingIds", FieldValue.arrayUnion(userId))
        followerRef.update("FollowerIds", FieldValue.arrayUnion(currentUserId))
    }

    fun getFollowers(
        userId: String
    ){
        firestoreDb.collection("Followers").document(userId)
            .addSnapshotListener{ value, error ->
                val followerIds = value?.get("FollowerIds") as? List<String> ?: listOf()
                _followerList.postValue(followerIds)
            }
    }
    fun getFollowing(
        userId: String
    ){
        firestoreDb.collection("Following").document(userId)
            .addSnapshotListener{ value, error ->
                val followingIds = value?.get("FollowingIds") as? List<String> ?: listOf()
                _followingList.postValue(followingIds)
            }
    }
}