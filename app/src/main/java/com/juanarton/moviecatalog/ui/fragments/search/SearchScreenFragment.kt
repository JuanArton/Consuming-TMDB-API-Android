package com.juanarton.moviecatalog.ui.fragments.search

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.juanarton.core.adapter.MoviePagingAdapter
import com.juanarton.core.data.domain.model.Movie
import com.juanarton.core.data.utils.DataMapper
import com.juanarton.moviecatalog.R
import com.juanarton.moviecatalog.databinding.FragmentSearchScreenBinding
import com.juanarton.moviecatalog.ui.activity.detail.DetailMovieActivity
import com.juanarton.moviecatalog.utils.DataHolder
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class SearchScreenFragment : Fragment() {

    private var _binding: FragmentSearchScreenBinding? = null
    private val binding get() = _binding
    private val searchViewModel: SearchViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchScreenBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val handler = Handler(Looper.getMainLooper())
        var runnable: Runnable? = null

        val listener: (Movie) -> Unit = {
            val mappedToMovie = DataMapper.mapSearchToMovieDomain(it)
            DataHolder.movie = mappedToMovie
            val intent = Intent(context, DetailMovieActivity::class.java)
            startActivity(intent)
        }

        binding?.rvSearch?.layoutManager = GridLayoutManager(activity, 3)
        val rvAdapter = MoviePagingAdapter(listener)
        binding?.rvSearch?.adapter = rvAdapter

        binding?.lottieSearchLoading?.playAnimation()

        binding?.searchBar?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                runnable?.let {
                    handler.removeCallbacks(it)
                }

                runnable = Runnable {
                    newText?.let {searchString ->
                        searchViewModel.setSearchString(searchString)
                    }
                }

                runnable?.let {
                    handler.postDelayed(it, 500)
                }
                return false
            }
        })

        searchViewModel.movieSearchList.observe(viewLifecycleOwner){
            rvAdapter.submitData(lifecycle, it)
        }

        lifecycleScope.launch {
            rvAdapter.loadStateFlow.collectLatest {loadState ->
                rvAdapter.snapshot().toList().let {

                    binding?.apply {
                        if (loadState.refresh is LoadState.Loading ||
                            loadState.append is LoadState.Loading)
                        {
                            tvSearcNoData.text = resources.getString(R.string.loading)
                            lottieSearchLoading.setAnimation("moody-giraffe.json")
                            tvSearcNoData.isVisible = true
                            lottieSearchLoading.isVisible = true
                        }
                        else if (loadState.refresh !is LoadState.Loading ||
                            loadState.append !is LoadState.Loading)
                        {
                            tvSearcNoData.isVisible = false
                            lottieSearchLoading.isVisible = false
                        }

                        lottieSearchLoading.playAnimation()
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        _binding = null
    }
}