package com.fjr619.bmi

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat
import kotlin.math.roundToInt


sealed class Mode(
    val suffixHeight: String,
    val rangeHeight: List<String>,
    val suffixWeight: String,
    val rangeWeight: List<String>
) {
    object Metric : Mode(
        suffixHeight = "m",
        rangeHeight = (200 downTo 100).toList().map {
            it.toString()
        },
        suffixWeight = "kg",
        rangeWeight = (50..200).toList().map {
            it.toString()
        }
    )

    object Imperial : Mode(
        suffixHeight = "in",
        rangeHeight = listOf(
            "4'9\"",
            "4'10\"",
            "4'11\"",
            "5'0\"",
            "5'1\"",
            "5'2\"",
            "5'3\"",
            "5'4\"",
            "5'5\"",
            "5'6\"",
            "5'7\"",
            "5'8\"",
            "5'9\"",
            "5'10\"",
            "5'11\"",
            "6'0\"",
            "6'1\"",
            "6'2\"",
            "6'3\"",
            "6'4\"",
            "6'5\"",
            "6'6\""
        ).reversed(),
        suffixWeight = "lbs",
        rangeWeight = (50..250).toList().map {
            it.toString()
        }
    )
}

class BmiViewModel : ViewModel() {
    var bmi by mutableDoubleStateOf(0.0)
        private set

    var bmiPercent by mutableDoubleStateOf(0.0)
        private set
    var message by mutableStateOf("")
        private set
    var selectedMode: Mode by mutableStateOf(Mode.Metric)
        private set
    var heightState by mutableStateOf(
        ValueState(
            label = "Height",
            suffix = Mode.Metric.suffixHeight,
            range = Mode.Metric.rangeHeight,
            value =Mode.Metric.rangeHeight.last().toString()
        )
    )
        private set
    var weightState by mutableStateOf(
        ValueState(
            label = "Weight",
            suffix = Mode.Metric.suffixWeight,
            range = Mode.Metric.rangeWeight,
            value =Mode.Metric.rangeWeight.first().toString()
        )
    )
        private set

    fun updateHeight(it: String) {
        heightState = heightState.copy(value = it, error = null)
    }

    fun updateWeight(it: String) {
        weightState = weightState.copy(value = it, error = null)
    }

    fun calculate() {
        val height = if(selectedMode == Mode.Metric) {
            heightState.toNumber()?.div(100)
        } else {
            heightState.convertToInch()
        }
        val weight = weightState.toNumber()
        if (height == null)
            heightState = heightState.copy(error = "Invalid number")
        else if (weight == null)
            weightState = weightState.copy(error = "Invalid number")
        else calculateBMI(height, weight)
    }

    @OptIn(ExperimentalStdlibApi::class)
    private fun calculateBMI(height: Double, weight: Double) {
        bmi = if (selectedMode == Mode.Metric)
            weight / (height * height)
        else (703 * weight) / (height * height)

        message = when {
            bmi < 18.5 -> "Underweight"
            bmi in 18.5..< 25.0 -> "Normal"
            bmi in 25.0.. 30.0 -> "Overweight"
            bmi > 30.0-> "Obese"
            else -> error("Invalid params")
        }

        val min = 15.0
        val max = 30.0
//        if (bmi > max) bmi = max
        bmiPercent = BigDecimal((bmi  - min) / (max - min)).setScale(2, RoundingMode.HALF_EVEN).toDouble() * 100
        Log.e("TAG", "bmiPercent $bmiPercent")
    }

    fun updateMode(it: Mode) {
        selectedMode = it
        clear()
        when (selectedMode) {
            Mode.Imperial -> {
                heightState = heightState.copy(
                    suffix = Mode.Imperial.suffixHeight,
                    range = Mode.Imperial.rangeHeight,
                    value = Mode.Imperial.rangeHeight.first()
                )
                weightState = weightState.copy(
                    suffix = Mode.Imperial.suffixWeight,
                    range = Mode.Imperial.rangeWeight,
                    value = Mode.Imperial.rangeWeight.last()
                )
            }
            Mode.Metric -> {
                heightState = heightState.copy(
                    suffix = Mode.Metric.suffixHeight,
                    range = Mode.Metric.rangeHeight,
                    value = Mode.Metric.rangeHeight.last()
                )
                weightState = weightState.copy(
                    suffix = Mode.Metric.suffixWeight,
                    range = Mode.Metric.rangeWeight,
                    value = Mode.Metric.rangeWeight.first()
                )
            }
        }
    }

    fun clear() {

//        when (selectedMode) {
//            Mode.Imperial -> {
//                heightState = heightState.copy(
//                    value = Mode.Imperial.rangeHeight.first()
//                )
//                weightState = weightState.copy(
//                    value = Mode.Imperial.rangeWeight.last()
//                )
//            }
//            Mode.Metric -> {
//                heightState = heightState.copy(
//                    value = Mode.Metric.rangeHeight.last()
//                )
//                weightState = weightState.copy(
//                    value = Mode.Metric.rangeWeight.first()
//                )
//            }
//        }

//        heightState = heightState.copy(value = "", error = null)
//        weightState = weightState.copy(value = "", error = null)
        bmi = 0.0
        bmiPercent = 0.0
        message = ""
    }

//    enum class Mode { Imperial, Metric }
}