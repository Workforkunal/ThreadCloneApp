package com.app.threadsapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.threadsapp.model.ThreadModel
import com.app.threadsapp.model.UserModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel: ViewModel() {


    private val db = FirebaseDatabase.getInstance()
    val thread = db.getReference("Threads")

    private var _threadsAndUsers = MutableLiveData<List<Pair<ThreadModel,UserModel>>>()
    val threadsAndUsers: LiveData<List<Pair<ThreadModel,UserModel>>> = _threadsAndUsers

    init {
        fetchThreadsAndUsers {
            _threadsAndUsers.value = it
        }
    }

    private val _isLoading = MutableStateFlow(false)
    val isLoading  = _isLoading.asStateFlow()

    init {
        loadStuff()
    }

    fun loadStuff(){
        viewModelScope.launch {
            _isLoading.value = true
            delay(3000)
            _isLoading.value = false
        }
    }


    private fun fetchThreadsAndUsers(
        onResult: (List<Pair<ThreadModel, UserModel>>) -> Unit
    ){
        thread.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val result = mutableListOf<Pair<ThreadModel,UserModel>>()
                for (threadSnapshot in snapshot.children){
                    val thread = threadSnapshot.getValue(ThreadModel::class.java)
                    thread.let {
                        fetchUserFromThread(it!!){ user ->
                            result.add(0,it to user)

                            if (result.size == snapshot.childrenCount.toInt()){
                                onResult(result)
                            }
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    fun fetchUserFromThread(
        thread: ThreadModel,
        onResult: (UserModel) -> Unit
    ){
        db.getReference("Users").child(thread.userId)
            .addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val user = snapshot.getValue(UserModel::class.java)
                    user?.let(onResult)
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
    }


}