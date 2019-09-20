package nl.nos.imagin

import android.widget.ImageView

/**
 * Kotlin extension functions for [ImageView]
 */
fun ImageView.edgeIsVisible(): Boolean {
    // Calculate what the translationX value should be when the left or right edge of the image
    // is visible.
    val maxTranslationXScale = (scaleX * width - width) / 2

    return translationX <= -maxTranslationXScale ||
            translationX >= maxTranslationXScale
}