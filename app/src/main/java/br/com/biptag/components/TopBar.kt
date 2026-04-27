package br.com.biptag.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.biptag.ui.theme.BipTagTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    startIcon: ImageVector? = null,
    onClick: () -> Unit = {},
    title: String,
    endIcon:  ImageVector? = null,
) {
    Column() {
        TopAppBar(
            title = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = title,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp
                    )
                    if (endIcon != null) {
                        Icon(
                            imageVector = endIcon,
                            contentDescription = null,
                            modifier = Modifier.size(24.dp),
                            tint = MaterialTheme.colorScheme.secondary
                        )
                    }
                }
            },
            navigationIcon = {
                if (startIcon != null) {
                    IconButton(onClick = {onClick()}) {
                        Icon(
                            imageVector = startIcon,
                            contentDescription = "",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        )
        HorizontalDivider(
            thickness = 0.5.dp,
            color = Color.Gray
        )
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
)
@Composable
private fun InventoryScreenPreview() {
    BipTagTheme {
        TopBar(
            startIcon = Icons.AutoMirrored.Outlined.ArrowBack,
            title = "Inventory",
            endIcon = Icons.Default.Tune
        )
    }
}
