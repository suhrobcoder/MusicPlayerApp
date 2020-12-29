package uz.suhrob.musicplayerapp.ui.fragments

import android.view.LayoutInflater
import android.view.ViewGroup
import uz.suhrob.musicplayerapp.databinding.FragmentHomeBinding
import uz.suhrob.musicplayerapp.ui.BaseFragment

class HomeFragment: BaseFragment<FragmentHomeBinding>() {
    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentHomeBinding = FragmentHomeBinding.inflate(inflater, container, false)
}