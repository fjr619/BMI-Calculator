package com.fjr619.bmi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fjr619.bmi.presenter.components.GaugeComponent
import com.fjr619.bmi.presenter.components.GenderContent
import com.fjr619.bmi.presenter.components.HeightContent
import com.fjr619.bmi.presenter.components.MultiSelector
import com.fjr619.bmi.presenter.components.WeightContent
import com.fjr619.bmi.ui.theme.BMITheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class,
        ExperimentalStdlibApi::class
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {


            val isVisible = remember {
                MutableTransitionState(false).apply {
                    targetState = true
                }
            }

            BMITheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    val viewModel = viewModel<BmiViewModel>()

                    AnimatedVisibility(
                        visibleState = isVisible,
                        enter = slideInVertically(tween(500)) {
                            it / 2
                        }
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(top = 16.dp, start = 16.dp, end = 16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            val options1 = listOf("Imperial", "Metric")

                            MultiSelector(
                                options = options1,
                                selectedOption = when (viewModel.selectedMode) {
                                    is Mode.Imperial -> options1.first()
                                    else -> options1.last()
                                },
                                onOptionSelect = { option ->
                                    if (options1.first() == option) {
                                        viewModel.updateMode(Mode.Imperial)
                                    } else {
                                        viewModel.updateMode(Mode.Metric)
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(32.dp),
                            )


                            Row(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .weight(2f),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(
                                    modifier = Modifier
                                        .weight(6f),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {

                                    WeightContent(
                                        label = viewModel.weightState.label,
                                        suffix = viewModel.weightState.suffix,
                                        value = viewModel.weightState.value,
                                        onUpdateValue = viewModel::updateWeight,
                                        list = viewModel.weightState.range.toList()
                                    )

                                    Spacer(modifier = Modifier.height(16.dp))

                                    HeightContent(
                                        label = viewModel.heightState.label,
                                        suffix = viewModel.heightState.suffix,
                                        value = viewModel.heightState.value,
                                        onUpdateValue = viewModel::updateHeight,
                                        list = viewModel.heightState.range.toList()
                                    )
                                }

                                Spacer(modifier = Modifier.width(16.dp))

                                GenderContent(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .weight(4f)
                                        .align(Alignment.CenterVertically)
                                )
                            }


                            AnimatedContent(targetState = viewModel.bmi, label = "") { bmi ->
                                if (bmi == 0.0) {
                                    Button(
                                        onClick = {
                                            viewModel.calculate()
                                        }) {
                                        Text(text = "Calculate")
                                    }
                                } else {
                                    Button(
                                        shape = RoundedCornerShape(100.dp),
                                        onClick = { viewModel.clear() }) {
                                        Icon(
                                            imageVector = Icons.Default.Refresh,
                                            contentDescription = ""
                                        )
                                    }
                                }
                            }

                            Box(
                                modifier = Modifier.weight(1f),
                                contentAlignment = Alignment.BottomCenter
                            ) {

                                val foregroundIndicatorColor by animateColorAsState(
                                    targetValue =
                                    when {
                                        viewModel.bmi == 0.0 -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                                        viewModel.bmi < 18.5 -> Color.Yellow
                                        viewModel.bmi in 18.5..< 25.0 -> Color.Green
                                        viewModel.bmi in 25.0.. 30.0 -> Color.Magenta
                                        viewModel.bmi > 30.0-> Color.Red
                                        else ->MaterialTheme.colorScheme.primary
                                    }
                                    , label = "", animationSpec = tween(1000)
                                )

                                GaugeComponent(
                                    canvasSize = 200.dp,
                                    foregroundIndicatorColor = foregroundIndicatorColor,
                                    smallTextColor = foregroundIndicatorColor,
                                    smallText = if (viewModel.message.isBlank()) {
                                        "Result"
                                    } else {
                                        viewModel.message
                                    },
                                    indicatorValue = (viewModel.bmiPercent).toInt(),
                                    actualValue = viewModel.bmi.toInt()
                                )
                            }


//                            Box(
//                                modifier = Modifier
//                                    .fillMaxSize()
//                                    .weight(1f),
//                                contentAlignment = Alignment.Center
//                            ) {
//                                val convertedValue by animateFloatAsState(
//                                    targetValue = (viewModel.bmiPercent * 180).toFloat(),
//                                    label = "",
//                                    animationSpec = tween(1000)
//                                )
//
//                                val colorValue by animateColorAsState(
//                                    targetValue = if (
//                                        viewModel.bmi < 18.5) {
//                                        Color.Yellow
//                                    } else if (viewModel.bmi > 40.0) {
//                                        Color.Red
//                                    } else {
//                                        Color.Green
//                                    }, label = "", animationSpec = tween(1000)
//                                )
//
//                                val bmi by animateIntAsState(
//                                    targetValue = viewModel.bmi.toInt(),
//                                    animationSpec = tween(1000), label = ""
//                                )
//
//                                Column(
//                                    modifier = Modifier
//                                        .width(200.dp)
//                                        .height(200.dp)
//                                        .background(Color.Magenta)
//                                        .drawBehind {
//                                            val componentSize = size / 1f
//                                            drawArc(
//                                                brush = SolidColor(Color.LightGray),
//                                                startAngle = 180f,
//                                                sweepAngle = 180f,
//                                                useCenter = false,
//                                                style = Stroke(35f, cap = StrokeCap.Round),
//                                            )
//                                            drawArc(
//                                                brush = SolidColor(colorValue),
//                                                startAngle = 180f,
//                                                sweepAngle = convertedValue,
//                                                useCenter = false,
//                                                style = Stroke(35f, cap = StrokeCap.Round)
//                                            )
//                                        },
//                                    verticalArrangement = Arrangement.Center,
//                                    horizontalAlignment = Alignment.CenterHorizontally
//                                ) {
//                                    Text(text = "${bmi}")
//                                }
//                            }

//                            Row(
//                                modifier = Modifier.weight(4f),
//                                horizontalArrangement = Arrangement.spacedBy(10.dp)) {
//                                FilterChip(
//                                    selected = Mode.Metric == viewModel.selectedMode,
//                                    onClick = {
//                                        viewModel.updateMode(
//                                            Mode.Metric
//                                        )
//                                    },
//                                    label = { Text(text = "Metric") },
//                                    leadingIcon = {
//                                        AnimatedVisibility(visible = Mode.Metric == viewModel.selectedMode) {
//                                            Icon(
//                                                imageVector = Icons.Default.Check,
//                                                contentDescription = ""
//                                            )
//                                        }
//                                    }
//                                )
//
//                                FilterChip(
//                                    selected = Mode.Imperial == viewModel.selectedMode,
//                                    onClick = {
//                                        viewModel.updateMode(
//                                            Mode.Imperial
//                                        )
//                                    },
//                                    label = { Text(text = "Imperial") },
//                                    leadingIcon = {
//                                        AnimatedVisibility(visible = Mode.Imperial == viewModel.selectedMode) {
//                                            Icon(
//                                                imageVector = Icons.Default.Check,
//                                                contentDescription = ""
//                                            )
//                                        }
//                                    }
//                                )
//                            }

//                            Column(
//                                modifier = Modifier.fillMaxWidth(),
//                                horizontalAlignment = Alignment.CenterHorizontally
//                            ) {
//                                Column(
//                                    modifier = Modifier.background(
//                                        Color.LightGray.copy(alpha = 0.3f),
//                                        shape = RoundedCornerShape(80.dp)
//                                    ),
//                                    horizontalAlignment = Alignment.CenterHorizontally,
//                                ) {
//                                    Spacer(modifier = Modifier.height(12.dp))
//                                    ListItemPicker(
//                                        value = viewModel.weightState.value,
//                                        onValueChange = {
//                                            viewModel.updateWeight(it)
//                                        },
//                                        list = viewModel.weightState.range.toList()
//                                    )
//                                    Box(
//                                        modifier = Modifier
//                                            .size(12.dp)
//                                            .background(
//                                                Color.Blue.copy(alpha = 0.5f),
//                                                shape = GenericShape { size, _ ->
//                                                    // 1)
//                                                    moveTo(size.width / 2f, 0f)
//
//                                                    // 2)
//                                                    lineTo(size.width, size.height)
//
//                                                    // 3)
//                                                    lineTo(0f, size.height)
//                                                }),
//                                    )
//                                }
//
//                                Spacer(modifier = Modifier.height(16.dp))
//
//                                Box(
//                                    modifier = Modifier
//                                        .width(160.dp)
//                                        .background(
//                                            Color.LightGray.copy(alpha = 0.3f),
//                                            shape = RoundedCornerShape(30.dp)
//                                        ),
//                                    contentAlignment = Alignment.CenterStart
//                                ) {
//
//                                    Box(
//                                        modifier = Modifier
//                                            .size(12.dp)
//                                            .rotate(90f)
//                                            .background(
//                                                Color.Blue.copy(alpha = 0.5f),
//                                                shape = GenericShape { size, _ ->
//                                                    // 1)
//                                                    moveTo(size.width / 2f, 0f)
//
//                                                    // 2)
//                                                    lineTo(size.width, size.height)
//
//                                                    // 3)
//                                                    lineTo(0f, size.height)
//                                                }),
//                                    )
//
//                                    ListItemPickerVertical(
//                                        value = viewModel.heightState.value,
//                                        onValueChange = {
//                                            viewModel.updateHeight(it)
//                                        },
//                                        list = (viewModel.heightState.range).toList()
//                                    )
//                                }
//
//                                Spacer(modifier = Modifier.height(16.dp))
//                            }
//
//                            Button(onClick = {
//                                viewModel.calculate()
//                            }) {
//                                Text(text = "Calculate")
//                            }
//
//                            Text(text = "hasil ${"%.2f".format(viewModel.bmi)} ${viewModel.message}")
                        }
                    }
                }
            }
        }
    }
}
