package nl.nos.imagin.example.overview

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_view_picture.view.*
import nl.nos.imagin.example.R

class PictureViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.item_view_picture, parent, false)
) {
    val imageView = itemView.image_view
}