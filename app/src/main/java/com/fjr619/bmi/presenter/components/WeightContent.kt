package com.fjr619.bmi.presenter.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.fjr619.bmi.util.ListItemPicker

@Composable
fun ColumnScope.WeightContent(
    modifier: Modifier = Modifier,
    label: String,
    suffix: String,
    value: String,
    onUpdateValue: (String) -> Unit,
    list: List<String>
) {
        Text(text = "$label ($suffix)")
        Column(
            modifier = modifier
                .fillMaxWidth()
                .background(
                Color.LightGray.copy(alpha = 0.3f),
                shape = RoundedCornerShape(80.dp)
            ),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(12.dp))
            ListItemPicker(
                value = value,
                onValueChange = onUpdateValue,
                list = list
            )
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .background(
                        Color.Blue.copy(alpha = 0.5f),
                        shape = GenericShape { size, _ ->
                            // 1)
                            moveTo(size.width / 2f, 0f)

                            // 2)
                            lineTo(size.width, size.height)

                            // 3)
                            lineTo(0f, size.height)
                        }),
            )
        }
}