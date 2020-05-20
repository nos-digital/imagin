package nl.nos.imagin

import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView

/**
 * Allow the user to move the image of the image view when zoomed in.
 *
 * @param imageView the ImageView that should be scrolled in.
 * @param allowScrollOutOfBoundsHorizontally Whether or not the image can be placed out of
 * it's bounds horizontally.
 * @param allowScrollOutOfBoundsVertically Whether or not the image can be placed out of it's
 * bounds vertically.
 * @param scrollDistanceToCloseInPx The distance that should be scrolled in pixels out of bounds
 * before the outOfBoundScrolledListener will be activated.
 * @param outOfBoundScrolledListener A listener that will be activated when the user has moved
 * the image as far as scrollDistanceToCloseInDp out of bounds.
 */
class ScrollHandler(
        private val imageView: ImageView,
        private val allowScrollOutOfBoundsHorizontally: Boolean,
        private val allowScrollOutOfBoundsVertically: Boolean,
        private val scrollDistanceToCloseInPx: Int,
        private val outOfBoundScrolledListener: (() -> Unit)?
) : View.OnTouchListener {

    private val calculator = Calculator()

    private val gestureDetector = GestureDetector(imageView.context,
            object : GestureDetector.SimpleOnGestureListener() {

                override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean {
                    if (imageView.rightEdgeIsVisible() && distanceX > 0) {
                        imageView.parent?.requestDisallowInterceptTouchEvent(false)
                    } else if (imageView.leftEdgeIsVisible() && distanceX < 0) {
                        imageView.parent?.requestDisallowInterceptTouchEvent(false)
                    }

                    imageView.translationX -= distanceX
                    imageView.translationY -= distanceY

                    if (!allowScrollOutOfBoundsHorizontally) {
                        // Prevent scrolling the image out of bounds horizontally
                        val maxTranslationXScale =
                                (imageView.scaleX * imageView.width - imageView.width) / 2
                        imageView.translationX = Math.max(
                                -maxTranslationXScale,
                                Math.min(imageView.translationX, maxTranslationXScale)
                        )
                    }
                    if (!allowScrollOutOfBoundsVertically) {
                        // Prevent scrolling the image out of bounds horizontally
                        val maxTranslationYScale =
                                (imageView.scaleY * imageView.height - imageView.height) / 2
                        imageView.translationY = Math.max(
                                -maxTranslationYScale,
                                Math.min(imageView.translationY, maxTranslationYScale)
                        )
                    }

                    return super.onScroll(e1, e2, distanceX, distanceY)
                }
            })

    private fun ImageView.leftEdgeIsVisible(): Boolean {
        val maxTranslationXScale = (scaleX * width - width) / 2
        return translationX >= maxTranslationXScale
    }

    private fun ImageView.rightEdgeIsVisible(): Boolean {
        val maxTranslationXScale = (scaleX * width - width) / 2
        return translationX <= -maxTranslationXScale
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        imageView.parent?.requestDisallowInterceptTouchEvent(true)

        val consumed = gestureDetector.onTouchEvent(event)


        if (event?.action == MotionEvent.ACTION_UP || event?.action == MotionEvent.ACTION_CANCEL) {
            val imageSize = calculator.calculateImageSize(imageView) ?: return consumed

            if (allowScrollOutOfBoundsHorizontally && shouldTriggerOutOfBoundListener(
                            scrollDistanceToCloseInPx,
                            imageView.translationX,
                            imageView.scaleX,
                            imageView.width,
                            imageSize.first
                    )) {
                outOfBoundScrolledListener?.invoke()
                return consumed
            }
            if (allowScrollOutOfBoundsVertically && shouldTriggerOutOfBoundListener(
                            scrollDistanceToCloseInPx,
                            imageView.translationY,
                            imageView.scaleY,
                            imageView.height,
                            imageSize.second
                    )) {
                outOfBoundScrolledListener?.invoke()
                return consumed
            }

            val newTranslationX = calculateNewTranslation(
                    imageSize.first,
                    imageView.width,
                    imageView.scaleX,
                    imageView.translationX
            )
            val newTranslationY = calculateNewTranslation(
                    imageSize.second,
                    imageView.height,
                    imageView.scaleY,
                    imageView.translationY
            )

            if ((newTranslationX != null || newTranslationY != null) &&
                    !DoubleTapToZoomTouchHandler.isAnimating) {
                var animator = imageView.animate()
                if (newTranslationX != null) {
                    animator = animator.translationX(newTranslationX)
                }
                if (newTranslationY != null) {
                    animator = animator.translationY(newTranslationY)
                }
                animator.start()
            }
        }

        return false
    }

    private fun calculateNewTranslation(
            imageSize: Int,
            imageViewSize: Int,
            scale: Float,
            currentTranslation: Float
    ): Float? {
        val maxTranslation =
                calculator.calculateMaxTranslation(
                        scale,
                        imageSize,
                        imageViewSize
                )
        var newTranslation: Float? = null
        // animate the image back to stay in the viewport
        if (currentTranslation < -maxTranslation) {
            newTranslation = -maxTranslation
        } else if (currentTranslation > maxTranslation) {
            newTranslation = maxTranslation
        }
        return newTranslation
    }

    /**
     * Whether the listener should be triggered or not.
     */
    private fun shouldTriggerOutOfBoundListener(
            distanceToClose: Int,
            imageViewTranslation: Float,
            imageViewScale: Float,
            imageViewSize: Int,
            imageSize: Int
    ): Boolean {
        if (imageViewScale > 1f) return false

        val maxTranslation =
                calculator.calculateMaxTranslation(imageViewScale, imageSize, imageViewSize)
        return imageViewTranslation < -(maxTranslation + distanceToClose) ||
                imageViewTranslation > maxTranslation + distanceToClose
    }
}