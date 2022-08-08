package com.dezzomorf.financulator.util

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.text.*
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.dezzomorf.financulator.R
import com.dezzomorf.financulator.extensions.draw

class FinanculatorItemTouchHelper(
    private val context: Context,
    private val onSwipedAction: (itemPosition: Int) -> Unit
) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {
        val position = viewHolder.adapterPosition
        onSwipedAction.invoke(position)
    }

    override fun onChildDraw(
        canvas: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        super.onChildDraw(canvas, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        val itemView = viewHolder.itemView
        val backgroundCornerOffset = 20

        val text = context.resources.getString(R.string.delete)
        val textPaint = TextPaint().apply {
            isAntiAlias = true
            textSize = 16 * context.resources.displayMetrics.density
            color = Color.WHITE
        }
        val textWidth = textPaint.measureText(text).toInt()
        val staticLayoutBuilder = StaticLayout.Builder
            .obtain(text, 0, text.length, textPaint, textWidth)
            .setAlignment(Layout.Alignment.ALIGN_CENTER)
            .setIncludePad(false)
        val staticLayout = staticLayoutBuilder.build()

        val textMargin = (itemView.height - staticLayout.height) / 2f
        val maxTextPositionX = -staticLayout.width - textMargin * 2f
        val textPositionY = itemView.top + itemView.height / 2f - staticLayout.height / 2f
        val textPositionX = when {
            itemView.x > maxTextPositionX -> context.resources.displayMetrics.widthPixels + itemView.x + textMargin
            else -> context.resources.displayMetrics.widthPixels + maxTextPositionX + textMargin
        }

        val background = ColorDrawable(Color.RED)
        if (dX < 0) { // Swiping to the left
            background.setBounds(
                itemView.right + dX.toInt() - backgroundCornerOffset,
                itemView.top, itemView.right, itemView.bottom
            )
        } else { // view is unSwiped
            background.setBounds(0, 0, 0, 0)
        }

        background.draw(canvas)
        staticLayout.draw(canvas, textPositionX, textPositionY)
    }
}