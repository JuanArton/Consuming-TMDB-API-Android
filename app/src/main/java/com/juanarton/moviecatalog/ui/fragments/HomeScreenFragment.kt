package com.juanarton.moviecatalog.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.juanarton.core.BuildConfig
import com.juanarton.core.data.source.remote.Resource
import com.juanarton.moviecatalog.databinding.FragmentHomeScreenBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeScreenFragment : Fragment() {

    private var _binding: FragmentHomeScreenBinding? = null
    private val binding get() = _binding

    private val homeScreenViewModel: HomeScreenViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeScreenBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homeScreenViewModel.getPopularMovie(BuildConfig.API_KEY, "en", 1)
            .observe(viewLifecycleOwner) {
                it.let {
                    when (it) {
                        is Resource.Success ->
                            if (!it.data.isNullOrEmpty()){
                                Log.d("daftar movie", it.data!![0].id)
                            }

                        is Resource.Loading -> Log.d("daftar movie", "loading")
                        is Resource.Error -> Log.d("daftar movie", "Error")
                    }
                }
            }
    }

}
