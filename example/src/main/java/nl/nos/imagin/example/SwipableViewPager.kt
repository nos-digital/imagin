package nl.nos.imagin.example

import android.content.Context
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView

/**
 * This ViewPager allows us to move a zoomed-in image to the edge, before we move to the next page.
 */
class SwipableViewPager : ViewPager {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    override fun canScroll(v: View, checkV: Boolean, dx: Int, x: Int, y: Int): Boolean {
        if (v is ImageView) {
            return !v.edgeIsVisible()
        }

        return super.canScroll(v, checkV, dx, x, y)
    }

    private fun ImageView.edgeIsVisible(): Boolean {
        // Calculate what the translationX value should be when the left or right edge of the image
        // is visible.
        val maxTranslationXScale = (scaleX * width - width) / 2

        return translationX <= -maxTranslationXScale ||
                translationX >= maxTranslationXScale
    }
}