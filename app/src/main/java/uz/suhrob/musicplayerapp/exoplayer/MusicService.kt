package uz.suhrob.musicplayerapp.exoplayer

import android.app.PendingIntent
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.session.MediaSessionCompat
import androidx.media.MediaBrowserServiceCompat
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import uz.suhrob.musicplayerapp.exoplayer.callbacks.MusicPlayerNotificationListener
import javax.inject.Inject

private const val SERVICE_TAG = "MusicService"

class MusicService : MediaBrowserServiceCompat() {

    @Inject
    lateinit var dataSourceFactory: DefaultDataSourceFactory

    @Inject
    lateinit var exoPlayer: SimpleExoPlayer

    private lateinit var musicNotificationManager: MusicNotificationManager

    private val serviceJob = Job()
    private val serviceScope = CoroutineScope(Dispatchers.Main + serviceJob)

    private lateinit var mediaSession: MediaSessionCompat
    private lateinit var mediaSessionConnector: MediaSessionConnector

    var isForegroundService = false

    override fun onCreate() {
        super.onCreate()
        val activityIndent = packageManager?.getLaunchIntentForPackage(packageName)?.let {
            PendingIntent.getActivity(this, 0, it, 0)
        }

        mediaSession = MediaSessionCompat(this, SERVICE_TAG).apply {
            setSessionActivity(activityIndent)
            isActive = true
        }

        sessionToken = mediaSession.sessionToken
        mediaSessionConnector = MediaSessionConnector(mediaSession)
        mediaSessionConnector.setPlayer(exoPlayer)

        musicNotificationManager = MusicNotificationManager(
            this,
            mediaSession.sessionToken,
            MusicPlayerNotificationListener(this)
        ) {

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }

    override fun onGetRoot(p0: String, p1: Int, p2: Bundle?): BrowserRoot? {
        TODO("Not yet implemented")
    }

    override fun onLoadChildren(p0: String, p1: Result<MutableList<MediaBrowserCompat.MediaItem>>) {
        TODO("Not yet implemented")
    }
}