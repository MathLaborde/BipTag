package br.com.biptag.ui.theme

import androidx.compose.ui.graphics.Color


// CORES DO TEMA CLARO

// A cor principal (Botões "Entrar", "Salvar", Initial Screen)
val LightPrimary = Color(0xFF2A2A2A)

// Cor secundária (Textos de apoio, ícones não selecionados)
val LightSecondary = Color(0xFF707070)

// Cor de fundo padrão das telas
val LightBackground = Color(0xFFFFFFFF) // Branco puro

// Cor de fundo de elementos sobrepostos (Cards, BottomBar, TopBar)
val LightSurface = Color(0xFFFFFFFF)

// Cor do texto/ícone que vai em cima da cor Primary (ex: Texto branco no botão preto)
val LightOnPrimary = Color(0xFFFFFFFF)

// Cor do texto/ícone que vai em cima da cor Secondary
val LightOnSecondary = Color(0xFFFFFFFF)

// Cor principal dos textos gerais na tela branca
val LightOnBackground = Color(0xFF1A1A1A) // Quase preto para leitura confortável

// Cor dos textos em cima de Cards/Surfaces
val LightOnSurface = Color(0xFF1A1A1A)

// Cor para bordas, divisórias (HorizontalDivider) e placeholders de input
val LightTertiary = Color(0xFFB6B6B6)
val LightOnTertiary = Color(0xFFFFFFFF)


// CORES DO TEMA ESCURO (Dark Mode para o BipTag)

val DarkPrimary = Color(0xFFE0E0E0) // Botões ficam cinza bem claro/branco
val DarkSecondary = Color(0xFFA0A0A0) // Textos secundários
val DarkBackground = Color(0xFF121212) // Fundo padrão do Android Dark Mode
val DarkSurface = Color(0xFF1E1E1E) // Fundo de Cards um pouco mais claro que o Background
val DarkOnPrimary = Color(0xFF121212) // O texto dentro do botão claro fica escuro
val DarkOnSecondary = Color(0xFF121212)
val DarkOnBackground = Color(0xFFFFFFFF) // Textos principais ficam brancos
val DarkOnSurface = Color(0xFFFFFFFF)
val DarkTertiary = Color(0xFF404040) // Bordas e divisórias ficam cinza escuro
val DarkOnTertiary = Color(0xFFFFFFFF)