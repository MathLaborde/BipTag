package br.com.biptag.screens

import android.widget.Toast
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import br.com.biptag.navigation.Destination
import br.com.biptag.repository.RoomUserRepository
import br.com.biptag.repository.SharedPreferencesUserRepository
import br.com.biptag.ui.theme.BipTagTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun LoginScreen(navController: NavController) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .safeDrawingPadding()
    ) {
        BottomSection(
            modifier = Modifier
                .align(Alignment.BottomCenter),
            navController = navController
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .align(Alignment.Center),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            BipTagLogo()
            Spacer(modifier = Modifier.height(50.dp))
            TitleComponent()
            Spacer(modifier = Modifier.height(20.dp))
            FormLogin(navController)
            Spacer(modifier = Modifier.height(100.dp))
        }
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
        Card (
            shape = CircleShape,
            modifier = Modifier
                .size(80.dp), // Aumenta tamanho do Card
            colors = CardDefaults
                .cardColors(
                    containerColor = Color.Black // fundo do Card fica preto
                )
        ){
            Box( // Usei a box para centralizar o texto dentro do Card
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ){
                Text(
                    text = "BT",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp
                )
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "BipTag",
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,

        )
    }
}

@Preview(showBackground = true)
@Composable
private fun BipTagLogoPreview() {
    BipTagTheme() {
        BipTagLogo()
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
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp
        )
        Text(
            text = "Bem-vindo de volta",
            color = Color(0xFFB6B6B6),
            fontSize = 14.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TitleComponentPreview() {
    BipTagTheme() {
        TitleComponent()
    }
}

@Composable
fun FormLogin(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val userRepository = remember { RoomUserRepository(context) }

    Column(
    ) {
        Text(
            text = "Email",
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 4.dp),
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold
        )
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            modifier = Modifier
                .fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            colors = OutlinedTextFieldDefaults
                .colors(
                    unfocusedBorderColor = Color(0xFFB6B6B6),
                    unfocusedTextColor = Color(0xFFB6B6B6)
                ),
            placeholder = {
                Text(
                    text = "seu@email.com",
                    color = Color(0xFFB6B6B6),
                    fontSize = 15.sp,
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.MailOutline,
                    contentDescription = "Mail Icon",
                    tint = Color(0xFFB6B6B6)
                )
            }
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "Senha",
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 4.dp),
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold
        )
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            modifier = Modifier
                .fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            colors = OutlinedTextFieldDefaults
                .colors(
                    unfocusedBorderColor = Color(0xFFB6B6B6),
                    unfocusedTextColor = Color(0xFFB6B6B6)
                ),
            placeholder = {
                Text(
                    text = "••••••••",
                    color = Color(0xFFB6B6B6),
                    fontSize = 15.sp
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = "Lock Icon",
                    tint = Color(0xFFB6B6B6)
                )
            },
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(modifier = Modifier.height(20.dp))
        Button(
            onClick = {
                scope.launch(Dispatchers.IO) {
                    try {
                        val user = userRepository.login(email = email, password = password)
                        val userShared = SharedPreferencesUserRepository(context)

                        if (user == null) {
                            error("Usuário ou senha inválidos")
                        }

                        withContext(Dispatchers.Main) {
                            userShared.saveUser(
                                id = user.id,
                                name = user.name,
                                email = user.email,
                                phoneNumber = user.phoneNumber,
                                notifications = user.notifications.toString()
                            )
                            navController.navigate(Destination.InventoryScreen.route)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        withContext(Dispatchers.Main) {
                            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black
            )
        ) {
            Text(
                text = "Entrar",
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp
            )
        }
        TextButton(
            onClick = {},
            contentPadding = PaddingValues(0.dp),
        ) {
            Text(
                text = "Esqueci minha senha",
                fontWeight = FontWeight.Medium,
                color = Color(0xFF404040),
                fontSize = 14.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FormLoginPreview() {
    BipTagTheme() {
        FormLogin(rememberNavController())
    }
}


@Composable
fun BottomSection(modifier: Modifier = Modifier,navController: NavController) {
    Column(
        modifier = modifier
    ) {
        HorizontalDivider(
            thickness = 1.dp,
            color = Color.LightGray,
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
                color = Color.Gray
            )
            Text(
                text = "Cadastre-se",
                fontSize = 14.sp,
                color = Color.Gray,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(start = 4.dp)
                    .clickable{
                        navController
                            .navigate(
                                Destination.SignUpScreen.route
                    )
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BottomSectionPreview() {
    BipTagTheme() {
        BottomSection(navController = rememberNavController())
    }
}