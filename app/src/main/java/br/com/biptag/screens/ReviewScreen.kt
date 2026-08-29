package br.com.biptag.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Sell
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarOutline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import br.com.biptag.components.PrimaryButton
import br.com.biptag.components.TopBar
import br.com.biptag.model.Alert
import br.com.biptag.model.ReturnProcess
import br.com.biptag.model.Review
import br.com.biptag.model.User
import br.com.biptag.navigation.Destination
import br.com.biptag.repository.AlertRepository
import br.com.biptag.repository.AuthRepository
import br.com.biptag.repository.ReviewRepository
import br.com.biptag.repository.ReturnProcessRepository
import br.com.biptag.ui.theme.BipTagTheme
import coil.compose.AsyncImage
import kotlinx.coroutines.launch

@Composable
fun RatingScreen(
    navController: NavController,
    returnProcessId: Int
) {
    val returnProcessRepository = remember { ReturnProcessRepository() }
    val alertRepository = remember { AlertRepository() }
    val authRepository = remember { AuthRepository() }
    val reviewRepository = remember { ReviewRepository() }

    var returnProcess by remember { mutableStateOf<ReturnProcess?>(null) }
    var alert by remember { mutableStateOf<Alert?>(null) }

    var user by remember { mutableStateOf<User?>(null) }

    LaunchedEffect(returnProcessId) {
        returnProcess = returnProcessRepository.getReturnProcessById(returnProcessId)
        alert = alertRepository.getAlertById(returnProcess?.alertId ?: return@LaunchedEffect)
        user = authRepository.getCurrentUser()
    }

    var rating by remember { mutableIntStateOf(4) }

    val scope = rememberCoroutineScope()

    Scaffold(
        modifier = Modifier
            .padding(16.dp),
        topBar = {
            TopBar(
                title = "Concluído"
            )
        },
        bottomBar = {
            PrimaryButton(
                text = "Avaliar",
                onClick = {

                    scope.launch {
                        try {
                            val review = Review(
                                returnProcessId = returnProcessId,
                                reviewerId = user?.id ?: "",
                                rating = rating
                            )

                            reviewRepository.createReview(
                                review
                            )

                            navController.navigate(Destination.InventoryScreen.route) {
                                popUpTo(0)
                            }
                        } catch (e: Exception){

                        }
                    }
                }
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(Color(0xFFE8F5E9), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    modifier = Modifier.size(50.dp),
                    tint = Color(0xFF4CAF50)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Devolução concluída!",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = if (user?.id == alert?.itemData?.userId) {
                    "O item ${alert?.itemData?.name} voltou para você. Obrigado por usar o BipTag."
                } else {
                    "O item ${alert?.itemData?.name} voltou para o dono. Obrigado por usar o BipTag."
                },
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(32.dp))

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                color = Color.White
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (alert?.itemData?.image != null) {
                        AsyncImage(
                            model = alert?.itemData?.image,
                            contentDescription = "Imagem de ${alert?.itemData?.name}",
                            modifier = Modifier
                                .size(56.dp)
                                .clip(RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Crop,
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(MaterialTheme.colorScheme.surfaceVariant),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.Sell,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = alert?.itemData?.name ?: "Item devolvido",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Devolvido por Anônimo.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .background(Color(0xFFE8F5E9), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = Color(0xFF4CAF50)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                color = Color.White
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = if (user?.id == alert?.itemData?.userId) {
                            "Avalie quem te ajudou"
                        } else {
                            "Avalie sua experiência"
                        },
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        for (i in 1..5) {
                            Card(
                                modifier = Modifier.clickable(
                                    onClick = {
                                        rating = i
                                    }
                                )
                            ) {
                                Icon(
                                    imageVector = if (i <= rating) Icons.Default.Star else Icons.Default.StarOutline,
                                    contentDescription = "Avaliação $i estrelas",
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clickable { rating = i },
                                    tint = if (i <= rating) Color(0xFFFFD54F) else Color.LightGray
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RatingScreenPreview() {
    BipTagTheme {
        RatingScreen(rememberNavController(), 0)
    }
}