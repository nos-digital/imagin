package nl.nos.imagin.example.gallery_vp2

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.gallery_item_vp2.*
import nl.nos.imagin.Imagin
import nl.nos.imagin.SingleTapHandler
import nl.nos.imagin.example.AssetLoader
import nl.nos.imagin.example.R
import nl.nos.imagin.example.data.Picture

class GalleryImageFragment : Fragment(), SingleTapHandler.OnSingleTapListener {
    private var tapListener: SingleTapHandler.OnSingleTapListener? = null
    private val assetLoader = AssetLoader()

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is SingleTapHandler.OnSingleTapListener) {
            tapListener = context
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.gallery_item_vp2, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val data = arguments?.getParcelable(IMAGE_DATA) as? Picture ?: return

        loadImage(data)
    }

    private fun loadImage(picture: Picture) {
        image_view.setImageDrawable(
            assetLoader.createDrawableFromAsset(
                image_view.resources,
                picture.fileName
            )
        )

        enableImageZoomControls(picture)
    }

    private fun enableImageZoomControls(picture: Picture) {
        view?.let {
            Imagin.with(it, image_view)
                .enableDoubleTapToZoom() // FIXME only works if enablePinchToZoom is also added to builder
                .enablePinchToZoom()
                .enableSingleTap(object : SingleTapHandler.OnSingleTapListener {
                    override fun onSingleTap() {
                        Toast.makeText(image_view.context, picture.name, Toast.LENGTH_SHORT).show()
                    }
                })
                .enableScroll(
                    allowScrollOutOfBoundsHorizontally = true,
                    allowScrollOutOfBoundsVertically = true,
                    scrollDistanceToCloseInPx = getScreenHeight(image_view.context) / 5
                ) {
                    activity?.finishAfterTransition()
                }

            image_view.transitionName = picture.name
            image_view.tag = picture.name
        }
    }

    private fun getScreenHeight(context: Context): Int {
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = windowManager.defaultDisplay
        val displayMetrics = DisplayMetrics()

        display.getMetrics(displayMetrics)
        return Rect(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels).height()
    }

    override fun onSingleTap() {
        tapListener?.onSingleTap()
    }

    companion object {
        const val IMAGE_DATA= "IMAGE_DATA"

        fun newInstance(image: Picture?): Fragment {
            val args = Bundle()
            args.putParcelable(IMAGE_DATA, image)
            val imageFragment =
                GalleryImageFragment()
            imageFragment.arguments = args
            return imageFragment
        }
    }
}
