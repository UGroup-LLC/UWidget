package com.kikoproject.uwidget.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

fun bitmapCrop(
    srcBmp: Bitmap,
    widthCompress: Int = 1,
    heightCompress: Int = 1
): Bitmap { // Обрезает картику до квадрата
    val dstBmp: Bitmap
    if (srcBmp.getWidth() >= srcBmp.getHeight()) {

        dstBmp = Bitmap.createBitmap(
            srcBmp,
            srcBmp.getWidth() / 2 - srcBmp.getHeight() / 2,
            0,
            srcBmp.getHeight() / heightCompress,
            srcBmp.getHeight() / heightCompress
        );

    } else {

        dstBmp = Bitmap.createBitmap(
            srcBmp,
            0,
            srcBmp.getHeight() / 2 - srcBmp.getWidth() / 2,
            srcBmp.getWidth() / widthCompress,
            srcBmp.getWidth() / widthCompress
        );
    }
    return dstBmp
}

fun bitmapResize(dstBmp: Bitmap, height: Int = 200, width: Int = 200): Bitmap {
    return Bitmap.createScaledBitmap(dstBmp, width, height, false)
}

fun bitmapCompress(dstBmp: Bitmap, quality: Int = 30): Bitmap {
    val out = ByteArrayOutputStream()
    dstBmp.compress(Bitmap.CompressFormat.JPEG, quality, out)
    return BitmapFactory.decodeStream(ByteArrayInputStream(out.toByteArray()));
}