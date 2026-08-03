package br.com.biptag.screens

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import android.widget.Toast
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
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.FormatAlignLeft
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.PhotoCamera
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import br.com.biptag.components.BipTagTextField
import br.com.biptag.components.PrimaryButton
import br.com.biptag.components.TopBar
import br.com.biptag.model.FoundReport
import br.com.biptag.navigation.Destination
import br.com.biptag.repository.AlertRepository
import br.com.biptag.repository.AuthRepository
import br.com.biptag.repository.FoundReportRepository
import br.com.biptag.ui.theme.BipTagTheme
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun ConfirmationScreen(navController: NavController, alertId: Int) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val foundReportRepository = remember { FoundReportRepository() }
    val authRepository = remember { AuthRepository() }
    val alertRepository = remember { AlertRepository() }

    var isLoading by remember { mutableStateOf(false) }

    var foundAddress by remember { mutableStateOf("") }
    var markerPosition by remember { mutableStateOf<LatLng?>(null) }
    var foundDateTime by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    var isAnonymous by remember { mutableStateOf(false) }
    var itemImage by remember { mutableStateOf<Bitmap?>(null) }

    Scaffold(modifier = Modifier.imePadding(), topBar = {
        TopBar(
            title = "Confirmar Item",
            startIcon = Icons.AutoMirrored.Outlined.ArrowBack,
            onClick = {
                navController.popBackStack()
            })
    }, bottomBar = {
        PrimaryButton(
            modifier = Modifier.padding(16.dp),
            text = if (isLoading) "Enviando..." else "Avisar o dono",
            enabled = !isLoading,
            onClick = {
                coroutineScope.launch {
                    isLoading = true
                    try {
                        val currentUser = authRepository.getCurrentUser()
                        val alert = alertRepository.getAlertById(alertId)

                        if (currentUser != null && alert != null) {

                            val dateForDatabase = try {
                                val parser = java.text.SimpleDateFormat(
                                    "dd/MM/yyyy hh:mm a", java.util.Locale.getDefault()
                                )
                                val dbFormatter = java.text.SimpleDateFormat(
                                    "yyyy-MM-dd HH:mm:ss", java.util.Locale.getDefault()
                                )
                                val parsedDate = parser.parse(foundDateTime)
                                if (parsedDate != null) dbFormatter.format(parsedDate) else foundDateTime
                            } catch (e: Exception) {
                                foundDateTime
                            }

                            val report = FoundReport(
                                itemId = alert.itemId,
                                finderId = currentUser.id,
                                foundLat = markerPosition?.latitude ?: 0.0,
                                foundLng = markerPosition?.longitude ?: 0.0,
                                foundAddress = foundAddress,
                                foundDate = dateForDatabase,
                                notes = notes,
                                isAnonymous = isAnonymous,
                                alertId = alertId
                            )

                            val result = foundReportRepository.createFoundReport(report)

                            if (result != null) {
                                Toast.makeText(
                                    context, "Dono avisado! Muito obrigado.", Toast.LENGTH_LONG
                                ).show()

                                navController.navigate(Destination.InventoryScreen.route)
                            } else {
                                Toast.makeText(
                                    context,
                                    "Erro ao enviar aviso. Tente novamente.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    } catch (e: Exception) {
                        Toast.makeText(
                            context, "Erro na conexão. Verifique sua internet.", Toast.LENGTH_SHORT
                        ).show()
                    } finally {
                        isLoading = false
                    }
                }
            },
        )
    }) { paddingValues ->
        ContentConfirmationScreen(
            modifier = Modifier.padding(paddingValues),
            foundAddress = foundAddress,
            onFoundAddressChange = { foundAddress = it },
            markerPosition = markerPosition,
            onMarkerPositionChange = { markerPosition = it },
            foundDateTime = foundDateTime,
            onFoundDateTimeChange = { foundDateTime = it },
            notes = notes,
            onNotesChange = { notes = it },
            isAnonymous = isAnonymous,
            onAnonymousChange = { isAnonymous = it },
            itemImage = itemImage,
            onItemImageChange = { itemImage = it }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContentConfirmationScreen(
    modifier: Modifier,
    foundAddress: String,
    onFoundAddressChange: (String) -> Unit,
    markerPosition: LatLng?,
    onMarkerPositionChange: (LatLng?) -> Unit,
    foundDateTime: String,
    onFoundDateTimeChange: (String) -> Unit,
    notes: String,
    onNotesChange: (String) -> Unit,
    isAnonymous: Boolean,
    onAnonymousChange: (Boolean) -> Unit,
    itemImage: Bitmap?,
    onItemImageChange: (Bitmap?) -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val geocoder = remember { Geocoder(context, Locale.getDefault()) }

    var suggestions by remember { mutableStateOf<List<Address>>(emptyList()) }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(-23.5611, -46.6565), 15f)
    }

    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    val formatterHours = remember { SimpleDateFormat("hh:mm a", Locale.getDefault()) }
    val formatterDate = remember { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) }

    val datePickerState = rememberDatePickerState()
    val timePickerState = rememberTimePickerState()

    val launchImage = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            val source = ImageDecoder.createSource(context.contentResolver, uri)
            onItemImageChange(ImageDecoder.decodeBitmap(source))
        }
    }

    LaunchedEffect(foundAddress) {
        if (foundAddress.length > 5 && suggestions.none { it.getAddressLine(0) == foundAddress }) {
            delay(800)
            withContext(Dispatchers.IO) {
                try {
                    val results = geocoder.getFromLocationName(foundAddress, 5)
                    if (results != null) suggestions = results
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        } else if (foundAddress.isEmpty()) {
            suggestions = emptyList()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
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
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.outlineVariant,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Box {
            Column {
                BipTagTextField(
                    value = foundAddress,
                    onValueChange = onFoundAddressChange,
                    placeholder = {
                        Text(
                            "Ex: Parque Ibirapuera - portão 9", color = Color.Gray, fontSize = 15.sp
                        )
                    },
                    leadingIcon = {
                        Icon(
                            Icons.Outlined.LocationOn, contentDescription = null, tint = Color.Gray
                        )
                    },
                )

                if (suggestions.isNotEmpty()) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp),
                        elevation = CardDefaults.cardElevation(4.dp),
                        colors = CardColors(
                            containerColor = MaterialTheme.colorScheme.secondary,
                            contentColor = MaterialTheme.colorScheme.secondary,
                            disabledContainerColor = MaterialTheme.colorScheme.secondary,
                            disabledContentColor = MaterialTheme.colorScheme.secondary
                        ),
                    ) {
                        suggestions.forEach { address ->
                            val addressLine = address.getAddressLine(0)
                            Text(
                                text = addressLine, modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        onFoundAddressChange(addressLine)
                                        suggestions = emptyList()
                                        val latLng = LatLng(address.latitude, address.longitude)
                                        onMarkerPositionChange(latLng)
                                        coroutineScope.launch {
                                            cameraPositionState.animate(
                                                CameraUpdateFactory.newLatLngZoom(
                                                    latLng, 15f
                                                )
                                            )
                                        }
                                    }
                                    .padding(12.dp), style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }
        }

        Spacer(Modifier.size(16.dp))

        GoogleMap(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(16.dp)),
            cameraPositionState = cameraPositionState,
            onMapClick = { latLng ->
                onMarkerPositionChange(latLng)
                coroutineScope.launch {
                    withContext(Dispatchers.IO) {
                        try {
                            val results =
                                geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
                            if (!results.isNullOrEmpty()) {
                                onFoundAddressChange(results[0].getAddressLine(0))
                                suggestions = emptyList()
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }
            },
            uiSettings = MapUiSettings(zoomControlsEnabled = false)
        ) {
            markerPosition?.let { Marker(state = MarkerState(position = it)) }
        }

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
                })

            Box(
                modifier = Modifier
                    .matchParentSize()
                    .clickable { showDatePicker = true })
        }

        if (showDatePicker) {
            DatePickerDialog(onDismissRequest = { showDatePicker = false }, confirmButton = {
                TextButton(onClick = {
                    showDatePicker = false
                    showTimePicker = true
                }) {
                    Text("OK")
                }
            }, dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancel")
                }
            }) {
                DatePicker(state = datePickerState)
            }
        }

        if (showTimePicker) {
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

                            onFoundDateTimeChange(
                                formatterDate.format(datePickerState.selectedDateMillis) + " " + formatterHours.format(
                                    cal.time
                                )
                            )
                            showTimePicker = false
                        },
                    ) {
                        Text("Ok")
                    }
                },
                dismissButton = {
                    TextButton(onClick = {
                        showTimePicker = false
                    }) { Text("Cancel") }
                },
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
            value = notes, onValueChange = onNotesChange, placeholder = {
                Text(
                    text = "Estado do item, como combinar a entrega...  ",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 15.sp
                )
            }, leadingIcon = {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.FormatAlignLeft,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }, singleLine = false
        )

        Spacer(Modifier.size(16.dp))

        UserImageConfirmationScreen(
            profileImage = itemImage, launchImage = launchImage
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
                        "Avisar sem mostrar meu nome", style = MaterialTheme.typography.titleSmall
                    )
                    Text(
                        "Sua identidade fica protegida",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.outlineVariant
                    )
                }
                Switch(checked = isAnonymous, onCheckedChange = onAnonymousChange)
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
    profileImage: Bitmap?, launchImage: ManagedActivityResultLauncher<String, Uri?>
) {
    val dashColor = Color(0xFF9FC6DA)
    val stroke = remember {
        Stroke(
            width = 16f, pathEffect = PathEffect.dashPathEffect(floatArrayOf(15f, 15f), 0f)
        )
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.secondary)
            .drawBehind {
                drawRoundRect(
                    color = dashColor, style = stroke, cornerRadius = CornerRadius(16.dp.toPx())
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
                modifier = Modifier.fillMaxWidth(),
                contentScale = ContentScale.FillWidth
            )
        } else {
            Row(
                modifier = Modifier
                    .height(52.dp)
                    .padding(16.dp),
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