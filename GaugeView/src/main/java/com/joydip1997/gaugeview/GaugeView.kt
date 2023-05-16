package com.joydip1997.gaugeview


import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.os.Build
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.example.gaugeview.R
import kotlin.math.floor


class GaugeView(val appContext: Context?, attrs: AttributeSet?) : View(appContext, attrs) {

    private var sweepAngle: Float = 0f
    private var internalArcStrokeWidth = 0f
    private var colorMainCenterCircle = Color.WHITE
    private var colorCenterCircle = Color.parseColor("#E6EBF2")
    private var colorPointerLine = Color.parseColor("#E6EBF2")
    private var paddingInnerCircle = 0f
    private var rotateDegree = 0f // for pointer line
    private var strokePointerLineWidth = 4.5f
    private var x = 0f
    private var y = 0f
    private var constantMeasure = 0f
    private var isWidthBiggerThanHeight = false
    private var internalArcStrokeWidthScale = 0.150
    private var paddingInnerCircleScale = 0.15
    private var pointerLineStrokeWidthScale = 0.006944
    private val mainCircleScale = 2f

    private var whiteSpaceAngle = 2f
    @SuppressLint("DrawAllocation")

    private var colorState = mutableListOf<GaugeColorState>()
    private var gaugeValue : Int = 0
    private var gaugeValueOnUi : Double = 1.2
    private var gaugePartsWidth = 150f
    private var gaugeDesc = "Good"
    private var isSpaceEnabledBetweenParts = false

    //Title Text
    private var titleTextColor : Int = R.color.title_default_color
    private var titleTextSize :  Float = 0f
    private var titleTextFont : Int = R.font.sf_ui_text_medium

    //Desc Text
    private var descTextColor : Int = R.color.title_default_color
    private var descTextSize :  Float = 0f
    private var descTextFont : Int = R.font.sf_pro_text_bold

    //Normal Text
    private var normalTextColor : Int = R.color.dark_grey_blue
    private var normalTextSize :  Float = 0f
    private var normalTextFont : Int = R.font.sf_pro_text_regular

    private var bfrTitleVerticalMargin : Float = 0f
    private var bfrDescriptionVerticalMargin : Float = 0f
    private var bfrModerateTexVerticalMargin : Float = 0f
    private var bfrFinnseAndPowerTextVerticalMargin : Float = 0f
    private var niddleVerticalMargin : Float = 0f





    private val paintWhite =  Paint().apply {
        color = Color.WHITE
        style = Paint.Style.STROKE
        strokeWidth = 70f
    }

