package nl.nos.imagin

import android.view.ScaleGestureDetector
import android.widget.ImageView

/**
 * Add pinch to zoom functionality to an ImageView.
 */
class PinchToZoomScaleGestureDetectorHandler(
        private val imageView: ImageView,
        private val minZoom: Float,
        private val maxZoom: Float
) : ScaleGestureDetector.SimpleOnScaleGestureListener() {

    override fun onScale(detector: ScaleGestureDetector): Boolean {
        if (isMaxZoomedIn() && detector.scaleFactor >= 1f) {
            return true
        }

        var scaleFactor = imageView.scaleX * detector.scaleFactor

        // Don't let the object get too small or too large.
        scaleFactor = Math.max(minZoom, Math.min(scaleFactor, maxZoom))

        val imageSize = Calculator.calculateImageSize(imageView) ?: return false

        imageView.translationX = calculateNewTranslationMinMaxed(
                imageSize.first,
                detector.scaleFactor,
                detector.focusX,
                imageView.scaleX,
                scaleFactor,
                imageView.width,
                imageView.translationX
        )
        imageView.translationY = calculateNewTranslationMinMaxed(
                imageSize.second,
                detector.scaleFactor,
                detector.focusY,
                imageView.scaleY,
                scaleFactor,
                imageView.height,
                imageView.translationY
        )

        imageView.scaleX = scaleFactor
        imageView.scaleY = scaleFactor

        return true
    }

    private fun isMaxZoomedIn() = imageView.scaleX >= maxZoom

    fun calculateNewTranslationMinMaxed(
            imageSize: Int,
            detectedScaleFactor: Float,
            detectedFocus: Float,
            currentImageScale: Float,
            newImageScale: Float,
            imageViewSize: Int,
            imageTranslation: Float
    ) = minMaxTranslation(
            calculateNewTranslation(
                    imageSize,
                    detectedScaleFactor,
                    detectedFocus,
                    currentImageScale,
                    newImageScale,
                    imageViewSize,
                    imageTranslation
            ),
            newImageScale,
            imageSize,
            imageViewSize
    )

    /**
     * Calculate the new translation (x or y) value.
     */
    fun minMaxTranslation(
            translation: Float,
            newImageScale: Float,
            imageSize: Int,
            imageViewSize: Int
    ): Float {
        val maxTranslation =
                Calculator.calculateMaxTranslation(newImageScale, imageSize, imageViewSize)

        // Never pinch out of the bounds
        return Math.max(-maxTranslation, Math.min(translation, maxTranslation))
    }

    fun calculateTranslationScale(
            imageSize: Int,
            currentScale: Float,
            newScale: Float,
            imageViewSize: Int
    ): Float {
        if (currentScale <= 1.0f) return 0f

        return Calculator.calculateMaxTranslation(newScale, imageViewSize, imageSize) /
                Calculator.calculateMaxTranslation(currentScale, imageViewSize, imageSize)
    }

    /**
     * Calculate the new translation (x or y) value.
     */
    fun calculateNewTranslation(
            imageSize: Int,
            detectedScaleFactor: Float,
            detectedFocus: Float,
            currentImageScale: Float,
            newImageScale: Float,
            imageViewSize: Int,
            imageTranslation: Float
    ): Float {
        val relativeTranslation = (detectedFocus / imageViewSize - 0.5f) * 2f * newImageScale

        var newImageTranslation = imageTranslation
        return when {
            detectedScaleFactor == 1f -> imageTranslation
            detectedScaleFactor > 1f -> {
                newImageTranslation -= (detectedScaleFactor - 1f) * relativeTranslation * imageViewSize
                newImageTranslation
            }
            else -> return imageTranslation *
                    calculateTranslationScale(
                            imageSize,
                            currentImageScale,
                            newImageScale,
                            imageViewSize
                    )
        }
    }
}