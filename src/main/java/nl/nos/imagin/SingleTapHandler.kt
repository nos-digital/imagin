package nl.nos.imagin

import android.content.Context
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View

class SingleTapHandler(
        context: Context,
        val onSingleTapListener: OnSingleTapListener
) : View.OnTouchListener {
    private val gestureDetector = GestureDetector(context,
            object : GestureDetector.SimpleOnGestureListener() {
                override fun onLongPress(e: MotionEvent) {}
            })

    private val tapListener = object : GestureDetector.OnDoubleTapListener {
        override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
            onSingleTapListener.onSingleTap()
            return false
        }

        override fun onDoubleTap(e: MotionEvent) = false
        override fun onDoubleTapEvent(e: MotionEvent) = false
    }

    init {
        gestureDetector.setOnDoubleTapListener(tapListener)
    }

    override fun onTouch(v: View?, event: MotionEvent?) = gestureDetector.onTouchEvent(event)

    /**
     * Simple listener for single view taps.
     */
    interface OnSingleTapListener {

        /**
         * The view has been tapped
         */
        fun onSingleTap()
    }
}

