package br.com.biptag.screens

import android.graphics.fonts.FontFamily
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.PersonOutline
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.biptag.ui.theme.BipTagTheme

@Composable
fun SignUpScreen() {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0XFFFFFFFF))
    ) {
        Scaffold(
            topBar = { MyTopAppBar() },
            bottomBar = {BottomButtons()}
        ) { paddingValues -> paddingValues
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                FormSignUp()
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun SignUpScreenPreview() {
    BipTagTheme() {
        SignUpScreen()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopAppBar() {

    Column() {
        TopAppBar(
            title = {
                Text(
                    text = "Criar conta",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp
                )
            },
            navigationIcon = {
                IconButton(onClick = {}) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Arrow Back Icon",
                        modifier = Modifier.size(24.dp)
                    )
                }
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
        MyTopAppBar()
    }
}

@Composable
fun FormSignUp() {
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
            value = "",
            onValueChange = {},
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
                    fontSize = 13.sp
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.PersonOutline,
                    contentDescription = "Person Icon",
                    tint = Color.Gray,
                    modifier = Modifier.size(24.dp)
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
            value = "",
            onValueChange = {},
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
                    fontSize = 13.sp
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.MailOutline,
                    contentDescription = "Mail Outline Icon",
                    tint = Color.Gray,
                    modifier = Modifier.size(24.dp)
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
            value = "",
            onValueChange = {},
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
                    fontSize = 13.sp
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Phone,
                    contentDescription = "Phone Icon",
                    tint = Color.Gray,
                    modifier = Modifier.size(24.dp)
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
            value = "",
            onValueChange = {},
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
            value = "",
            onValueChange = {},
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
                checked = true,
                onCheckedChange = {},
                colors = SwitchDefaults
                    .colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = Color(0xFF2C2C2C)
                    )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FormSignUpPreview() {
    BipTagTheme() {
        FormSignUp()
    }
}

@Composable
fun BottomButtons() {
    Column(
    ) {
        Button(
            onClick = {},
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
                text = "Criar Conta",
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp
            )
        }
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth(),
        ) {
            Text(
                text = "Já tem conta?",
                fontSize = 14.sp,
                color = Color.Gray,
                fontWeight = FontWeight.Bold
            )
            TextButton(
                onClick = {},
                contentPadding = PaddingValues(horizontal = 5.dp)
            ) {
                Text(
                    text = "Entrar",
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
private fun BottomButtonsPreview() {
    BipTagTheme() {
        BottomButtons()
    }
}