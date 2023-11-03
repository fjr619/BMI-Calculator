package com.fjr619.bmi.presenter.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.fjr619.bmi.Mode

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterChipComponents(
    label: String,
    isSelected: Boolean,
    onUpdateMode: () -> Unit,
    imageVector: ImageVector = Icons.Default.Check
) {
    FilterChip(
        selected = isSelected,
        onClick = onUpdateMode,
        label = { Text(text = label) },
        leadingIcon = {
            AnimatedVisibility(visible = isSelected) {
                Icon(
                    imageVector = imageVector,
                    contentDescription = ""
                )
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterChipContent(
    selectedMode: Mode,
    onUpdateMode: (Mode) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        FilterChipComponents(
            label = "Metric",
            isSelected = Mode.Metric == selectedMode,
            onUpdateMode = {
                onUpdateMode(Mode.Metric)
            },
        )

        FilterChipComponents(
            label = "Imperial",
            isSelected = Mode.Imperial == selectedMode,
            onUpdateMode = {
                onUpdateMode(Mode.Imperial)
            },
        )
    }
}