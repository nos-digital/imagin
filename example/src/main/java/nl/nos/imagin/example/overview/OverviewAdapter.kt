package nl.nos.imagin.example.overview

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import nl.nos.imagin.example.AssetLoader
import nl.nos.imagin.example.data.Picture


class OverviewAdapter : RecyclerView.Adapter<PictureViewHolder>() {
    private val assetLoader = AssetLoader()

    val pictures = mutableListOf<Picture>()
    var onPictureClickedListener: OnPictureClickedListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = PictureViewHolder(parent)

    override fun getItemCount() = pictures.size

    override fun onBindViewHolder(viewHolder: PictureViewHolder, position: Int) {
        val picture = pictures[position]

        viewHolder.imageView.setImageDrawable(
            assetLoader.createDrawableFromAsset(
                viewHolder.imageView.resources,
                picture.fileName
            )
        )

        viewHolder.itemView.transitionName = picture.name
        viewHolder.itemView.tag = picture.name

        viewHolder.itemView.setOnClickListener {
            onPictureClickedListener?.onPictureClicked(viewHolder.itemView, picture, position)
        }
    }

    interface OnPictureClickedListener {
        fun onPictureClicked(view: View, picture: Picture, position: Int)
    }
}