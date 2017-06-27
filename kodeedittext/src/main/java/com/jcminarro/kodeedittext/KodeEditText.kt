package com.jcminarro.kodeedittext

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.support.v7.widget.AppCompatEditText
import android.util.AttributeSet
import android.util.TypedValue
import android.view.ActionMode

class KodeEditText
@JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = android.support.v7.appcompat.R.attr.editTextStyle) :
        AppCompatEditText(context, attrs, defStyleAttr){
    private val lineStrokeWidth: Float
    private val selectedLineStrokeWidth: Float
    private val lineColor: Int
    private val focusedLineColor: Int
    private val currentEditingLineColor: Int
    private val maxLenght: Int
    private val lineSpace: Float
    private val linesPaint: Paint = Paint(paint)
    private var extOnClickListener: OnClickListener? = null

    init {
        var outValue = TypedValue()
        context.theme.resolveAttribute(R.attr.colorControlHighlight, outValue, true)
        val defaultLineColor = outValue.data
        context.theme.resolveAttribute(R.attr.colorPrimaryDark, outValue, true)
        val defaultFocusedLineColor = outValue.data
        context.theme.resolveAttribute(R.attr.colorControlActivated, outValue, true)
        val defaultCurrentEditingLineColor = outValue.data
        val ta = context.obtainStyledAttributes(attrs, R.styleable.KodeEditText)
        lineStrokeWidth = ta.getDimension(R.styleable.KodeEditText_lineStrokeWidth, pixelToDp(DEFAULT_LINE_STROKE))
        selectedLineStrokeWidth =
                ta.getDimension(R.styleable.KodeEditText_focusedLineStrokeWidth, pixelToDp(DEFAULT_LINE_STROKE))
        maxLenght = ta.getInt(R.styleable.KodeEditText_android_maxLength, DEFAULT_MAX_LENGTH)
        lineColor = ta.getColor(R.styleable.KodeEditText_lineColor, defaultLineColor)
        focusedLineColor = ta.getColor(R.styleable.KodeEditText_focusedLineColor, defaultFocusedLineColor)
        currentEditingLineColor = ta.getColor(R.styleable.KodeEditText_currentEditingLineColor, defaultCurrentEditingLineColor)
        lineSpace = ta.getDimension(R.styleable.KodeEditText_lineSpace, pixelToDp(DEFAULT_LINE_SPACE))
        ta.recycle()
        setOnClickListener {
            setSelection(text.length)
            extOnClickListener?.onClick(it)
        }
        setBackgroundColor(0)
    }

    override fun setCustomSelectionActionModeCallback(actionModeCallback: ActionMode.Callback) {
        throw RuntimeException("setCustomSelectionActionModeCallback() not supported.")
    }
    override fun setOnClickListener(onClickListener: OnClickListener?) {
        extOnClickListener = onClickListener
    }

    override fun onDraw(canvas: Canvas) {
        val availableWidth = width - paddingRight - paddingLeft
        val charSize = (availableWidth - (lineSpace * (maxLenght - 1))) / maxLenght
        var startX = paddingLeft.toFloat()
        val bottom = height.toFloat() - paddingBottom
        val textWidths = FloatArray(text.length)
        paint.getTextWidths(text.toString(), textWidths)
        for (i in 0..maxLenght) {
            updateColorForLines(i == text.length)
            canvas.drawLine(startX, bottom, startX+charSize, bottom, linesPaint)
            text.elementAtOrNull(i)?.let {
                canvas.drawText(it.toString(),
                        startX + (charSize / 2)-(textWidths[i]/2),
                        bottom-lineStrokeWidth-paint.fontMetrics.descent,
                        paint)
            }
            startX += charSize + lineSpace
        }
    }


    private fun updateColorForLines(currentEditing: Boolean) {
        linesPaint.color = Color.RED
        if (isFocused) {
            linesPaint.setStrokeWidth(selectedLineStrokeWidth)
            linesPaint.color = if (currentEditing) {currentEditingLineColor} else {focusedLineColor}
        } else {
            linesPaint.setStrokeWidth(lineStrokeWidth)
            linesPaint.color = lineColor
        }
    }

    private fun pixelToDp(pixel: Float) = resources.displayMetrics.density * pixel

    companion object {
        val DEFAULT_LINE_STROKE = 1f
        val DEFAULT_MAX_LENGTH = 4
        val DEFAULT_LINE_SPACE = 4f
    }
}