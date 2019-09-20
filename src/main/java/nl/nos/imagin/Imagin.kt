package nl.nos.imagin

import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import android.widget.ImageView

/**
 * Imagin is a library module that can be used to add several features (e.g. pinch to zoom,
 * double tap to zoom, etc.) to an ImageView. It can be used as simple as:
 * <code>Imagin.with(imageViewParent, imageView).enablePinchToZoom()</code>
 */
class Imagin private constructor(
        private val imageViewWrapper: View,
        private val imageView: ImageView,
        private val minZoom: Float,
        private val maxZoom: Float
) {
    private val touchListeners = mutableListOf<(View, MotionEvent) -> Boolean>()

    init {
        imageViewWrapper.setOnTouchListener { v, event ->
            var returnValue = false
            touchListeners.forEach { touchListener ->
                returnValue = touchListener.invoke(v, event) || returnValue
            }
            return@setOnTouchListener returnValue
        }
    }

    /**
     * Add pinch to zoom functionality to an ImageView.
     */
    fun enablePinchToZoom(): Imagin {
        val scaleDetector = ScaleGestureDetector(imageViewWrapper.context,
                PinchToZoomScaleGestureDetectorHandler(
                        imageView,
                        minZoom,
                        maxZoom
                )
        )
        touchListeners.add { _, event ->
            scaleDetector.onTouchEvent(event)
        }
        return this
    }

    /**
     * Add double-tap-to-zoom functionality to the image view, see @see [DoubleTapToZoomTouchHandler]
     */
    fun enableDoubleTapToZoom(): Imagin {
        val doubleTapToZoomListener = DoubleTapToZoomTouchHandler(imageView, minZoom, maxZoom)

        touchListeners.add { view, event ->
            doubleTapToZoomListener.onTouch(view, event)
        }
        return this
    }

    /**
     * Allow the user to move the image of the image view when zoomed in.
     *
     * @param allowScrollOutOfBoundsHorizontally Whether or not the image can be placed out of
     * it's bounds horizontally.
     * @param allowScrollOutOfBoundsVertically Whether or not the image can be placed out of it's
     * bounds vertically.
     * @param scrollDistanceToCloseInPx The distance in pixels that should be scrolled out of bounds
     * before the outOfBoundScrolledListener will be activated.
     * @param outOfBoundScrolledListener A listener that will be activated when the user has moved
     * the image as far as scrollDistanceToCloseInPx out of bounds.
     */
    fun enableScroll(
            allowScrollOutOfBoundsHorizontally: Boolean = true,
            allowScrollOutOfBoundsVertically: Boolean = true,
            scrollDistanceToCloseInPx: Int,
            outOfBoundScrolledListener: (() -> Unit)?
    ): Imagin {
        val scrollListener = ScrollHandler(
                imageView,
                allowScrollOutOfBoundsHorizontally,
                allowScrollOutOfBoundsVertically,
                scrollDistanceToCloseInPx,
                outOfBoundScrolledListener
        )

        touchListeners.add { view, event ->
            scrollListener.onTouch(view, event)
        }
        return this
    }

    fun enableSingleTap(onSingleTapListener: SingleTapHandler.OnSingleTapListener): Imagin {
        val singleTapHandler = SingleTapHandler(imageViewWrapper.context, onSingleTapListener)

        touchListeners.add { view, event ->
            singleTapHandler.onTouch(view, event)
        }
        return this
    }

    companion object {
        /**
         * Attach Imagin functionality to a ImageView by added a TouchListener to the
         * [imageViewWrapper]. The [imageViewWrapper] should not be the [imageView] or unexpected
         * behaviour will occur when the image is zoom in (scaled).
         */
        fun with(
                imageViewWrapper: View,
                imageView: ImageView,
                minZoom: Float = 1f,
                maxZoom: Float = 3f
        ) = Imagin(imageViewWrapper, imageView, minZoom, maxZoom)
    }
}