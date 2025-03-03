package com.amrwalidi.android.fancup.bindingUtils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.databinding.BindingAdapter
import com.google.android.material.imageview.ShapeableImageView

@BindingAdapter("imageByteArray")
fun ShapeableImageView.imageByteArray(byteArray: ByteArray?) {
    byteArray?.let {
        val bitmap: Bitmap = BitmapFactory.decodeByteArray(it, 0, it.size)
        this.setImageBitmap(bitmap)
    }
}
