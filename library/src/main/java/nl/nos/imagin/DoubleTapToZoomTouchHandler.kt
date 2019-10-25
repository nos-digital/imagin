package nl.nos.imagin

import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView

/**
 * Add this [View.OnTouchListener] to a view and this will do a animated zoom to [minZoom] or
 * [maxZoom] on a double tap gesture. If the image is not yet fully zoomed in, we will zoom in to
 * [maxZoom], otherwise we will zoom back to [minZoom].
 */
class DoubleTapToZoomTouchHandler(
        val imageView: ImageView,
        val minZoom: Float,
        val maxZoom: Float
) : View.OnTouchListener {

    private val calculator = Calculator()

    private val gestureDetector = GestureDetector(imageView.context,
            object : GestureDetector.SimpleOnGestureListener() {
                override fun onLongPress(e: MotionEvent) {}

                override fun onDoubleTap(e: MotionEvent): Boolean {
                    // Zoom to minZoom if the current scale is already fully zoomed in, else zoom to the
                    // maxZoom value.
                    val futureScale = if (imageView.scaleX >= maxZoom) minZoom else maxZoom

                    val imageSize = calculator.calculateImageSize(imageView) ?: return false

                    val translationX = calculateFutureTranslation(
                            imageSize.first,
                            futureScale,
                            e.rawX,
                            imageView.translationX,
                            imageView.width,
                            imageView.scaleX
                    )
                    val translationY = calculateFutureTranslation(
                            imageSize.second,
                            futureScale,
                            e.rawY,
                            imageView.translationY,
                            imageView.height,
                            imageView.scaleY
                    )

                    // Zoom in or out after a double tap
                    isAnimating = true
                    imageView.animate()
                            .scaleX(futureScale)
                            .scaleY(futureScale)
                            .translationX(translationX)
                            .translationY(translationY)
                            .withEndAction { isAnimating = false }
                            .start()
                    return true
                }

                /**
                 * Calculate the translation (x or y) we want to animate to after a double tap.
                 *
                 * @param futureScale The scale we want to animate to
                 * @param relativeTouchPoint The x or y value of the double tap, relative to the current
                 * visible viewport.
                 * @param currentTranslation The current translation (x or y) value of the image view.
                 * @param currentViewSize The current image view size (width or height).
                 * @param currentViewScale The current image view scale (x or y).
                 */
                fun calculateFutureTranslation(
                        imageSize: Int,
                        futureScale: Float,
                        relativeTouchPoint: Float,
                        currentTranslation: Float,
                        currentViewSize: Int,
                        currentViewScale: Float
                ): Float {
                    /**
                     * The maximum and minimum translation the ImageView should have (with the futureScale),
                     * so the View is still visible.
                     */
                    val maxTranslation = calculator
                            .calculateMaxTranslation(futureScale, imageSize, currentViewSize)
                    val minTranslation = -maxTranslation

                    // The maximum translation at this moment.
                    val currentMaxTranslation = (currentViewScale - 1) / 2 * currentViewSize

                    /**
                     * Calculate the [relativeTouchPoint] back to how it would be if the scale was 1.0,
                     * so it is relative to the image view size.
                     */
                    val touchPointRelativeToImageView
                            = ((-currentTranslation) + currentMaxTranslation + relativeTouchPoint) /
                    currentViewScale

                    /**
                     * Now that we have the touchPointRelativeToImageView, we can calculate what this
                     * would be with within the new translation values.
                     * with "touchPointRelativeToImageView / currentViewSize" we calculate the x in a
                     * number between 0.0 and 1.0.
                     * with "maxTranslation - minTranslation" we calculate the difference between the
                     * lowest possible translation value and the highest possible value, the answer is
                     * somewhere in between. We multiply this with the previous number.
                     * then with "- maxTranslation" we correct this, because the minimum translation is
                     * not 0, but somewhere lower then that.
                     */
                    return -(
                            (touchPointRelativeToImageView / currentViewSize) * (maxTranslation - minTranslation)
                                    - maxTranslation
                            )
                }

                override fun onDoubleTapEvent(e: MotionEvent): Boolean {
                    return false
                }
            }
    )

    override fun onTouch(v: View?, event: MotionEvent?) = gestureDetector.onTouchEvent(event)

    companion object {
        var isAnimating = false
    }
}