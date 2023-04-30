package com.juanarton.moviecatalog.ui.fragments.movie

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagingData
import androidx.recyclerview.widget.GridLayoutManager
import com.juanarton.core.BuildConfig
import com.juanarton.core.adapter.MoviePagingAdapter
import com.juanarton.core.data.domain.model.Movie
import com.juanarton.moviecatalog.databinding.FragmentPopularMovieBinding
import com.juanarton.moviecatalog.ui.activity.detail.DetailMovieActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.launch
import org.imaginativeworld.whynotimagecarousel.ImageCarousel
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem
import org.koin.androidx.viewmodel.ext.android.viewModel

class PopularMovieFragment : Fragment() {

    private var _binding: FragmentPopularMovieBinding? = null
    private val binding get() = _binding

    private var _imgCarousel: ImageCarousel? = null
    private val imgCarousel get() = _imgCarousel

    private val popularMovieViewModel: PopularMovieViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPopularMovieBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        popularMovieViewModel.getPopularMovie().observe(viewLifecycleOwner){
            showRecyclerList(it)
        }

        binding?.movieMotionLayout?.setTransitionListener(object : MotionLayout.TransitionListener{
            override fun onTransitionStarted(
                motionLayout: MotionLayout?,
                startId: Int,
                endId: Int
            ) {
            }

            override fun onTransitionChange(
                motionLayout: MotionLayout?,
                startId: Int,
                endId: Int,
                progress: Float
            ) {
                imgCarousel?.stop()
            }

            override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {
                if(binding?.movieMotionLayout?.progress?.toInt() == 1){
                    imgCarousel?.stop()
                }else{
                    imgCarousel?.start()
                }
                Log.d("motionMonitor", binding?.movieMotionLayout?.progress?.toInt().toString())
            }

            override fun onTransitionTrigger(
                motionLayout: MotionLayout?,
                triggerId: Int,
                positive: Boolean,
                progress: Float
            ) {
            }

        })

    }

    private fun setUI(data: List<Movie?>){
        if(data.isEmpty()){
            binding?.nodataText?.visibility = View.VISIBLE
            binding?.loadanimate?.visibility = View.VISIBLE
        }else{
            binding?.nodataText?.visibility = View.INVISIBLE
            binding?.loadanimate?.visibility = View.INVISIBLE
            setCarousel(data)
        }
    }

    private fun setCarousel(data: List<Movie?>){
        _imgCarousel = binding?.imgcarousel
        imgCarousel?.registerLifecycle(lifecycle)
        CoroutineScope(Dispatchers.IO).launch{
            val carouselList = mutableListOf<CarouselItem>()
            for(i in 0..5){
                val imageUrl = buildString{
                    append(BuildConfig.BASE_IMAGE_URL)
                    append(data[i]?.backdrop_path)
                }
                carouselList.add(
                    CarouselItem(
                        imageUrl,
                        data[i]?.title
                    )
                )
            }
            activity?.runOnUiThread {
                imgCarousel?.setData(carouselList.shuffled())
            }
        }
    }

    private fun showRecyclerList(movieList: PagingData<Movie>){
        val listener: (Movie) -> Unit = {
            val intent = Intent(context, DetailMovieActivity::class.java)
            intent.putExtra("movieData", it)
            startActivity(intent)
        }

        binding?.movieRecyclerContainer?.layoutManager = GridLayoutManager(activity, 3)
        val rvAdapter = MoviePagingAdapter(listener)
        binding?.movieRecyclerContainer?.adapter = rvAdapter
        rvAdapter.submitData(lifecycle, movieList)

        lifecycleScope.launch {
            rvAdapter.loadStateFlow.distinctUntilChangedBy { it.refresh }.collect {
                rvAdapter.snapshot().toList().let {
                    Log.d("pagingdata", it.toString())
                    setUI(it)
                }
            }
        }
    }
}
