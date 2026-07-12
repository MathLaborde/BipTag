package br.com.biptag.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.biptag.R
import br.com.biptag.ui.theme.BipTagTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterBottomSheet(
    availableCategories: List<String>,
    initialSelectedCategories: Set<String> = emptySet(),
    initialOnlyVerified: Boolean = false,
    onDismissRequest: () -> Unit,
    onApplyFilters: (selectedCategories: Set<String>, onlyVerified: Boolean) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface,
        dragHandle = { BottomSheetDefaults.DragHandle() }) {
        FilterBottomSheetContent(
            availableCategories = availableCategories,
            initialSelectedCategories = initialSelectedCategories,
            initialOnlyVerified = initialOnlyVerified,
            onDismissRequest = onDismissRequest,
            onApplyFilters = onApplyFilters
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FilterBottomSheetContent(
    availableCategories: List<String>,
    initialSelectedCategories: Set<String>,
    initialOnlyVerified: Boolean,
    onDismissRequest: () -> Unit,
    onApplyFilters: (selectedCategories: Set<String>, onlyVerified: Boolean) -> Unit
) {
    var selectedCategories by remember(initialSelectedCategories) {
        mutableStateOf(initialSelectedCategories)
    }
    var onlyVerified by remember(initialOnlyVerified) {
        mutableStateOf(initialOnlyVerified)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.filter_title),
                style = MaterialTheme.typography.headlineSmall
            )
            IconButton(onClick = onDismissRequest) {
                Icon(
                    Icons.Default.Close,
                    contentDescription = stringResource(R.string.filter_close_description)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(R.string.filter_category_label),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.outlineVariant
        )

        Spacer(modifier = Modifier.height(8.dp))

        FlowRow(
            modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            availableCategories.forEach { category ->
                val isSelected = selectedCategories.contains(category)
                FilterChip(
                    selected = isSelected,
                    onClick = {
                        selectedCategories =
                            if (isSelected) selectedCategories - category else selectedCategories + category
                    },
                    label = { Text(category) },
                    leadingIcon = if (isSelected) {
                        { Icon(Icons.Default.Check, null, modifier = Modifier.size(16.dp)) }
                    } else null,
                    shape = RoundedCornerShape(16.dp),
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primary,
                        selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
                        selectedLeadingIconColor = MaterialTheme.colorScheme.onPrimary
                    ))
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = stringResource(R.string.filter_status_label),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.outlineVariant
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedCard(
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
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
                        stringResource(R.string.filter_verified_only),
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        stringResource(R.string.filter_verified_description),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                Switch(checked = onlyVerified, onCheckedChange = { onlyVerified = it })
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Row(
            modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedButton(
                onClick = {
                    selectedCategories = emptySet()
                    onlyVerified = false
                },
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
            ) {
                Text(
                    stringResource(
                        R.string.filter_clear
                    ),
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Button(
                onClick = {
                    onApplyFilters(selectedCategories, onlyVerified)
                    onDismissRequest()
                }, modifier = Modifier
                    .weight(1f)
                    .height(50.dp), shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    stringResource(R.string.filter_apply),
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FilterBottomSheetPreview() {
    BipTagTheme {
        Surface(
            modifier = Modifier.fillMaxWidth(), color = MaterialTheme.colorScheme.surface
        ) {
            FilterBottomSheetContent(
                availableCategories = listOf(
                    "Eletrônicos",
                    "Veículos",
                    "Roupas",
                    "Eletrodomésticos",
                    "Acessórios",
                    "Outros"
                ),
                initialSelectedCategories = setOf("Eletrônicos", "Roupas"),
                initialOnlyVerified = true,
                onDismissRequest = {},
                onApplyFilters = { _, _ -> }
            )
        }
    }
}