package uz.suhrob.musicplayerapp.data.model

import android.graphics.Bitmap

data class Song(
    val mediaId: String,
    val path: String,
    val title: String,
    val album: String,
    val artist: String,
    val image: Bitmap?
)