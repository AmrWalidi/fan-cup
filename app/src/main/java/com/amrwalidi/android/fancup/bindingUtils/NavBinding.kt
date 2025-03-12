package com.amrwalidi.android.fancup.bindingUtils

import android.content.Context
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.ActionBar.LayoutParams
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.amrwalidi.android.fancup.R

@BindingAdapter("setStatus")
fun ConstraintLayout.setStatus(selected: Boolean) {

    val layoutParams = layoutParams ?: return

    if (selected) {
        setBackgroundResource(R.drawable.selected_page_background)

        layoutParams.height = dpToPx(50, context)
        layoutParams.width = dpToPx(50, context)
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
    if (selected) {
        this.drawable?.setTint(ContextCompat.getColor(context, R.color.white))

    } else {
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

@BindingAdapter("setLeftArrowVisible")
fun ImageView.setLeftArrowVisible(page: Int) {
    this.visibility = if (page == 1) View.INVISIBLE else View.VISIBLE

}
