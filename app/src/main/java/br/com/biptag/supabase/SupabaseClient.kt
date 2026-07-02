package br.com.biptag.supabase

import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.serializer.KotlinXSerializer
import io.github.jan.supabase.storage.Storage
import kotlinx.serialization.json.Json

object SupabaseClient {
    val client = createSupabaseClient(
        supabaseUrl = "https://dechbdkzvxnveivehzti.supabase.co", // Sua URL que já está no RetrofitClient
        supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImRlY2hiZGt6dnhudmVpdmVoenRpIiwicm9sZSI6ImFub24iLCJpYXQiOjE3ODI4MzIwMDIsImV4cCI6MjA5ODQwODAwMn0.I-FBqjPgIw7ld-UjpTRw6V2wanWoZRCMH5rz6j-_xy0" // Encontre no painel do Supabase (Project Settings > API)
    ) {
        install(Auth)
        install(Postgrest)
        install(Storage)

        defaultSerializer = KotlinXSerializer(Json {
            ignoreUnknownKeys = true
            coerceInputValues = true
        })
    }
}