package nl.nos.imagin.example.gallery_vp2

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView

class NestedScrollableHost : FrameLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    private val childView: View? get() = if (childCount > 0) getChildAt(0) else null

    private fun canImageScroll(): Boolean {
        if (childView is ImageView?) {
            return (childView as? ImageView)?.edgeIsVisible() == false
        }
        return childView != this
    }

    private fun ImageView.edgeIsVisible(): Boolean {
        val maxTranslationXScale = (scaleX * width - width) / 2
        return translationX <= -maxTranslationXScale ||
                translationX >= maxTranslationXScale
    }

//    private fun ImageView.leftEdgeIsVisible(): Boolean {
//        val maxTranslationXScale = (scaleX * width - width) / 2
//        return translationX >= maxTranslationXScale
//    }
//
//    private fun ImageView.rightEdgeIsVisible(): Boolean {
//        val maxTranslationXScale = (scaleX * width - width) / 2
//        return translationX <= -maxTranslationXScale
//    }


    override fun onInterceptTouchEvent(e: MotionEvent): Boolean {
        handleInterceptTouchEvent(e)
        return super.onInterceptTouchEvent(e)
    }

    private fun handleInterceptTouchEvent(e: MotionEvent) {
        if (!canImageScroll()) return

        if (e.action == MotionEvent.ACTION_DOWN) {
            parent.requestDisallowInterceptTouchEvent(true)
        }
    }
}