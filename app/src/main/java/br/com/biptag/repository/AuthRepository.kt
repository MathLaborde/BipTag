package br.com.biptag.repository

import br.com.biptag.model.User
import br.com.biptag.network.SupabaseClient
import com.google.firebase.messaging.FirebaseMessaging
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import kotlinx.coroutines.tasks.await
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
        val token = try {
            FirebaseMessaging.getInstance().token.await()
        } catch (e: Exception) {
            null
        }

        auth.signUpWith(Email) {
            email = user.email
            password = user.password

            data = buildJsonObject {
                put("name", user.name)
                put("phone", user.phoneNumber)
                put("fcm_token", token)
            }
        }

        return getCurrentUser() ?: throw Exception("Sign up failed")
    }

    suspend fun signIn(email: String, password: String): User {
        val token = try {
            FirebaseMessaging.getInstance().token.await()
        } catch (e: Exception) {
            null
        }

        auth.signInWith(Email) {
            this.email = email
            this.password = password
        }

        auth.updateUser {
            data = buildJsonObject {
                put("fcm_token", token)
            }
        }

        return getCurrentUser() ?: throw Exception("Sign in failed")
    }




    suspend fun signOut() = auth.signOut()
}