package nl.nos.imagin.example.gallery_vp1

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.app.SharedElementCallback
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import kotlinx.android.synthetic.main.activity_gallery.*
import nl.nos.imagin.example.R
import nl.nos.imagin.example.data.Repository

class GalleryActivity : AppCompatActivity(), GalleryPagerAdapter.OnSwipedToCloseListener {

    private val repository = Repository()
    private val adapter = GalleryPagerAdapter().apply {
        onSwipedToCloseListener = this@GalleryActivity
    }
    private val position by lazy { intent.getIntExtra(EXTRA_POSITION, -1) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_gallery)

        window.sharedElementEnterTransition.interpolator = DecelerateInterpolator(3f)
        supportPostponeEnterTransition()

        adapter.pictures.addAll(repository.getPictures())

        view_pager.adapter = adapter
        view_pager.setCurrentItem(position, false)

        view_pager.post {
            supportStartPostponedEnterTransition()
        }

        // Map the shared element transition for when the page has changed.
        setEnterSharedElementCallback(object : SharedElementCallback() {
            override fun onMapSharedElements(
                names: MutableList<String>,
                sharedElements: MutableMap<String, View>
            ) {
                if (names.isEmpty()) return

                val selectedPictureName = repository.getPictures()[view_pager.currentItem].name

                val imageView = view_pager.findViewWithTag<ImageView>(
                    selectedPictureName
                ) ?: return

                names[0] = imageView.transitionName
                sharedElements.clear()
                sharedElements[imageView.transitionName] = imageView
            }
        })
    }

    override fun onSwipeToClose() {
        finishAfterTransition()
    }

    companion object {
        private const val EXTRA_POSITION = "position"

        fun createIntent(context: Context, position: Int) =
            Intent(context, GalleryActivity::class.java).apply {
                putExtra(EXTRA_POSITION, position)
            }
    }
}