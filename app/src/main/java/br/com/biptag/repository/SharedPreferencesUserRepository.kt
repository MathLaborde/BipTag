package br.com.biptag.repository

import android.content.Context
import br.com.biptag.model.User
import androidx.core.content.edit

class SharedPreferencesUserRepository(context: Context) {
    private val userPrefs = context
        .getSharedPreferences(
            "userPreferences",
            Context.MODE_PRIVATE
        )

    fun saveUser(name: String, email: String, phoneNumber: String, notifications: String) {
        userPrefs.edit {
            putString("name", name)
            putString("email", email)
            putString("phoneNumber", phoneNumber)
            putString("notifications", notifications)
        }
    }

    fun getUser(): User {
        val namePrefs = userPrefs.getString("name", "")
        val emailPrefs = userPrefs.getString("email", "")
        val phoneNumberPrefs = userPrefs.getString("phoneNumber", "")
        val notifications = userPrefs.getString("notifications", "")

        return User(
            name = namePrefs!!,
            email = emailPrefs!!,
            phoneNumber = phoneNumberPrefs!!,
            notifications = notifications.toBoolean()
        )
    }
}