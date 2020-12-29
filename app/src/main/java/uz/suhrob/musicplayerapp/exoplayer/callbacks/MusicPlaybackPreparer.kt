package uz.suhrob.musicplayerapp.exoplayer.callbacks

import android.net.Uri
import android.os.Bundle
import android.os.ResultReceiver
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.PlaybackStateCompat
import com.google.android.exoplayer2.ControlDispatcher
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector
import uz.suhrob.musicplayerapp.exoplayer.MusicDataSource

class MusicPlaybackPreparer(
    private val musicDataSource: MusicDataSource,
    private val playerPrepared: (MediaMetadataCompat?) -> Unit
) : MediaSessionConnector.PlaybackPreparer {
    override fun onCommand(
        p0: Player,
        p1: ControlDispatcher,
        p2: String,
        p3: Bundle?,
        p4: ResultReceiver?
    ): Boolean = false

    override fun getSupportedPrepareActions(): Long {
        return PlaybackStateCompat.ACTION_PREPARE_FROM_MEDIA_ID or PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID
    }

    override fun onPrepare(p0: Boolean) = Unit

    override fun onPrepareFromMediaId(p0: String, p1: Boolean, p2: Bundle?) {
        musicDataSource.whenReady {
            val itemToPlay = musicDataSource.songs.find { p0 == it.description.mediaId }
            playerPrepared(itemToPlay)
        }
    }

    override fun onPrepareFromSearch(p0: String, p1: Boolean, p2: Bundle?) = Unit

    override fun onPrepareFromUri(p0: Uri, p1: Boolean, p2: Bundle?) = Unit
}