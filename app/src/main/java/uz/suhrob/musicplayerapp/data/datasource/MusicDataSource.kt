package uz.suhrob.musicplayerapp.data.datasource

import android.content.Context
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.provider.MediaStore
import androidx.loader.content.CursorLoader
import uz.suhrob.musicplayerapp.data.model.Song

class MusicDataSource(
    private val context: Context
) {
    fun getAllSongs(): List<Song> {
        val list = ArrayList<Song>()
        val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val selection = MediaStore.Audio.Media.IS_MUSIC  + " != 0"
        val sortOrder = MediaStore.Audio.Media.TITLE
        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.ARTIST
        )
        val cursorLoader = CursorLoader(context, uri, projection, selection, null, sortOrder)
        val cursor = cursorLoader.loadInBackground()
        if (cursor != null) {
            while (cursor.moveToNext()) {
                val mmr = MediaMetadataRetriever()
                mmr.setDataSource(cursor.getString(1))
                val data = mmr.embeddedPicture
                val bitmap = data?.let {
                    BitmapFactory.decodeByteArray(it, 0, it.size)
                }
                list.add(
                    Song(
                        mediaId = cursor.getString(0),
                        path = cursor.getString(1),
                        title = cursor.getString(2),
                        album = cursor.getString(3),
                        artist = cursor.getString(4),
                        image = bitmap
                    )
                )
            }
        }
        cursor?.close()
        return list
    }
}