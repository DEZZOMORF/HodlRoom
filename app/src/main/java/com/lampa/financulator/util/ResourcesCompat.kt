package com.lampa.financulator.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.TypedValue
import androidx.annotation.*
import androidx.appcompat.content.res.AppCompatResources

class ResourcesCompat(val context: Context) {

    fun pixelsFromDimension(
        @DimenRes
        dimension: Int
    ): Int {
        return context.resources.getDimensionPixelSize(dimension)
    }

    fun getImage(imageUri: Uri): Bitmap {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            ImageDecoder.decodeBitmap(ImageDecoder.createSource(context.contentResolver, imageUri))
        } else {
            @Suppress("Deprecation")
            MediaStore.Images.Media.getBitmap(context.contentResolver, imageUri)
        }
    }

    fun getString(
        @StringRes
        resId: Int
    ): String {
        return context.getString(resId)
    }

    fun getString(
        @StringRes
        resId: Int,
        vararg formatArgs: Any
    ): String {
        return context.getString(resId, *formatArgs)
    }

    fun getStrings(
        @ArrayRes
        resId: Int
    ): Array<String> {
        return context.resources.getStringArray(resId)
    }

    fun getStringArrayElement(
        @ArrayRes
        arrayId: Int,
        index: Int
    ): String {
        return context.resources.getStringArray(arrayId)[index]
    }

    fun getDrawable(
        @DrawableRes
        resId: Int
    ): Drawable? {
        return AppCompatResources.getDrawable(context, resId)
    }

    @ColorInt
    fun getColor(
        @ColorRes
        id: Int
    ): Int {
        return context.getColor(id)
        // or: ContextCompat.getColor(this, id)
    }

    fun getColorFromAttr(
        @AttrRes
        attrColor: Int
    ): Int {
        val typedValue = TypedValue()
        context.theme.resolveAttribute(attrColor, typedValue, true)
        return typedValue.data
    }
}
