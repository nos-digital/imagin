package nl.nos.imagin.example.gallery

import android.content.Context
import android.graphics.Rect
import android.support.v4.view.PagerAdapter
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import kotlinx.android.synthetic.main.gallery_item.view.*
import nl.nos.imagin.Imagin
import nl.nos.imagin.SingleTapHandler
import nl.nos.imagin.example.AssetLoader
import nl.nos.imagin.example.R
import nl.nos.imagin.example.data.Picture

class GalleryPagerAdapter : PagerAdapter() {

    private val assetLoader = AssetLoader()

    val pictures = mutableListOf<Picture>()
    var onSwipedToCloseListener: OnSwipedToCloseListener? = null

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val imageWrapper: ViewGroup = LayoutInflater.from(container.context)
            .inflate(R.layout.gallery_item, container, false) as ViewGroup
        val imageView = imageWrapper.image_view

        val picture = pictures[position]

        imageView.setImageDrawable(
            assetLoader.createDrawableFromAsset(
                imageWrapper.resources,
                picture.fileName
            )
        )

        Imagin.with(imageWrapper, imageView)
            .enableDoubleTapToZoom()
            .enablePinchToZoom()
            .enableSingleTap(object : SingleTapHandler.OnSingleTapListener {
                override fun onSingleTap() {
                    Toast.makeText(imageView.context, picture.name, Toast.LENGTH_SHORT).show()
                }
            })
            .enableScroll(
                allowScrollOutOfBoundsHorizontally = false,
                allowScrollOutOfBoundsVertically = true,
                scrollDistanceToCloseInPx = getScreenHeight(imageView.context) / 5
            ) {
                onSwipedToCloseListener?.onSwipeToClose()
            }

        imageView.transitionName = picture.name

        container.addView(imageWrapper)

        return imageWrapper
    }

    private fun getScreenHeight(context: Context): Int {
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = windowManager.defaultDisplay
        val displayMetrics = DisplayMetrics()

        display.getMetrics(displayMetrics)
        return Rect(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels).height()
    }

    override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
        container.removeView(obj as View)
    }

    override fun isViewFromObject(view: View, obj: Any): Boolean {
        return view == obj
    }

    override fun getCount() = pictures.size

    interface OnSwipedToCloseListener {
        fun onSwipeToClose()
    }
}
