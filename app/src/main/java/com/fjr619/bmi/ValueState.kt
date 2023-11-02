package com.fjr619.bmi


data class ValueState(
    val label: String, // lable
    val suffix: String, // units
    val value: String = "", // A string representing the user's input value.
    val range: List<String>,
    val error: String? = null // error meaage if any
) {
    fun toNumber() = value.toDoubleOrNull()
    fun convertToInch(): Double {
        val a = value.replace("\"", "").split("'")
        return (a[0].toDouble() * 12) + a[1].toDouble()
    }
}