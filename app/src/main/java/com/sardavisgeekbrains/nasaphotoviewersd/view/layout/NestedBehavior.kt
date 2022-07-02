package com.sardavisgeekbrains.nasaphotoviewersd.view.layout

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.appbar.AppBarLayout
import kotlin.math.abs

class NestedBehavior(
    context: Context,
    attrs: AttributeSet? = null
) : CoordinatorLayout.Behavior<View>(context, attrs) {


    override fun layoutDependsOn(
        parent: CoordinatorLayout,
        child: View,
        dependency: View
    ): Boolean {
        return (dependency is AppBarLayout)
    }

    override fun onDependentViewChanged(
        parent: CoordinatorLayout,
        child: View,
        dependency: View
    ): Boolean {
        val bar = dependency as AppBarLayout
       /* Log.d("", bar.y.toString())
        Log.d("", bar.height.toString())*/
        val alpha = 0.6 + abs(bar.y) / bar.height
        child.y = 0 + bar.height.toFloat() + bar.y

        if(alpha < 1){
            child.alpha = alpha.toFloat()
        } else{
            child.alpha = 1f
        }

        return super.onDependentViewChanged(parent, child, dependency)
    }

}