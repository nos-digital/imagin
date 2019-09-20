package nl.nos.imagin

import android.widget.ImageView

/**
 * @author mennovogel (22/02/2019).
 */
class Calculator {
    fun calculateMaxTranslation(
            scale: Float,
            imageSize: Int,
            imageViewSize: Int
    ) = Math.max(0f, (scale * imageSize - imageViewSize) / 2)

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