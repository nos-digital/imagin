package nl.nos.imagin

import android.widget.ImageView
import kotlin.math.max

class Calculator {
    fun calculateMaxTranslation(
            scale: Float,
            imageSize: Int,
            imageViewSize: Int
    ) = max(0f, (scale * imageSize - imageViewSize) / 2)

    fun calculateImageSize(imageView: ImageView): Pair<Int, Int>? {
        if (imageView.drawable == null) return null

        val widthScale = imageView.drawable.intrinsicWidth.toFloat() / imageView.width
        val heightScale = imageView.drawable.intrinsicHeight.toFloat() / imageView.height

        return if (widthScale > heightScale) {
            Pair(imageView.width, (imageView.drawable.intrinsicHeight / widthScale).toInt())
        } else {
            Pair((imageView.drawable.intrinsicWidth / heightScale).toInt(), imageView.height)
        }
    }
}