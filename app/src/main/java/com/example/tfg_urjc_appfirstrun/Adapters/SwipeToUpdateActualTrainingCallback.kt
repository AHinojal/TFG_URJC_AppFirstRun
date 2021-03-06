package com.example.tfg_urjc_appfirstrun.Adapters

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.tfg_urjc_appfirstrun.R

class SwipeToUpdateActualTrainingCallback(var adapter: MyTrainingRecyclerViewAdapter, val context: Context):ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {

    private var icon: Drawable? = ContextCompat.getDrawable(context, R.drawable.ic_check);
    private var background: ColorDrawable? = ColorDrawable(Color.parseColor("#00E676"))

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        TODO("Not yet implemented")
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        var pos: Int = viewHolder.adapterPosition
        adapter.updateActualTraining(pos, viewHolder)
    }

    override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
        super.onChildDraw(c, recyclerView!!, viewHolder, dX, dY, actionState, isCurrentlyActive)
        val itemView: View = viewHolder.itemView
        val backgroundCornerOffset = 20 //so background is behind the rounded corners of itemView
        val iconMargin: Int = (itemView.getHeight() - icon!!.intrinsicHeight) / 2
        val iconTop: Int = itemView.getTop() + (itemView.getHeight() - icon!!.intrinsicHeight) / 2
        val iconBottom = iconTop + icon!!.intrinsicHeight
        if (dX > 0) { // Swiping to the right
            val iconLeft = itemView.left + iconMargin
            val iconRight = itemView.left + iconMargin + icon!!.intrinsicWidth
            icon!!.setBounds(iconLeft, iconTop, iconRight, iconBottom)
            background!!.setBounds(itemView.getLeft(), itemView.getTop(),
                    itemView.getLeft() + dX.toInt() + backgroundCornerOffset, itemView.getBottom())
        } else if (dX < 0) { // Swiping to the left
            val iconLeft: Int = itemView.getRight() - iconMargin - icon!!.intrinsicWidth
            val iconRight: Int = itemView.getRight() - iconMargin
            icon!!.setBounds(iconLeft, iconTop, iconRight, iconBottom)
            background!!.setBounds(itemView.getRight() + dX.toInt() - backgroundCornerOffset,
                    itemView.getTop(), itemView.getRight(), itemView.getBottom())
        } else { // view is unSwiped
            background!!.setBounds(0, 0, 0, 0)
        }
        background!!.draw(c)
        icon!!.draw(c)
    }
}