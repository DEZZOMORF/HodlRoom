package com.lampa.financulator.extensions

import android.graphics.Canvas
import android.text.StaticLayout
import androidx.core.graphics.withTranslation

fun StaticLayout.draw(canvas: Canvas, x: Float, y:Float) {
    canvas.withTranslation(x,y) {
        draw(this)
    }
}