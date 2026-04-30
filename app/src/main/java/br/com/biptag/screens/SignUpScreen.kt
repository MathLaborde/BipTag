package br.com.biptag.screens

import android.widget.Toast
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.PersonOutline
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import br.com.biptag.model.User
import br.com.biptag.navigation.Destination
import br.com.biptag.repository.RoomUserRepository
import br.com.biptag.ui.theme.BipTagTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun SignUpScreen(navController: NavController) {

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0XFFFFFFFF))
            .safeDrawingPadding()
    ) {
        Scaffold(
            topBar = { MyTopAppBar(navController) },
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

@Preview(showSystemUi = true)
@Composable
private fun SignUpScreenPreview() {
    BipTagTheme() {
        SignUpScreen(navController = rememberNavController())
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopAppBar(navController: NavController) {
    Column() {
        TopAppBar(
            title = {
                Text(
                    text = "Criar conta"
                )
            },
            navigationIcon = {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Arrow Back Icon",
                    modifier = Modifier
                        .padding(start = 8.dp, end = 8.dp)
                        .clickable{
                            navController.popBackStack()
                        }
                )
            }
        )
        HorizontalDivider(
            thickness = 0.5.dp,
            color = Color.Gray
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MyTopAppBarPreview() {
    BipTagTheme() {
        MyTopAppBar(navController = rememberNavController())
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
    val userRepository = remember { RoomUserRepository(context) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(
                text = "Nome Completo",
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 4.dp),
                fontSize = 14.sp
            )
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults
                    .colors(
                        unfocusedContainerColor = Color.White,
                        focusedContainerColor = Color.White,

                        unfocusedBorderColor = Color.LightGray,
                        focusedBorderColor = Color.Gray,

                        // 3. Cor do Texto digitado
                        unfocusedTextColor = Color.Gray,
                        focusedTextColor = Color.DarkGray
                    ),
                placeholder = {
                    Text(
                        text = "Ex.: Maria Silva",
                        color = Color.Gray,
                        fontSize = 15.sp
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.PersonOutline,
                        contentDescription = "Person Icon",
                        tint = Color.Gray,
                        modifier = Modifier.size(26.dp)
                    )
                }
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Email",
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 4.dp),
                fontSize = 14.sp
            )
            OutlinedTextField(
                value = email,
                onValueChange = {email = it},
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults
                    .colors(
                        unfocusedContainerColor = Color.White,
                        focusedContainerColor = Color.White,

                        unfocusedBorderColor = Color.LightGray,
                        focusedBorderColor = Color.Gray,

                        // 3. Cor do Texto digitado
                        unfocusedTextColor = Color.Gray,
                        focusedTextColor = Color.DarkGray
                    ),
                placeholder = {
                    Text(
                        text = "seu@gmail.com.br",
                        color = Color.Gray,
                        fontSize = 15.sp
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.MailOutline,
                        contentDescription = "Mail Outline Icon",
                        tint = Color.Gray,
                        modifier = Modifier.size(26.dp)
                    )
                }
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Telefone",
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 4.dp),
                fontSize = 14.sp
            )
            OutlinedTextField(
                value = phoneNumber,
                onValueChange = { phoneNumber = it },
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults
                    .colors(
                        unfocusedContainerColor = Color.White,
                        focusedContainerColor = Color.White,

                        unfocusedBorderColor = Color.LightGray,
                        focusedBorderColor = Color.Gray,

                        // 3. Cor do Texto digitado
                        unfocusedTextColor = Color.Gray,
                        focusedTextColor = Color.DarkGray
                    ),
                placeholder = {
                    Text(
                        text = "(11) 99999-9999",
                        color = Color.Gray,
                        fontSize = 15.sp
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Phone,
                        contentDescription = "Phone Icon",
                        tint = Color.Gray,
                        modifier = Modifier.size(26.dp)
                    )
                }
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Senha",
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 4.dp),
                fontSize = 14.sp
            )
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults
                    .colors(
                        unfocusedContainerColor = Color.White,
                        focusedContainerColor = Color.White,

                        unfocusedBorderColor = Color.LightGray,
                        focusedBorderColor = Color.Gray,

                        // 3. Cor do Texto digitado
                        unfocusedTextColor = Color.Gray,
                        focusedTextColor = Color.DarkGray
                    ),
                placeholder = {
                    Text(
                        text = "••••••••",
                        color = Color.Gray,
                        fontSize = 20.sp
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Lock Icon",
                        tint = Color.Gray,
                        modifier = Modifier.size(24.dp)
                    )
                }
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Confirmar senha",
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 4.dp),
                fontSize = 14.sp
            )
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults
                    .colors(
                        unfocusedContainerColor = Color.White,
                        focusedContainerColor = Color.White,

                        unfocusedBorderColor = Color.LightGray,
                        focusedBorderColor = Color.Gray,

                        // 3. Cor do Texto digitado
                        unfocusedTextColor = Color.Gray,
                        focusedTextColor = Color.DarkGray
                    ),
                placeholder = {
                    Text(
                        text = "••••••••",
                        color = Color.Gray,
                        fontSize = 20.sp
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Lock Icon",
                        tint = Color.Gray,
                        modifier = Modifier.size(24.dp)
                    )
                }
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Receber notificações",
                    color = Color.DarkGray,
                    fontSize = 14.sp
                )
                Switch(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .scale(0.9f),
                    checked = notifications,
                    onCheckedChange = { notifications = it },
                    colors = SwitchDefaults
                        .colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = Color(0xFF2C2C2C)
                        )
                )
            }
        }

        BottomButtons(navController = navController, onClick = {

            val user = User(
                name = name,
                email = email,
                phoneNumber = phoneNumber,
                password = password,
                notifications = notifications
            )

            scope.launch(Dispatchers.IO) {
                try {
                    userRepository.save(user)
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "Usuário criado com sucesso!", Toast.LENGTH_SHORT).show()
                        navController.navigate(Destination.LoginScreen.route)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "Erro ao criar usuário!", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }
}

@Preview(showBackground = true)
@Composable
private fun FormSignUpPreview() {
    BipTagTheme() {
        FormSignUp(navController = rememberNavController())
    }
}

@Composable
fun BottomButtons(navController: NavController, onClick : () -> Unit) {

    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp)
    ) {
        Button(
            onClick = onClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF2A2A2A)
            )
        ) {
            Text(
                text = "Criar conta",
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                color = Color.White
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth(),
        ) {
            Text(
                text = "Já tem conta?",
                fontSize = 14.sp,
                color = Color(0xFF404040)
            )
            Text(
                text = "Entrar",
                fontSize = 14.sp,
                color = Color.Gray,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(start = 4.dp)
                    .clickable{
                        navController
                            .navigate(
                                Destination.LoginScreen.route
                            )
                    }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BottomButtonsPreview() {
    BipTagTheme() {
        BottomButtons(navController = rememberNavController(), onClick = {})
    }
}