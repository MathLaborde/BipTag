package br.com.biptag.screens

import androidx.compose.foundation.background
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import br.com.biptag.navigation.Destination
import br.com.biptag.ui.theme.BipTagTheme

@Composable
fun LoginScreen(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
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
    Column() {
        OutlinedTextField(
            value = "seu@gmail.com",
            onValueChange = {},
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            shape = RoundedCornerShape(10.dp),
            colors = OutlinedTextFieldDefaults
                .colors(
                    unfocusedBorderColor = Color(0xFFB6B6B6),
                    unfocusedTextColor = Color(0xFFB6B6B6)
                ),
            label = {
                Text(
                    text = "Email",
                    color = Color(0xFFB6B6B6),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
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
        OutlinedTextField(
            value = "••••••••",
            onValueChange = {},
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            shape = RoundedCornerShape(10.dp),
            colors = OutlinedTextFieldDefaults
                .colors(
                    unfocusedBorderColor = Color(0xFFB6B6B6),
                    unfocusedTextColor = Color(0xFFB6B6B6)
                ),
            label = {
                Text(
                    text = "Senha",
                    color = Color(0xFFB6B6B6),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
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
                navController
                    .navigate(
                        Destination.HomeScreen.route
                    )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp)
                .padding(4.dp),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black
            )
        ) {
            Text(
                text = "Entrar",
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp
            )
        }
        TextButton(
            onClick = {}
        ) {
            Text(
                text = "Esqueci minha senha",
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF404040)
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
        ) {
            Text(
                text = "Não tem conta?",
                fontSize = 14.sp,
                color = Color.Gray,
                fontWeight = FontWeight.Bold
            )
            TextButton(
                onClick = {
                    navController
                        .navigate(
                            Destination.SignUpScreen.route
                        )
                },
                contentPadding = PaddingValues(horizontal = 5.dp)
            ) {
                Text(
                text = "Cadastre-se",
                fontSize = 14.sp,
                color = Color.Gray,
                fontWeight = FontWeight.Bold
                )
            }
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