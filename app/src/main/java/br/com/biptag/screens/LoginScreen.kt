package br.com.biptag.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import br.com.biptag.R
import br.com.biptag.components.BipTagTextField
import br.com.biptag.components.PrimaryButton
import br.com.biptag.navigation.Destination
import br.com.biptag.repository.AuthRepository
import br.com.biptag.ui.theme.BipTagTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun LoginScreen(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
            .safeDrawingPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp)
                .align(Alignment.Center),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            BipTagLogo()
            Spacer(modifier = Modifier.height(28.dp))
            TitleComponent()
            Spacer(modifier = Modifier.height(28.dp))
            FormLogin(navController)
            Spacer(modifier = Modifier.height(100.dp))
        }

        FooterSection(
            modifier = Modifier
                .align(Alignment.BottomCenter),
            navController = navController
        )
    }
}

@Preview
@Composable
private fun LoginScreenPreview() {
    BipTagTheme() {
        LoginScreen(rememberNavController())
    }
}

@Composable
fun BipTagLogo() {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .background(
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(17.dp)
                ),
            contentAlignment = Alignment.Center
        ){
            Image(
                painter = painterResource(id = R.drawable.logo_mark),
                contentDescription = "Logo BipTag",
                Modifier.size(34.dp),
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimary)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "BipTag",
            style = MaterialTheme.typography.displayMedium
        )
    }
}

@Composable
fun TitleComponent() {
    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(
            text = "Entrar",
            style = MaterialTheme.typography.displayMedium,
        )
        Text(
            text = "Bem-vindo de volta",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun FormLogin(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .verticalScroll(scrollState)
            .padding(0.dp, 0.dp, 0.dp, 16.dp,),
    ) {
        Text(
            text = "Email",
            modifier = Modifier.padding(bottom = 8.dp),
            style = MaterialTheme.typography.labelMedium,
        )

        BipTagTextField(
            value = email,
            onValueChange = { email = it },
            placeholder = {
                Text(
                    text = "seu@email.com",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 15.sp,
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.MailOutline,
                    contentDescription = "Mail Icon",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
        )

        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Senha",
            modifier = Modifier.padding(bottom = 8.dp),
            style = MaterialTheme.typography.labelMedium
        )

        BipTagTextField(
            value = password,
            onValueChange = { password = it },
            modifier = Modifier
                .fillMaxWidth(),
            placeholder = {
                Text(
                    text = "••••••••",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 15.sp
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = "Lock Icon",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(modifier = Modifier.height(28.dp))

        PrimaryButton(
            text = "Entrar",
            onClick = {
                scope.launch {
                    try {
                        val auth = AuthRepository()
                        auth.signIn(email, password)

                        withContext(Dispatchers.Main) {
                            if (auth.isLoggedIn()) {
                                navController.navigate(Destination.InventoryScreen.route)
                            }
                        }

                        TODO("Validação dos formulario")
                    } catch (e: Exception) {
                        println("Supabase Login Erro: ${e.message}")
                        e.printStackTrace()
                        withContext(Dispatchers.Main) {
                            Toast.makeText(context, "Falha ao Realizar o Login.", Toast.LENGTH_SHORT).show()
                        }
                    }
                }


                TODO("Implementar um Loading, ao clicar no botão, deve aparecer algo " +
                        "para mostrar o usuario está caregando para não clicar mais vezes. Colocar " +
                        "também uma trava no botão para não ser clicado 2 vezes")
            }
        )

        TextButton(
            onClick = {},
            contentPadding = PaddingValues(0.dp),
        ) {
            Text(
                text = "Esqueci minha senha",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
fun FooterSection(modifier: Modifier = Modifier,navController: NavController) {
    Column(
        modifier = modifier
    ) {
        HorizontalDivider(
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.outline,
            modifier = Modifier
                .padding(horizontal = 16.dp)
        )
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp)
        ) {
            Text(
                text = "Não tem conta?",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "Cadastre-se",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(start = 4.dp)
                    .clickable {
                        navController
                            .navigate(
                                Destination.SignUpScreen.route
                            )
                    }
            )
        }
    }
}
