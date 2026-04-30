package br.com.biptag.screens

import android.content.Intent
import android.content.res.Configuration
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
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import br.com.biptag.R
import br.com.biptag.components.TopBar
import br.com.biptag.ui.theme.BipTagTheme

@Composable
fun CreditsScreen(navController: NavController) {
    Surface(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            topBar = {
                TopBar(
                    title = stringResource(R.string.credits),
                    startIcon = Icons.AutoMirrored.Outlined.ArrowBack,
                    onClick = {
                        navController.popBackStack()
                    }
                )
            },
            bottomBar = {
                MyBottomCreditsBar()
            },
        ) { paddingValues ->
            CreditsContentScreen(modifier = Modifier.padding(paddingValues))
        }
    }
}

data class Team (
    val name: String,
    val linkedIn: String,
)

@Composable
fun CreditsContentScreen(modifier: Modifier){

    val context = LocalContext.current

    val team = listOf(
        Team("Narayana Moreira", "https://www.linkedin.com/in/narayana-moreira/"),
        Team("Matheus Laborde", "https://www.linkedin.com/in/matheus-laborde/"),
        Team("Paulo Ricardo Soares", "https://www.linkedin.com/in/paulo-soares555/"),
        Team("Pedro Lucas Rosa Rezende", "https://www.linkedin.com/in/pedro-lucas-rosa-rezende-282ba02b6/"),
        Team("Victor Lang", "https://www.linkedin.com/in/victorlangsi/"),
    )

    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.background,
                modifier = Modifier
                    .size(80.dp)
                    .align(Alignment.CenterHorizontally)

            ) {
                Box(
                    contentAlignment = Alignment.Center,
                ){
                    Text(
                        stringResource(R.string.bip_tag_abreviation),
                        style = MaterialTheme.typography.displayMedium,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                stringResource(R.string.biptag),
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                stringResource(R.string.version),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Spacer(modifier = Modifier.size(16.dp))

        Column() {
            Text(
                stringResource(R.string.team),
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth(),
                colors = CardDefaults.cardColors(MaterialTheme.colorScheme.background)
            ) {
                team.forEach { people ->
                    Card(
                        modifier = Modifier
                            .padding(vertical = 4.dp)
                            .clickable {
                                val intent =
                                    Intent(Intent.ACTION_VIEW, people.linkedIn.toUri())
                                context.startActivity(intent)
                            }
                    ) {
                        Row (
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Text(people.name)

                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                                contentDescription = null,
                                modifier = Modifier.size(24.dp),
                                tint = MaterialTheme.colorScheme.secondary
                            )
                        }

                    }
                }

            }
        }
        Column(
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth()
                .height(64.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                stringResource(R.string.technologies),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                stringResource(R.string.android_nativo_jetpack_compose_nfc),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                stringResource(R.string.postgresql_supabase),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun MyBottomCreditsBar(){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(56.dp),
        verticalAlignment = Alignment.CenterVertically
    ){
        Text(
            stringResource(R.string.fiap_sistemas_de_informa_o_2026),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    locale = "en"
)
@Composable
private fun CreditsScreenPreview() {
    BipTagTheme() {
        CreditsScreen(navController = rememberNavController())
    }
}