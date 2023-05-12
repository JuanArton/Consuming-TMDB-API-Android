package com.juanarton.moviecatalog.ui.fragments.search

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagingData
import androidx.recyclerview.widget.GridLayoutManager
import com.juanarton.core.adapter.SearchPagingAdapter
import com.juanarton.core.data.domain.model.Movie
import com.juanarton.core.data.utils.DataMapper
import com.juanarton.moviecatalog.databinding.FragmentSearchScreenBinding
import com.juanarton.moviecatalog.ui.activity.detail.DetailMovieActivity
import kotlinx.coroutines.flow.distinctUntilChangedBy
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
                        searchViewModel.multiSearch().observe(viewLifecycleOwner){
                            showRecyclerList(it)
                        }
                    }
                }

                runnable?.let {
                    handler.postDelayed(it, 500)
                }
                return false
            }
        })
    }

    private fun showRecyclerList(movieList: PagingData<Movie>){
        val listener: (Movie) -> Unit = {
            val mappedToMovie = DataMapper.mapSearchToMovieDomain(it)
            val intent = Intent(context, DetailMovieActivity::class.java)
            intent.putExtra("movieData", mappedToMovie)
            intent.putExtra("mode", it.mediaType)
            startActivity(intent)
        }

        binding?.searchRecyclerContainer?.layoutManager = GridLayoutManager(activity, 3)
        val rvAdapter = SearchPagingAdapter(listener)
        binding?.searchRecyclerContainer?.adapter = rvAdapter
        rvAdapter.submitData(lifecycle, movieList)

        lifecycleScope.launch {
            rvAdapter.loadStateFlow.distinctUntilChangedBy { it.refresh }.collect {
                rvAdapter.snapshot().toList().let {
                    Log.d("pagingdata", it.toString())
                }
            }
        }
    }
}