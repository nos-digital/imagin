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

    private val gestureDetector = GestureDetector(imageView.context,
            object : GestureDetector.SimpleOnGestureListener() {
                override fun onLongPress(e: MotionEvent) {}

                override fun onDown(e: MotionEvent?): Boolean {
                    return true
                }

                override fun onDoubleTap(e: MotionEvent): Boolean {
                    // Zoom to minZoom if the current scale is already fully zoomed in, else zoom to the
                    // maxZoom value.
                    val futureScale = if (imageView.scaleX >= maxZoom) minZoom else maxZoom

                    val imageSize = Calculator.calculateImageSize(imageView) ?: return false

                    val translationX = Calculator.calculateFutureTranslation(
                        imageSize.first,
                        futureScale,
                        e.rawX,
                        imageView.translationX,
                        imageView.width,
                        imageView.scaleX
                    )
                    val translationY = Calculator.calculateFutureTranslation(
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

                override fun onDoubleTapEvent(e: MotionEvent): Boolean {
                    return false
                }
            }
    )

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        if (event == null) return false

        return gestureDetector.onTouchEvent(event)
    }

    companion object {
        var isAnimating = false
    }
}