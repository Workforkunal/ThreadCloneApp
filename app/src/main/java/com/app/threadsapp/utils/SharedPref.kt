package com.app.threadsapp.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE

object SharedPref {

    fun storeData(
        name: String,
        userName: String,
        bio: String,
        email: String,
        password: String,
        imageUrl: String,
        context: Context
    ){
        val sharedPreferences = context.getSharedPreferences("Users", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("name", name)
        editor.putString("userName", userName)
        editor.putString("bio", bio)
        editor.putString("email", email)
        editor.putString("password",password)
        editor.putString("imageUrl", imageUrl)
        editor.apply()
    }

    fun getName(context: Context): String {
        val sharedPreferences = context.getSharedPreferences("Users", MODE_PRIVATE)
        return sharedPreferences.getString("name","")!!
    }

    fun getUserName(context: Context): String {
        val sharedPreferences = context.getSharedPreferences("Users", MODE_PRIVATE)
        return sharedPreferences.getString("userName","")!!
    }

    fun getBio(context: Context): String {
        val sharedPreferences = context.getSharedPreferences("Users", MODE_PRIVATE)
        return sharedPreferences.getString("bio","")!!
    }
    fun getImage(context: Context): String {
        val sharedPreferences = context.getSharedPreferences("Users", MODE_PRIVATE)
        return sharedPreferences.getString("imageUrl","")!!
    }
}