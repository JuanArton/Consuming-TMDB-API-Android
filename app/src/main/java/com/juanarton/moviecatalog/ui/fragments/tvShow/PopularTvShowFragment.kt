package com.juanarton.moviecatalog.ui.fragments.tvShow

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.juanarton.core.BuildConfig
import com.juanarton.core.adapter.MoviePagingAdapter
import com.juanarton.core.data.domain.model.Movie
import com.juanarton.moviecatalog.R
import com.juanarton.moviecatalog.databinding.FragmentPopularTvShowBinding
import com.juanarton.moviecatalog.ui.activity.detail.DetailMovieActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.imaginativeworld.whynotimagecarousel.ImageCarousel
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem
import org.koin.androidx.viewmodel.ext.android.viewModel

class PopularTvShowFragment : Fragment() {

    private var _binding: FragmentPopularTvShowBinding? = null
    private val binding get() = _binding

    private var _imgCarousel: ImageCarousel? = null
    private val imgCarousel get() = _imgCarousel

    private val popularTvShowViewModel: PopularTvShowViewModel by viewModel()

    private var firstRun: Boolean = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPopularTvShowBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _imgCarousel = binding?.imgcarousel
        imgCarousel?.registerLifecycle(lifecycle)

        val listener: (Movie) -> Unit = {
            val intent = Intent(context, DetailMovieActivity::class.java)
            intent.putExtra("movieData", it)
            startActivity(intent)
        }

        binding?.tvShowRecyclerContainer?.layoutManager = GridLayoutManager(activity, 3)
        val rvAdapter = MoviePagingAdapter(listener)
        binding?.tvShowRecyclerContainer?.adapter = rvAdapter

        binding?.refreshAnimation?.playAnimation()

        popularTvShowViewModel.getPopularTvShow().observe(viewLifecycleOwner){
            rvAdapter.submitData(lifecycle, it)
        }

        binding?.tvMotionLayout?.setTransitionListener(object : MotionLayout.TransitionListener{
            override fun onTransitionStarted( motionLayout: MotionLayout?, startId: Int, endId: Int
            ) {
            }

            override fun onTransitionChange( motionLayout: MotionLayout?,  startId: Int,
                endId: Int, progress: Float
            ) {
                imgCarousel?.stop()
            }

            override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {
                if(binding?.tvMotionLayout?.progress?.toInt() == 1){
                    imgCarousel?.stop()
                }else{
                    imgCarousel?.start()
                }
            }

            override fun onTransitionTrigger( motionLayout: MotionLayout?, triggerId: Int,
                positive: Boolean, progress: Float
            ) {
            }
        })

        lifecycleScope.launch {
            rvAdapter.loadStateFlow.collectLatest {loadState ->
                rvAdapter.snapshot().toList().let {

                    if (it.isNotEmpty() && firstRun){
                        setCarousel(it)
                        firstRun = false
                    }

                    binding?.apply {
                        if (loadState.refresh is LoadState.Loading ||
                            loadState.append is LoadState.Loading)
                        {
                            nodataText.text = resources.getString(R.string.loading)
                            refreshAnimation.setAnimation("moody-giraffe.json")
                            nodataText.isVisible = true
                            refreshAnimation.isVisible = true
                        }
                        else if (loadState.refresh !is LoadState.Loading ||
                            loadState.append !is LoadState.Loading)
                        {
                            nodataText.isVisible = false
                            refreshAnimation.isVisible = false
                        }

                        refreshAnimation.playAnimation()
                    }
                }
            }
        }
    }

    private fun setCarousel(data: List<Movie?>){
        CoroutineScope(Dispatchers.IO).launch{
            val carouselList = mutableListOf<CarouselItem>()
            for(i in 0..5){
                val imageUrl = buildString{
                    append(BuildConfig.BASE_IMAGE_URL)
                    append(data[i]?.backdropPath)
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
}