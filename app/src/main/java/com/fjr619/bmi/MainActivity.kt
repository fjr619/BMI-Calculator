package com.fjr619.bmi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedFilterChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fjr619.bmi.ui.theme.BMITheme
import com.fjr619.bmi.util.ListItemPicker
import com.fjr619.bmi.util.ListItemPickerVertical
import com.fjr619.bmi.util.NumberPicker

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BMITheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    val viewModel = viewModel<BmiViewModel>()

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {

                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            FilterChip(
                                selected = Mode.Metric == viewModel.selectedMode,
                                onClick = { viewModel.updateMode(
                                    Mode.Metric
                                ) },
                                label = { Text(text = "Metric") },
                                leadingIcon = {
                                        AnimatedVisibility(visible = Mode.Metric == viewModel.selectedMode) {
                                            Icon(imageVector = Icons.Default.Check, contentDescription = "")
                                    }
                                }
                            )

                            FilterChip(
                                selected = Mode.Imperial == viewModel.selectedMode,
                                onClick = { viewModel.updateMode(
                                    Mode.Imperial
                                ) },
                                label = { Text(text = "Imperial") },
                            leadingIcon = {
                                AnimatedVisibility(visible = Mode.Imperial == viewModel.selectedMode) {
                                    Icon(imageVector = Icons.Default.Check, contentDescription = "")
                                }
                            }
                            )
                        }

                        Column {
                            Column(
                                modifier = Modifier.background(
                                    Color.LightGray.copy(alpha = 0.3f),
                                    shape = RoundedCornerShape(80.dp)
                                ),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Spacer(modifier = Modifier.height(12.dp))
                                ListItemPicker(value = viewModel.weightState.value, onValueChange = {
                                    viewModel.updateWeight(it)
                                }, list = viewModel.weightState.range.toList())
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

                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    Color.LightGray.copy(alpha = 0.3f),
                                    shape = RoundedCornerShape(30.dp)
                                ),
                            contentAlignment = Alignment.CenterStart
                        ) {

                            Box(
                                modifier = Modifier
                                    .size(12.dp)
                                    .rotate(90f)
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

                            ListItemPickerVertical(value = viewModel.heightState.value, onValueChange = {
                                viewModel.updateHeight(it)
                            }, list = (viewModel.heightState.range).toList())
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(onClick = {
                            viewModel.calculate()
                        }) {

                        }
                        
                        Text(text = "hasil ${"%.2f".format(viewModel.bmi)} ${viewModel.message}")
                    }
                }
            }
        }
    }
}