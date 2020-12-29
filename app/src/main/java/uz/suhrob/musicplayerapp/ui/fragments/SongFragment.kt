package uz.suhrob.musicplayerapp.ui.fragments

import android.view.LayoutInflater
import android.view.ViewGroup
import uz.suhrob.musicplayerapp.databinding.FragmentSongBinding
import uz.suhrob.musicplayerapp.ui.BaseFragment

class SongFragment: BaseFragment<FragmentSongBinding>() {
    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSongBinding = FragmentSongBinding.inflate(inflater, container, false)
}