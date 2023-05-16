package com.joydip1997.gaugeactivity


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.gaugeactivity.R
import com.joydip1997.gaugeview.GaugeColorState
import com.joydip1997.gaugeview.GaugeView

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        gaugeView = findViewById<View>(R.id.gaugeView) as GaugeView2
//        gaugeView!!.setRotateDegree(degree)
//        startRunning()
        val gaugeListValue = mutableListOf<GaugeColorState>(
            GaugeColorState(
                selectedColor = R.color.first_selected,
                unSelectedColor = R.color.first_unselected
            ),
            GaugeColorState(
                selectedColor = R.color.second_selected,
                unSelectedColor = R.color.second_unselected
            ),
            GaugeColorState(
                selectedColor = R.color.third_selected,
                unSelectedColor = R.color.third_unselected
            ),
            GaugeColorState(
                selectedColor = R.color.fourth_selected,
                unSelectedColor = R.color.fourth_unselected
            ),
            GaugeColorState(
                selectedColor = R.color.fifth_selected,
                unSelectedColor = R.color.fifth_unselected
            ),
            GaugeColorState(
                selectedColor = R.color.sixth_selected,
                unSelectedColor = R.color.sixth_unselected
            ),
            GaugeColorState(
                selectedColor = R.color.seventh_selected,
                unSelectedColor = R.color.seventh_unselected
            ),
            GaugeColorState(
                selectedColor = R.color.eighth_selected,
                unSelectedColor = R.color.eighth_unselected
            ),
            GaugeColorState(
                selectedColor = R.color.ninth_selected,
                unSelectedColor = R.color.ninth_unselected
            ),


        )
        findViewById<GaugeView>(R.id.gaugeView).apply {
            setGaugeValues(gaugeListValue)
            setOnClickListener {
                setValue(6.05,"GOOD")
            }
        }
    }




}