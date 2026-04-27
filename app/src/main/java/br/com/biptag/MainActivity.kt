package br.com.biptag

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import br.com.biptag.screens.CreditsScreen
import br.com.biptag.screens.InitialScreen
import br.com.biptag.screens.LoginScreen
import br.com.biptag.screens.SignUpScreen
import br.com.biptag.ui.theme.BipTagTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BipTagTheme {
                //CreditsScreen()
                //LoginScreen()
                //SignUpScreen()
                InitialScreen()
            }
        }
    }
}