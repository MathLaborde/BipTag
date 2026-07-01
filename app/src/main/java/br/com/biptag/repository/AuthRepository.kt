package br.com.biptag.repository

import br.com.biptag.model.User
import br.com.biptag.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.put

class AuthRepository {
    private val auth = SupabaseClient.client.auth

    fun isLoggedIn(): Boolean = auth.currentSessionOrNull() != null

    fun getCurrentUser(): User? {
        val session = auth.currentSessionOrNull() ?: return null
        val userInfo = session.user

        return User(
            id = userInfo?.id ?: "",
            name = userInfo?.userMetadata?.get("name")?.jsonPrimitive?.contentOrNull ?: "",
            email = userInfo?.email ?: "",
            phoneNumber = userInfo?.userMetadata?.get("phone")?.jsonPrimitive?.contentOrNull ?: ""
        )
    }

    suspend fun signUp(user: User): User {
        auth.signUpWith(Email) {
            email = user.email
            password = user.password

            data = buildJsonObject {
                put("name", user.name)
                put("phone", user.phoneNumber)
            }
        }

        return getCurrentUser() ?: throw Exception("Sign up failed")
    }

    suspend fun signIn(email: String, password: String): User {
        auth.signInWith(Email) {
            this.email = email
            this.password = password
        }

        return getCurrentUser() ?: throw Exception("Sign in failed")
    }

    suspend fun signOut() = auth.signOut()
}