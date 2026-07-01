package br.com.biptag.screens

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.PersonOutline
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import br.com.biptag.components.BipTagTextField
import br.com.biptag.components.PrimaryButton
import br.com.biptag.components.TopBar
import br.com.biptag.model.User
import br.com.biptag.navigation.Destination
import br.com.biptag.repository.AuthRepository
import br.com.biptag.ui.theme.BipTagTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun SignUpScreen(navController: NavController) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .safeDrawingPadding(),
        color = MaterialTheme.colorScheme.background
    ) {
        Scaffold(
            topBar = {
                TopBar(
                    title = "Criar Conta",
                    startIcon = Icons.AutoMirrored.Outlined.ArrowBack,
                    onClick = { navController.popBackStack() }
                )
            },
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                FormSignUp(navController)
            }
        }
    }
}

@Preview()
@Composable
private fun SignUpScreenPreview() {
    BipTagTheme() {
        SignUpScreen(navController = rememberNavController())
    }
}

@Composable
fun FormSignUp(navController: NavController) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var notifications by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .verticalScroll(scrollState),

    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(
                text = "Nome Completo",
                modifier = Modifier.padding(bottom = 6.dp),
                style = MaterialTheme.typography.labelMedium
            )

            BipTagTextField(
                value = name,
                onValueChange = { name = it },
                placeholder = {
                    Text(
                        text = "Ex.: Maria Silva",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 15.sp
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.PersonOutline,
                        contentDescription = "Person Icon",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(26.dp)
                    )
                }
            )
            Spacer(modifier = Modifier.height(14.dp))
            Text(
                text = "Email",
                modifier = Modifier.padding(bottom = 6.dp),
                style = MaterialTheme.typography.labelMedium
            )
            BipTagTextField(
                value = email,
                onValueChange = {email = it},
                placeholder = {
                    Text(
                        text = "seu@gmail.com.br",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 15.sp
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.MailOutline,
                        contentDescription = "Mail Outline Icon",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(26.dp)
                    )
                }
            )
            Spacer(modifier = Modifier.height(14.dp))
            Text(
                text = "Telefone",
                modifier = Modifier.padding(bottom = 6.dp),
                style = MaterialTheme.typography.labelMedium
            )
            BipTagTextField(
                value = phoneNumber,
                onValueChange = { phoneNumber = it },
                placeholder = {
                    Text(
                        text = "(11) 99999-9999",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 15.sp
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Phone,
                        contentDescription = "Phone Icon",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(26.dp)
                    )
                }
            )
            Spacer(modifier = Modifier.height(14.dp))
            Text(
                text = "Senha",
                modifier = Modifier.padding(bottom = 6.dp),
                style = MaterialTheme.typography.labelMedium
            )
            BipTagTextField(
                value = password,
                onValueChange = { password = it },
                placeholder = {
                    Text(
                        text = "••••••••",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 20.sp
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Lock Icon",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(24.dp)
                    )
                },
                visualTransformation = PasswordVisualTransformation()
            )
            Spacer(modifier = Modifier.height(14.dp))
            Text(
                text = "Confirmar senha",
                modifier = Modifier.padding(bottom = 6.dp),
                style = MaterialTheme.typography.labelMedium
            )
            BipTagTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                placeholder = {
                    Text(
                        text = "••••••••",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 20.sp
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Lock Icon",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(24.dp)
                    )
                },
                visualTransformation = PasswordVisualTransformation()
            )
            Spacer(modifier = Modifier.height(14.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "Receber notificações",
                    modifier = Modifier.padding(bottom = 6.dp),
                    style = MaterialTheme.typography.labelMedium
                )

                Switch(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .scale(0.9f),
                    checked = notifications,
                    onCheckedChange = { notifications = it },
                    colors = SwitchDefaults
                        .colors(
                            checkedThumbColor = MaterialTheme.colorScheme.secondary,
                            checkedTrackColor = MaterialTheme.colorScheme.primary,
                            uncheckedThumbColor = MaterialTheme.colorScheme.primary,
                            uncheckedTrackColor = MaterialTheme.colorScheme.secondary,
                        )
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f, fill = true))

        Column() {
            PrimaryButton(
                text = "Criar Conta",
                onClick = {
                    if (password != confirmPassword) {
                        Toast.makeText(context, "As senhas não coincidem", Toast.LENGTH_SHORT).show()
                        return@PrimaryButton
                    }

                    val user = User(
                        name = name,
                        email = email,
                        phoneNumber = phoneNumber,
                        password = password,
                        notifications = notifications
                    )

                    scope.launch {
                        try {
                            val auth = AuthRepository()
                            auth.signUp(user)

                            if (auth.isLoggedIn()) {
                                withContext(Dispatchers.Main) {
                                    Toast.makeText(context, "Cadastro Realizado com sucesso!", Toast.LENGTH_LONG).show()
                                    navController.navigate(Destination.InventoryScreen.route)
                                }
                            } else {
                                withContext(Dispatchers.Main){
                                    Toast.makeText(context, "Erro ao realizar cadastro.", Toast.LENGTH_LONG).show()
                                }
                            }
                        } catch (e: Exception) {
                            println("Supabase Erro Detalhado: ${e.message}")
                            e.printStackTrace()
                            withContext(Dispatchers.Main) {
                                Toast.makeText(context, "Erro ao realizar cadastro.", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            )

            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            ) {
                Text(
                    text = "Já tem conta?",
                    fontSize = 14.sp,
                    color = Color(0xFF404040)
                )
                Text(
                    text = "Entrar",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(start = 4.dp)
                        .clickable {
                            navController
                                .navigate(
                                    Destination.LoginScreen.route
                                )
                        }
                )
            }
        }
    }
}
