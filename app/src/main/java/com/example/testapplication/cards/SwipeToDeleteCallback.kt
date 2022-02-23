package com.example.testapplication.cards

import android.graphics.Canvas
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.testapplication.utils.updateForNewXCord
import kotlin.math.abs

private const val SWIPE_RIGHT_TO_LEFT = 8
private const val SWIPE_LEFT_TO_RIGHT = 4
class SwipeToDeleteCallback : ItemTouchHelper.Callback() {

    var swipeListener: ((Int) -> Unit)? = null
    var itemMovingListener: ((RecyclerView.ViewHolder, Float, Boolean) -> Unit)? = null

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        return makeMovementFlags(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val swipeDirection = if (direction == SWIPE_RIGHT_TO_LEFT) 1 else -1
        swipeListener?.invoke(swipeDirection)
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        with(viewHolder.itemView) {
            itemMovingListener?.invoke(viewHolder, abs(x) / width, x > 0)
        }
        viewHolder.itemView.updateForNewXCord()
    }

}