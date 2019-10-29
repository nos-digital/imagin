package nl.nos.imagin.example.gallery

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.animation.DecelerateInterpolator
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