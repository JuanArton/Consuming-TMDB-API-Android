package com.juanarton.moviecatalog.ui.fragments.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.juanarton.core.BuildConfig
import com.juanarton.core.adapter.RecyclerAdapter
import com.juanarton.core.data.domain.model.Movie
import com.juanarton.core.data.source.remote.Resource
import com.juanarton.moviecatalog.databinding.FragmentPopularMovieBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.imaginativeworld.whynotimagecarousel.ImageCarousel
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem
import org.koin.androidx.viewmodel.ext.android.viewModel

class PopularMovieFragment : Fragment() {

    private var _binding: FragmentPopularMovieBinding? = null
    private val binding get() = _binding

    private var _imgCarousel: ImageCarousel? = null
    private val imgCarousel get() = _imgCarousel

    private val homeScreenViewModel: HomeScreenViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPopularMovieBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding?.nodataText?.visibility = View.INVISIBLE
        binding?.loadanimate?.visibility = View.INVISIBLE

        homeScreenViewModel.getPopularMovie(BuildConfig.API_KEY, "en", 1)
            .observe(viewLifecycleOwner) {
                it.let {
                    when (it) {
                        is Resource.Success ->
                            if (!it.data.isNullOrEmpty()){
                                Log.d("daftar movie", it.data!![0].id)
                                it.data?.let { movieList ->
                                    setUI(movieList)
                                }
                            }

                        is Resource.Loading -> Log.d("daftar movie", "loading")
                        is Resource.Error -> Log.d("daftar movie", "Error")
                    }
                }
            }
    }

    private fun setUI(data: List<Movie>){
        if(data.isEmpty()){
            binding?.nodataText?.visibility = View.VISIBLE
            binding?.loadanimate?.visibility = View.VISIBLE
        }else{
            setCarousel(data)
        }
    }

    private fun setCarousel(data: List<Movie>){
        _imgCarousel = binding?.imgcarousel
        imgCarousel?.registerLifecycle(lifecycle)
        CoroutineScope(Dispatchers.IO).launch{
            val carouselList = mutableListOf<CarouselItem>()
            for(i in 0..5){
                val imageUrl = buildString{
                    append(BuildConfig.BASE_IMAGE_URL)
                    append(data[i].backdrop_path)
                }
                carouselList.add(
                    CarouselItem(
                        imageUrl,
                        data[i].title
                    )
                )
            }
            activity?.runOnUiThread {
                imgCarousel?.setData(carouselList.shuffled())
                showRecyclerList(data)
            }
        }
    }

    private fun showRecyclerList(movieList: List<Movie>){
        val listener: (Movie) -> Unit = {

        }

        binding?.movieRecyclerContainer?.layoutManager = GridLayoutManager(activity, 3)
        val rvAdapter = RecyclerAdapter(listener, movieList)
        binding?.movieRecyclerContainer?.adapter = rvAdapter
        binding?.nodataText?.visibility = View.INVISIBLE
        binding?.loadanimate?.visibility = View.INVISIBLE
    }
}
