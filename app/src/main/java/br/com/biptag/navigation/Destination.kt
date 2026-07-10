package br.com.biptag.navigation

sealed class Destination(val route: String){

    // Rotas Simples
    object InitialScreen : Destination("inital")
    object LoginScreen : Destination("login")
    object SignUpScreen : Destination("signup")

    object InventoryScreen : Destination("inventory")
    object CreditsScreen : Destination("credits")
    object InventoryFormScreen : Destination("inventory_form")
    object BindTagScreen : Destination("bind_tag_screen")
    // Rotas Dinânimas
    object ProfileScreen : Destination("profile")


}