package br.com.biptag.screens

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.automirrored.filled.FormatAlignLeft
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.ShortText
import androidx.compose.material.icons.filled.FormatAlignLeft
import androidx.compose.material.icons.filled.ShortText
import androidx.compose.material.icons.filled.ViewHeadline
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.PhotoCamera
import androidx.compose.material.icons.outlined.ShortText
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDialog
import androidx.compose.material3.TimePickerDialogDefaults
import androidx.compose.material3.TimePickerDisplayMode
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import br.com.biptag.R
import br.com.biptag.components.BipTagTextField
import br.com.biptag.components.BottomBar
import br.com.biptag.components.PrimaryButton
import br.com.biptag.components.TopBar
import br.com.biptag.ui.theme.BipTagTheme
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun ConfirmationScreen(navController: NavController, alertId: Int) {
    Scaffold(
        topBar = {
            TopBar(
                title = "Confirmar Item",
                startIcon = Icons.AutoMirrored.Outlined.ArrowBack,
                onClick = {
                    navController.popBackStack()
                }
            )
        },
        bottomBar = {
            PrimaryButton(
                modifier = Modifier.padding(16.dp),
                text = "Avisar o dono",
                onClick = {
                    // TODO função para salvar confirmação do Item
                },
            )
        }
    ) { paddingValues ->
        ContentConfirmationScreen(navController = navController, modifier = Modifier.padding(paddingValues))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContentConfirmationScreen(navController: NavController, modifier: Modifier) {

    var foundAddress by remember { mutableStateOf("") }
    var foundDateTime by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    var isAnonymous by remember { mutableStateOf(false) }

    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    val formatterHours = remember { SimpleDateFormat("hh:mm a", Locale.getDefault()) }
    val formatterDate = remember { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) }

    val datePickerState = rememberDatePickerState()

    val timePickerState = rememberTimePickerState()

    var itemImage by remember {
        mutableStateOf<Bitmap?>(null)
    }

    val context = LocalContext.current

    val launchImage = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            if (Build.VERSION.SDK_INT < 28) {
                itemImage = MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
            } else {
                val source = ImageDecoder.createSource(context.contentResolver, uri)
                itemImage = ImageDecoder.decodeBitmap(source)
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Você está com este item? Confirme os detalhes abaixo.",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.outlineVariant
        )

        Spacer(Modifier.size(16.dp))

        Text(
            text = "Onde você encontrou",
            modifier = Modifier.padding(bottom = 8.dp),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.outlineVariant
        )
        BipTagTextField(
            value = foundAddress,
            onValueChange = { foundAddress = it },
            placeholder = {
                Text(
                    text = "Parque Ibirapuera - portão 9",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 15.sp
                )
            },
            leadingIcon = {
                Icon(
                    imageVector =  Icons.Outlined.LocationOn,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
        )

        // TODO Fazer integração do endereço.

        Spacer(Modifier.size(16.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(72.dp)
                .background(color = Color(140, 0, 0))
        )

        // TODO Fazer a Integração com o Mapa

        Spacer(Modifier.size(16.dp))

        Text(
            text = "Quando encontrou",
            modifier = Modifier.padding(bottom = 8.dp),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.outlineVariant
        )

        Box(modifier = Modifier.fillMaxWidth()) {
            BipTagTextField(
                value = foundDateTime,
                onValueChange = { },
                readOnly = true,
                placeholder = {
                    Text(
                        text = "dd/mm/aaaa --:--",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 15.sp
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.CalendarMonth,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            )

            Box(
                modifier = Modifier
                    .matchParentSize()
                    .clickable { showDatePicker = true }
            )
        }

        if (showDatePicker) {
            DatePickerDialog(
                onDismissRequest = { showDatePicker = false },
                confirmButton = {
                    TextButton(onClick = {
                        showDatePicker = false
                        showTimePicker = true
                    }) {
                        Text("OK")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDatePicker = false }) {
                        Text("Cancel")
                    }
                }
            ) {
                DatePicker(state = datePickerState)
            }
        }

        if (showTimePicker){
            TimePickerDialog(
                title = { TimePickerDialogDefaults.Title(displayMode = TimePickerDisplayMode.Picker) },
                onDismissRequest = { showTimePicker = false },
                confirmButton = {
                    TextButton(
                        onClick = {
                            val cal = Calendar.getInstance()
                            cal.set(Calendar.HOUR_OF_DAY, timePickerState.hour)
                            cal.set(Calendar.MINUTE, timePickerState.minute)
                            cal.isLenient = false

                            foundDateTime = formatterDate.format(datePickerState.selectedDateMillis) + " " + formatterHours.format(cal.time)

                            showTimePicker = false
                        },
                    ) {
                        Text("Ok")
                    }
                },
                dismissButton = { TextButton(onClick = { showTimePicker = false }) { Text("Cancel") } },
                modeToggleButton = {},
            ) {
                TimePicker(state = timePickerState)
            }
        }

        Spacer(Modifier.size(16.dp))

        Text(
            text = "Observações",
            modifier = Modifier.padding(bottom = 8.dp),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.outlineVariant
        )
        BipTagTextField(
            value = notes,
            onValueChange = { notes = it },
            placeholder = {
                Text(
                    text = "Estado do item, como combinar a entrega...  ",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 15.sp
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.FormatAlignLeft,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            singleLine = false
        )

        Spacer(Modifier.size(16.dp))

        UserImageConfirmationScreen(
            profileImage = itemImage,
            launchImage = launchImage
        )

        Spacer(Modifier.size(16.dp))

        OutlinedCard(
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        "Avisar sem mostrar meu nome",
                        style = MaterialTheme.typography.titleSmall
                    )
                    Text(
                        "Sua identidade fica protegida",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.outlineVariant
                    )
                }
                Switch(checked = isAnonymous, onCheckedChange = { isAnonymous = it })
            }
        }

        Spacer(Modifier.size(16.dp))

        Card(
            colors = CardColors(
                containerColor = MaterialTheme.colorScheme.outline,
                contentColor = MaterialTheme.colorScheme.outline,
                disabledContainerColor = MaterialTheme.colorScheme.outline,
                disabledContentColor = MaterialTheme.colorScheme.outline
            ),
            shape = RoundedCornerShape(12.dp),
        ) {
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.Info,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(Modifier.size(12.dp))

                Text(
                    text = "A entrega é combinada depois: ponto parceiro ou motoboy.",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 15.sp
                )
            }
        }
    }
}


@Composable
fun UserImageConfirmationScreen(
    profileImage: Bitmap?,
    launchImage: ManagedActivityResultLauncher<String, Uri?>
) {
    val dashColor = Color(0xFF9FC6DA)
    val stroke = remember {
        Stroke(
            width = 16f,
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(15f, 15f), 0f)
        )
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.secondary)
            .drawBehind {
                drawRoundRect(
                    color = dashColor,
                    style = stroke,
                    cornerRadius = CornerRadius(16.dp.toPx())
                )
            }
            .clickable {
                launchImage.launch("image/*")
            },
        contentAlignment = Alignment.Center,
    ) {
        if (profileImage != null) {
            Image(
                bitmap = profileImage.asImageBitmap(),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        } else {
            Row(
                modifier = Modifier.fillMaxSize().padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Outlined.PhotoCamera,
                        contentDescription = "Camera Icon",
                        modifier = Modifier.size(24.dp),
                        tint = MaterialTheme.colorScheme.onSecondary
                    )
                }

                Spacer(modifier = Modifier.size(8.dp))

                Text(
                    text = "Adicionar foto (opcional)",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSecondary
                )
            }
        }
    }
}

@Preview
@Composable
private fun ConfirmationScreenPreview() {
    BipTagTheme {
        ConfirmationScreen(navController = rememberNavController(), 1)
    }
}