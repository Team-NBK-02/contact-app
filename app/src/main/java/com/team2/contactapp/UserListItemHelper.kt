package com.team2.contactapp

import android.content.Context
import android.graphics.Canvas
import android.util.Log
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

class UserListItemHelper(
    private val context: Context,
    private val requsetPermission: (pos: Int) -> (Unit),
) : ItemTouchHelper.Callback() {
    companion object {
        private const val TAG = "UserListItemHelper"
    }

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
    ): Int {
        return makeMovementFlags(0, ItemTouchHelper.RIGHT)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder,
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        if (viewHolder is UserRecyclerViewAdapter.CustomViewHolder) {
            if (viewHolder.mUser == null) return
            requsetPermission(viewHolder.adapterPosition)
        }
    }

    override fun isItemViewSwipeEnabled(): Boolean {
        return true
    }

    override fun isLongPressDragEnabled(): Boolean {
        return false
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean,
    ) {
        //super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE && viewHolder is UserRecyclerViewAdapter.CustomViewHolder) {
            val view = viewHolder.swipeView
            getDefaultUIUtil().onDraw(c, recyclerView, view, dX, dY, actionState, isCurrentlyActive)
        }
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        Log.d(TAG, "clearView: ")
        getDefaultUIUtil().clearView((viewHolder as UserRecyclerViewAdapter.CustomViewHolder).swipeView)
    }

}