    private val defaultGaugeColorList = mutableListOf(
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


    init {
        setGaugeValues(colors = defaultGaugeColorList)
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.GaugeView,
            0, 0).apply {

            try {
                //   gaugeValue = getFloat(R.styleable.GaugeView_default_value,1f)
                gaugeDesc = getString(R.styleable.GaugeView_default_description_value).toString()

                isSpaceEnabledBetweenParts = getBoolean(R.styleable.GaugeView_is_space_enabled_between_parts, false)
                gaugePartsWidth = getFloat(R.styleable.GaugeView_gauge_width, 150f)

                titleTextColor = getInt(
                    R.styleable.GaugeView_title_text_color,
                    R.color.title_default_color
                )
                titleTextSize = getDimension(
                    R.styleable.GaugeView_title_text_size,
                    16f)
                titleTextFont = getInt(
                    R.styleable.GaugeView_title_text_font,
                    R.font.sf_pro_text_bold
                )

                descTextColor = getInt(
                    R.styleable.GaugeView_desc_text_color,
                    R.color.title_default_color
                )
                descTextSize =  getDimension(
                    R.styleable.GaugeView_desc_text_size,
                    16f)
                descTextFont = getInt(R.styleable.GaugeView_desc_text_font, R.font.sf_pro_text_bold)


                bfrTitleVerticalMargin = getDimension(
                    R.styleable.GaugeView_bfr_value_vertical_margin,
                  16f)
                bfrDescriptionVerticalMargin = getDimension(
                    R.styleable.GaugeView_bfr_description_vertical_margin,
                    16f)
                bfrModerateTexVerticalMargin = getDimension(
                    R.styleable.GaugeView_bfr_moderate_text_vertical_margin,
                   16f)
                niddleVerticalMargin = getDimension(
                    R.styleable.GaugeView_niddle_vertical_margin,
                    40f)
                bfrFinnseAndPowerTextVerticalMargin = getDimension(
                    R.styleable.GaugeView_bfr_finnse_power_vertical_margin,
                    10f)
                normalTextSize = getDimension(
                    R.styleable.GaugeView_normal_text_size,
                    16f)

            } finally {
                recycle()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        x = width.toFloat()
        y = height.toFloat()
        if (x >= y) {
            constantMeasure = y
            isWidthBiggerThanHeight = true
        } else {
            constantMeasure = x
            isWidthBiggerThanHeight = false
        }
        internalArcStrokeWidth = (constantMeasure * internalArcStrokeWidthScale).toFloat()
        paddingInnerCircle = (constantMeasure * paddingInnerCircleScale).toFloat()
        strokePointerLineWidth = (constantMeasure * pointerLineStrokeWidthScale).toFloat()
        val mainCircleStroke = (mainCircleScale * constantMeasure / 60).toInt()

        val colorArray = colorState.map {
            Paint().apply {
                color = ContextCompat.getColor(context!!,it.currentColor)
                style = Paint.Style.STROKE
                strokeWidth = internalArcStrokeWidth
            }
        } as MutableList<Paint>
        sweepAngle = (180/colorArray.size).toFloat()

        //Gauge
        createTheGauge(canvas, x, y,colorArray)
        //Title And SubTitle
        createTheGaugeTitleAndSubTitle(canvas, x, y)
        //The Niddle
        createNiddle(canvas,x,y,mainCircleStroke)


    }

    private fun createNiddle(canvas: Canvas, x: Float, y: Float,mainCircleStroke : Int) {
        paddingInnerCircle += niddleVerticalMargin
        val p = Paint().apply {
            isAntiAlias = true
            color = colorPointerLine
            strokeWidth = strokePointerLineWidth
        }
        val angleOfTheSelectedValue = sweepAngle * gaugeValue
        rotateDegree = (180f + angleOfTheSelectedValue + (sweepAngle / 2) - whiteSpaceAngle / 2)
        canvas.rotate(rotateDegree, x / 2, y / 2)
        val a = 8
        var stopX = 0f
        var stopY = 0f
        if (isWidthBiggerThanHeight) {
            stopX =
                (x - constantMeasure) / 2 + paddingInnerCircle + constantMeasure - 2 * paddingInnerCircle + mainCircleStroke
            stopY = y / 2
        } else {
            stopX = constantMeasure - paddingInnerCircle + mainCircleStroke
            stopY = y / 2
        }
        val path = Path()
        path.fillType = Path.FillType.EVEN_ODD
        path.moveTo(x / 2 + mainCircleStroke / a, y / 2 - mainCircleStroke)
        path.lineTo(x / 2 + mainCircleStroke / a, y / 2 + mainCircleStroke)
        path.lineTo(stopX, stopY)
        path.close()
        canvas.drawPath(path,p)



        // center circles START
        val paintInnerCircle = Paint()
        paintInnerCircle.style = Paint.Style.FILL
        paintInnerCircle.color = colorCenterCircle
        paintInnerCircle.isAntiAlias = true
        canvas.drawCircle(x / 2 , y / 2, mainCircleStroke.toFloat(), paintInnerCircle)
        val paintCenterCircle = Paint()
        paintCenterCircle.style = Paint.Style.FILL
        paintCenterCircle.color = colorMainCenterCircle
        canvas.drawCircle(x / 2 , y / 2, (mainCircleStroke / 2).toFloat(), paintCenterCircle)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun createTheGaugeTitleAndSubTitle(canvas: Canvas, x: Float, y: Float) {
        val paintTextTitle =  TextPaint().apply {
            color = ContextCompat.getColor(context!!,titleTextColor)
            style = Paint.Style.FILL
            textSize = titleTextSize * 3
            typeface = context.resources.getFont(titleTextFont)
            textAlign = Paint.Align.CENTER
            isAntiAlias = true
        }
        val paintTextDesc =  Paint().apply {
            color = ContextCompat.getColor(context!!,descTextColor)
            style = Paint.Style.FILL
            textSize = descTextSize * 2
            typeface = context.resources.getFont(descTextFont)
            textAlign = Paint.Align.CENTER
            isAntiAlias = true
        }
        val textXAxis = x/2
        val titleTextY = y/2 + bfrTitleVerticalMargin * 2
        val descTextY = titleTextY + bfrDescriptionVerticalMargin
        canvas.drawText(String.format("%.1f", gaugeValueOnUi.toFloat()),textXAxis,titleTextY,paintTextTitle)
        canvas.drawText(gaugeDesc,textXAxis,descTextY,paintTextDesc)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createTheGauge(canvas: Canvas, x: Float, y: Float,colorArray : List<Paint>){
        if(colorArray.isEmpty()){
            return
        }

        val parts = colorArray.size
        val rectfInner =if (isWidthBiggerThanHeight) {
            RectF(
                (x - constantMeasure) / 2 + paddingInnerCircle,
                paddingInnerCircle,
                (x - constantMeasure) / 2 + paddingInnerCircle + constantMeasure - 2
                        * paddingInnerCircle,
                constantMeasure - paddingInnerCircle
            )
        } else {
            RectF(
                paddingInnerCircle,
                (y - constantMeasure) / 2 + paddingInnerCircle,
                constantMeasure - paddingInnerCircle,
                (y - constantMeasure) / 2
                        + constantMeasure - paddingInnerCircle
            )
        }





        //Calculate the gauge sweep angle
        val gaugeSweepAngle = sweepAngle - if(isSpaceEnabledBetweenParts) whiteSpaceAngle else 0f
        var startAngle = 180f


        //Create the gauge using arch and put white spaces if isSpaceEnabledBetweenParts is enabled
        for(i in 0 until parts){
            canvas.drawArc(rectfInner, startAngle, if(i == parts-1) gaugeSweepAngle + whiteSpaceAngle else gaugeSweepAngle, false, colorArray.get(i))
            if(isSpaceEnabledBetweenParts){
                startAngle += if(i == parts - 1){
                    gaugeSweepAngle + whiteSpaceAngle
                }else{
                    gaugeSweepAngle
                }
                if(i != parts - 1){
                    canvas.drawArc(rectfInner, startAngle, whiteSpaceAngle, false, paintWhite)
                    startAngle+=whiteSpaceAngle
                }
            }else{
                startAngle += sweepAngle
            }
        }
        val paintText =  Paint().apply {
            color = ContextCompat.getColor(context!!,normalTextColor)
            style = Paint.Style.FILL
            textSize = normalTextSize
            typeface = context.resources.getFont(normalTextFont)
            textAlign = Paint.Align.CENTER
        }

        canvas.drawText("Finesse",
            (x - constantMeasure) / 2 + paddingInnerCircle,
            y/2 + paintText.textSize + bfrFinnseAndPowerTextVerticalMargin ,paintText)

        canvas.drawText("Power",
            (x - constantMeasure) / 2 + paddingInnerCircle + constantMeasure - 2
                    * paddingInnerCircle,
            y/2 + paintText.textSize + bfrFinnseAndPowerTextVerticalMargin  ,paintText)
        canvas.drawText("Moderate",
            x/2,
            paddingInnerCircle  - bfrModerateTexVerticalMargin * 2   ,paintText)
    }

    fun setGaugeValues(colors : MutableList<GaugeColorState>){
        colorState = colors
        invalidate()
        requestLayout()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun setValue(value : Double,valueDesc: String){
        val gaugeIndexValue = if(value > colorState.size) {
            colorState.size - 1
        }else if(value < 0){
            0
        }else{
            floor(value).toInt()
        }
        this.gaugeValueOnUi  = value
        this.gaugeDesc = valueDesc
        this.gaugeValue = gaugeIndexValue
        this.titleTextColor = colorState[gaugeValue].selectedColor
        this.descTextColor = colorState[gaugeValue].selectedColor
        val newColorState = colorState.mapIndexed { index, gaugeColorState ->
            if(index == gaugeValue){
                GaugeColorState(
                    selectedColor = gaugeColorState.selectedColor,
                    unSelectedColor = gaugeColorState.unSelectedColor,
                    currentColor = gaugeColorState.selectedColor
                )
            }else{
                GaugeColorState(
                    selectedColor = gaugeColorState.selectedColor,
                    unSelectedColor = gaugeColorState.unSelectedColor,
                    currentColor = gaugeColorState.unSelectedColor
                )
            }
        }
        colorState = newColorState.toMutableList()
        invalidate()
        requestLayout()
    }



    override fun setX(x: Float) {
        this.x = x
        invalidate()
    }

    override fun setY(y: Float) {
        this.y = y
        invalidate()
    }


    override fun getX(): Float {
        return x
    }

    override fun getY(): Float {
        return y
    }



}