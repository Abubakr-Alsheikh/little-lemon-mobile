package com.example.littlelimonmobile

import android.content.Context
import android.content.SharedPreferences

private const val PREFS_NAME = "LittleLemon"
private const val KEY_FIRST_NAME = "firstName"
private const val KEY_LAST_NAME = "lastName"
private const val KEY_EMAIL = "email"

data class User(val firstName: String, val lastName: String, val email: String)

class UserPreferences(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    val isLoggedIn: Boolean
        get() = prefs.contains(KEY_EMAIL)

    fun saveUser(user: User) {
        prefs.edit()
            .putString(KEY_FIRST_NAME, user.firstName)
            .putString(KEY_LAST_NAME, user.lastName)
            .putString(KEY_EMAIL, user.email)
            .apply()
    }

    fun getUser(): User = User(
        firstName = prefs.getString(KEY_FIRST_NAME, "").orEmpty(),
        lastName = prefs.getString(KEY_LAST_NAME, "").orEmpty(),
        email = prefs.getString(KEY_EMAIL, "").orEmpty()
    )

    fun clear() {
        prefs.edit().clear().apply()
    }
}
