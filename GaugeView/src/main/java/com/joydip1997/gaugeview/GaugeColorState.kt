package com.joydip1997.gaugeview

data class GaugeColorState(
    val selectedColor : Int,
    val unSelectedColor : Int,
    val currentColor : Int = unSelectedColor
)