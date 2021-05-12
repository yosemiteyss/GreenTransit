package com.yosemiteyss.greentransit.app.utils

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior

/**
 * Created by kevin on 12/5/2021
 */

class MapFloatingButtonBehavior(
    context: Context,
    attrs: AttributeSet?
) : CoordinatorLayout.Behavior<View>(context, attrs) {

    override fun layoutDependsOn(
        parent: CoordinatorLayout,
        child: View,
        dependency: View
    ): Boolean {
        val layoutParams = dependency.layoutParams
        return layoutParams is CoordinatorLayout.LayoutParams &&
            layoutParams.behavior is BottomSheetBehavior
    }

    override fun onDependentViewChanged(
        parent: CoordinatorLayout,
        child: View,
        dependency: View
    ): Boolean {
        if (dependency.y < parent.height) {
            child.y = (parent.height - child.height).toFloat()
        } else {
            child.y = dependency.y - child.height
        }

        return false
    }
}