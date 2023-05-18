package com.juanarton.favorite.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.juanarton.favorite.adapter.FavoriteRecylerAdapter
import com.juanarton.core.data.domain.model.Movie
import com.juanarton.favorite.databinding.FragmentFavoriteScreenBinding
import com.juanarton.favorite.di.favoriteMovieModule
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.context.loadKoinModules

class FavoriteScreenFragment : Fragment() {

    private var _binding: FragmentFavoriteScreenBinding? = null
    private val binding get() = _binding
    private val favoriteViewModel: FavoriteViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavoriteScreenBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadKoinModules(favoriteMovieModule)

        val listener: (Movie) -> Unit = {
            val intent = Intent(context, Class.forName("com.juanarton.moviecatalog.ui.activity.detail.DetailMovieActivity"))
            intent.putExtra("movieData", it)
            startActivity(intent)
        }

        binding?.favoriteRecyclerContainer?.layoutManager = LinearLayoutManager(context)
        val rvAdapter = FavoriteRecylerAdapter(listener)
        binding?.favoriteRecyclerContainer?.adapter = rvAdapter

        favoriteViewModel.getListFavorite().observe(viewLifecycleOwner) {
            Log.d("test", it.toString())
            rvAdapter.submit(it)
        }
    }
}