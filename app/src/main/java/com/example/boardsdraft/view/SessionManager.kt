package com.example.boardsdraft.view

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import javax.inject.Inject

class SessionManager @Inject constructor(
    private val context: Context
) {

    private val sharedPreferences : SharedPreferences by lazy{
        context.getSharedPreferences("MyPrefs",Context.MODE_PRIVATE)
    }
    fun isLoggedIn(): Boolean {
        return sharedPreferences.getBoolean("loggedIn", false)
    }

    fun getLoggedInEmail(): String?{
        return sharedPreferences.getString("loggedInEmail",null)
    }
    fun getLoggedInName(): String?{
        return sharedPreferences.getString("LoggedInName",null)
    }
    fun getLoggedInID(): Int{
        return sharedPreferences.getInt("loggedInID",0)
    }

    fun getPassword(): String?{
        return sharedPreferences.getString("password",null)
    }

    fun clearSession(){
        sharedPreferences.edit {
            putBoolean("loggedIn", false)
            putString("loggedInEmail", "")
            putString("LoggedInName", "")
            putInt("loggedInID", -1)
            putString("password","")
        }
    }

    fun setLoggedIn(isLoggedIn: Boolean, email: String,userID: Int,userName: String,password: String) {
        sharedPreferences.edit {
            putBoolean("loggedIn", isLoggedIn)
            putString("loggedInEmail", email)
            putString("LoggedInName", userName)
            putInt("loggedInID", userID)
            putString("password",password)
        }
    }
}
