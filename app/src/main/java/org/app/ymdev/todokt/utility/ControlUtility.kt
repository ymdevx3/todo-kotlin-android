package org.app.ymdev.todokt.utility

import android.graphics.Color
import android.graphics.Paint
import android.text.TextPaint
import android.widget.TextView

fun changeItemState(textView: TextView, completed: Boolean) {
    var paint: TextPaint = textView.paint

    if (completed) {
        textView.setTextColor(Color.DKGRAY)
        // 取り消し線を入れる
        paint.flags = textView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
    } else {
        textView.setTextColor(Color.WHITE)
        paint.flags = textView.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
    }
    paint.isAntiAlias = true
}