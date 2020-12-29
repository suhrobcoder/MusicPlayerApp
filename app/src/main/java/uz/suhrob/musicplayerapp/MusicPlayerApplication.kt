package uz.suhrob.musicplayerapp

import android.app.Application
import timber.log.Timber

class MusicPlayerApplication :Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}