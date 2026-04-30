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

    fun saveUser(id: Int, name: String, email: String, phoneNumber: String, notifications: String) {
        userPrefs.edit {
            putInt("id", id)
            putString("name", name)
            putString("email", email)
            putString("phoneNumber", phoneNumber)
            putString("notifications", notifications)
        }
    }

    fun getUser(): User {
        val idPrefs = userPrefs.getInt("id", 0)
        val namePrefs = userPrefs.getString("name", "")
        val emailPrefs = userPrefs.getString("email", "")
        val phoneNumberPrefs = userPrefs.getString("phoneNumber", "")
        val notifications = userPrefs.getString("notifications", "")

        return User(
            id = idPrefs,
            name = namePrefs!!,
            email = emailPrefs!!,
            phoneNumber = phoneNumberPrefs!!,
            notifications = notifications.toBoolean()
        )
    }
}