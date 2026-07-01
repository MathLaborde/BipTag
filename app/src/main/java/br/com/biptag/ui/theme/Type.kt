package br.com.biptag.ui.theme

import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import br.com.biptag.R

// FONTE INTER
val interFamily = FontFamily(
    Font(R.font.inter_regular, FontWeight.Normal),
    Font(R.font.inter_medium, FontWeight.Medium),
    Font(R.font.inter_semi_bold, FontWeight.SemiBold),
    Font(R.font.inter_bold, FontWeight.Bold)
)

// Configuração de Tipografia (Fontes) do Material 3
fun createTypography(colorScheme: ColorScheme) = Typography(

    // Textos gigantes (ex: O nome "BipTag" na Splash Screen ou Login)
    displayLarge = TextStyle(
        fontFamily = interFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
        lineHeight = 40.sp,
        letterSpacing = 0.sp,
        color = colorScheme.onBackground
    ),

    // Textos gigantes (ex: O nome "BipTag" na Splash Screen ou Login)
    displayMedium = TextStyle(
        fontFamily = interFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp,
        lineHeight = 32.sp,
        letterSpacing = 0.sp,
        color = colorScheme.onBackground
    ),

    // Títulos de telas (ex: "Novo item", "Inventário" na TopBar)
    titleLarge = TextStyle(
        fontFamily = interFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 18.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp,
        color = colorScheme.onBackground
    ),

    // Títulos de blocos menores (ex: "Maria Silva" no Perfil)
    titleMedium = TextStyle(
        fontFamily = interFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.15.sp,
        color = colorScheme.onBackground
    ),

    // O texto comum do dia a dia (ex: Nome do item nas listas, valor de um card)
    bodyLarge = TextStyle(
        fontFamily = interFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp,
        color = colorScheme.onBackground
    ),

    // Texto medio (ex: O email da Maria no Perfil)
    bodyMedium = TextStyle(
        fontFamily = interFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.25.sp,
        color = colorScheme.onBackground
    ),

    // Texto um pouco menor
    bodySmall = TextStyle(
        fontFamily = interFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.25.sp,
        color = colorScheme.onBackground
    ),

    // Textos bem pequenos e de apoio (ex: Os labels "Nome do item" em cima dos campos)
    labelSmall = TextStyle(
        fontFamily = interFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp,
        color = colorScheme.onBackground
    ),

    labelMedium = TextStyle(
        fontFamily = interFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.5.sp,
        color = colorScheme.onBackground
    )
)
