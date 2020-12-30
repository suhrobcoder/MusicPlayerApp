package uz.suhrob.musicplayerapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.bumptech.glide.RequestManager
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import uz.suhrob.musicplayerapp.R
import uz.suhrob.musicplayerapp.data.model.Song
import uz.suhrob.musicplayerapp.databinding.FragmentSongBinding
import uz.suhrob.musicplayerapp.exoplayer.isPlaying
import uz.suhrob.musicplayerapp.ui.BaseFragment
import uz.suhrob.musicplayerapp.ui.viewmodels.MainViewModel
import uz.suhrob.musicplayerapp.ui.viewmodels.SongViewModel
import javax.inject.Inject

@AndroidEntryPoint
class SongFragment: BaseFragment<FragmentSongBinding>() {
    private val mainViewModel by activityViewModels<MainViewModel>()
    private val songViewModel by viewModels<SongViewModel>()

    @Inject
    lateinit var glide: RequestManager

    private var pos = 0
    private lateinit var currentSong: Song
    private var isSeekbarTracking = false

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSongBinding = FragmentSongBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.songPos.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                pos = progress
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                isSeekbarTracking = true
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                mainViewModel.seekTo(pos.toLong())
                isSeekbarTracking = false
            }

        })
        binding.prevBtn.setOnClickListener {
            mainViewModel.skipToPreviousSong()
        }
        binding.nextBtn.setOnClickListener {
            mainViewModel.skipToNextSong()
        }
        binding.playBtn.setOnClickListener {
            mainViewModel.playOrToggleSong(currentSong, true)
        }
        mainViewModel.currentPlayingSong.observe(viewLifecycleOwner) {
            if (it == null) return@observe

            val _song = if (it.description.mediaId != null) Song(
                mediaId = it.description.mediaId.toString(),
                title = it.description.title.toString(),
                artist = it.description.subtitle.toString(),
                album = it.description.description.toString(),
                path = it.description.mediaUri.toString(),
                image = it.description.iconBitmap
            ) else null
            _song?.let { song ->
                Timber.d(song.toString())
                binding.songTitle.text = song.title
                binding.songSubtitle.text = song.artist
                currentSong = song
            }
            glide.load(_song?.image).into(binding.songImage)
        }
        songViewModel.currentPlayerPosition.observe(viewLifecycleOwner) {
            binding.songCurrentPos.text = formatTime(it)
            if (!isSeekbarTracking) {
                binding.songPos.progress = it.toInt()
            }
        }
        songViewModel.currentSongDuration.observe(viewLifecycleOwner) {
            binding.songPos.max = it.toInt()
            binding.songDuration.text = formatTime(it)
            Timber.d("song_duration $it")
        }
        mainViewModel.playbackState.observe(viewLifecycleOwner) {
            it?.let {
                if (it.isPlaying) {
                    binding.playBtn.setImageResource(R.drawable.ic_pause)
                } else {
                    binding.playBtn.setImageResource(R.drawable.ic_play)
                }
            }
        }
    }

    fun formatTime(time: Long): String {
        val minute = time / 1000 / 60
        val second = (time / 1000) % 60
        return "$minute:${if (second > 9) "" else "0"}$second"
    }
}