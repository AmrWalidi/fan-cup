package com.amrwalidi.android.fancup.bindingUtils

import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import com.amrwalidi.android.fancup.R

@BindingAdapter("helperDisable")
fun ConstraintLayout.helperDisable(disable: Boolean) {
    if (disable) {
        setBackgroundResource(R.drawable.disable_helper_container)
    }
}