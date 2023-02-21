package com.example.gaugeview

data class GaugeColorState(
    val selectedColor : Int,
    val unSelectedColor : Int,
    val currentColor : Int = unSelectedColor
)