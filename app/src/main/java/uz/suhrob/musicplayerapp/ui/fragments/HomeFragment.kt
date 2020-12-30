package uz.suhrob.musicplayerapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.RequestManager
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import uz.suhrob.musicplayerapp.R
import uz.suhrob.musicplayerapp.data.model.Song
import uz.suhrob.musicplayerapp.databinding.FragmentHomeBinding
import uz.suhrob.musicplayerapp.exoplayer.isPlaying
import uz.suhrob.musicplayerapp.other.Resource
import uz.suhrob.musicplayerapp.ui.BaseFragment
import uz.suhrob.musicplayerapp.ui.adapters.SongAdapter
import uz.suhrob.musicplayerapp.ui.viewmodels.MainViewModel
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment: BaseFragment<FragmentHomeBinding>() {
    private val viewModel by activityViewModels<MainViewModel>()

    @Inject
    lateinit var songAdapter: SongAdapter

    @Inject
    lateinit var glide: RequestManager

    private var currentPlayingSong: Song? = null

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentHomeBinding = FragmentHomeBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        songAdapter.clickListener = { song ->
            viewModel.playOrToggleSong(song)
        }
        binding.songsRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = songAdapter
        }
        binding.playBtn.setOnClickListener {
            currentPlayingSong?.let {
                viewModel.playOrToggleSong(it, true)
            }
        }
        binding.bottomLayout.setOnClickListener {
            findNavController().navigate(R.id.songFragment)
        }
        viewModel.mediaItems.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    songAdapter.submitList(it.data)
                    Timber.d("Songs ${it.data.size}")
                }
            }
        }
        viewModel.playbackState.observe(viewLifecycleOwner) {
            it?.let {
                if (it.isPlaying) {
                    binding.playBtn.setImageResource(R.drawable.ic_pause)
                } else {
                    binding.playBtn.setImageResource(R.drawable.ic_play)
                }
            }
        }
        viewModel.currentPlayingSong.observe(viewLifecycleOwner) {
            if (it == null) return@observe

            currentPlayingSong = if (it.description.mediaId != null) Song(
                mediaId = it.description.mediaId.toString(),
                title = it.description.title.toString(),
                artist = it.description.subtitle.toString(),
                album = it.description.description.toString(),
                path = it.description.mediaUri.toString(),
                image = it.description.iconBitmap
            ) else null
            currentPlayingSong?.let { song ->
                Timber.d(song.toString())
                binding.songTitle.text = song.title
                binding.songSubtitle.text = song.artist
                glide.load(currentPlayingSong?.image).into(binding.songImage)
            }
        }
    }
}