package com.juanarton.moviecatalog

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.juanarton.moviecatalog.ui.fragments.SearchScreenFragment
import com.juanarton.moviecatalog.databinding.ActivityMainBinding
import com.juanarton.moviecatalog.ui.fragments.movie.PopularMovieFragment
import nl.joery.animatedbottombar.AnimatedBottomBar

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        fragmentBuilder(PopularMovieFragment())

        val bottomNavBar = binding?.bottomBar
        bottomNavBar?.selectTab(bottomNavBar.tabs[0])

        bottomNavBar?.setOnTabSelectListener(object  : AnimatedBottomBar.OnTabSelectListener{
            override fun onTabSelected(
                lastIndex: Int,
                lastTab: AnimatedBottomBar.Tab?,
                newIndex: Int,
                newTab: AnimatedBottomBar.Tab
            ) {
                when(newIndex){
                    0 -> fragmentBuilder(PopularMovieFragment())
                    1 -> fragmentBuilder(SearchScreenFragment())
                    2 -> moveToFavoriteFragment()
                }
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun moveToFavoriteFragment() {
        val fragment = instantiateFragment(className)
        if (fragment != null) {
            fragmentBuilder(fragment)
        }
    }

    private val className: String
        get() = "com.example.favoritemodule.fragments.FavoriteScreenFragment"

    private fun instantiateFragment(className: String): Fragment? {
        return try {
            Class.forName(className).newInstance() as Fragment
        } catch (e: Exception) {
            Toast.makeText(this, "Module not found", Toast.LENGTH_SHORT).show()
            null
        }
    }

    private fun fragmentBuilder(fragment: Fragment){
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .commit()
    }
}