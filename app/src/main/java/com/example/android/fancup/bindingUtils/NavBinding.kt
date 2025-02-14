package com.example.android.fancup.bindingUtils

import android.content.Context
import android.util.TypedValue
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.ActionBar.LayoutParams
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.example.android.fancup.R

@BindingAdapter("setStatus")
fun ConstraintLayout.setStatus(selected: Boolean) {

    val layoutParams = layoutParams ?: return

    if (selected) {
        setBackgroundResource(R.drawable.selected_page_background)

        layoutParams.height = dpToPx(55,context)
        layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT
        this.layoutParams = layoutParams

        val paddingPx =
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5f, resources.displayMetrics)
                .toInt()
        setPadding(paddingPx, paddingPx, paddingPx, paddingPx)

    } else {
        setBackgroundResource(0)
        layoutParams.height = LayoutParams.WRAP_CONTENT
        layoutParams.width = LayoutParams.WRAP_CONTENT
        this.layoutParams = layoutParams
        setPadding(0, 0, 0, 0)
    }
}

@BindingAdapter("setStatus")
fun ImageView.setStatus(selected: Boolean) {

    val layoutParams = layoutParams ?: return
    if (selected) {
        layoutParams.height = dpToPx(48,context)
        layoutParams.width = dpToPx(48,context)
        this.layoutParams = layoutParams

        this.drawable?.setTint(ContextCompat.getColor(context, R.color.white))

    } else{
        layoutParams.height =  ViewGroup.LayoutParams.WRAP_CONTENT
        layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT
        this.layoutParams = layoutParams
        this.drawable?.setTintList(null)
    }
}

fun dpToPx(dp: Int, context: Context): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dp.toFloat(),
        context.resources.displayMetrics
    ).toInt()
}