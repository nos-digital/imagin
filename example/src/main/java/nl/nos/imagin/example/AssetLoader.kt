package nl.nos.imagin.example

import android.content.res.Resources
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable

class AssetLoader {

    fun createDrawableFromAsset(resources: Resources, assetFileName: String): Drawable {
        val inputStream = resources.assets.open(assetFileName)
        val bitmap = BitmapFactory.decodeStream(inputStream)
        return BitmapDrawable(resources, bitmap)
    }
}