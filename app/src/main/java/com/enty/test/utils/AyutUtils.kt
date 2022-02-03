package com.enty.test.utils

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import com.enty.test.APP_PREFERENCES
import com.enty.test.R

fun getToken(context: Context) = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE).getString("token", null)


fun getUser(context: Context) = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE).getString("user", null)


fun putToken(context: Context, token: String) = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
    .edit()
    .putString("token", token)
    .apply()

fun putUser(context: Context, user: String) = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
    .edit()
    .putString("user", user)
    .apply()

fun checkToken(context: Context) = getToken(context).isNullOrBlank()

fun navigateToLogin(navController: NavController){
    navController.navigate(R.id.loginFragment)
}