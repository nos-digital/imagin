package nl.nos.imagin.example.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Picture(
    val name: String,
    val fileName: String
) : Parcelable