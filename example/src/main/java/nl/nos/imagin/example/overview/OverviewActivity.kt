package nl.nos.imagin.example.overview

import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.app.SharedElementCallback
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.view.View
import kotlinx.android.synthetic.main.activity_overview.*
import nl.nos.imagin.example.data.Picture
import nl.nos.imagin.example.data.Repository
import nl.nos.imagin.example.gallery.GalleryActivity

class OverviewActivity : AppCompatActivity(), OverviewAdapter.OnPictureClickedListener {

    private val repository = Repository()
    private val adapter = OverviewAdapter().apply {
        onPictureClickedListener = this@OverviewActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(nl.nos.imagin.example.R.layout.activity_overview)

        adapter.pictures.addAll(repository.getPictures())

        recycler_view.layoutManager = GridLayoutManager(this, 2)
        recycler_view.adapter = adapter

        // Map the shared element transition for when the page has changed.
        setExitSharedElementCallback(object : SharedElementCallback() {
            override fun onMapSharedElements(
                names: MutableList<String>,
                sharedElements: MutableMap<String, View>
            ) {
                if (names.isEmpty()) return

                val selectedView = recycler_view.findViewWithTag<View>(names[0]) ?: return
                sharedElements[names[0]] = selectedView
            }
        })
    }

    override fun onPictureClicked(view: View, picture: Picture, position: Int) {
        startActivity(
            GalleryActivity.createIntent(this, position),
            ActivityOptionsCompat.makeSceneTransitionAnimation(
                this,
                view,
                view.transitionName
            ).toBundle()
        )
    }
}
