package com.rival.belajardoadandzikirapp

import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.viewpager2.widget.ViewPager2
import com.rival.belajardoadandzikirapp.adapter.ArtikelAdapter
import com.rival.belajardoadandzikirapp.databinding.ActivityMainBinding
import com.rival.belajardoadandzikirapp.model.ArtikelModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var slideIndicator: Array<ImageView?>
    private val listArtikel: ArrayList<ArtikelModel> = arrayListOf()

    private val slidingCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            updateSlideIndicators(position)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        installSplashScreen()
        setContentView(binding.root)

        setDataArtikel()
        setView()
        setUpViewPagerArtikel()
    }

    private fun updateSlideIndicators(position: Int) {
        // Update the slide indicators based on the current position
        for (i in slideIndicator.indices) {
            slideIndicator[i]?.setImageDrawable(
                if (i == position) {
                    ContextCompat.getDrawable(applicationContext, R.drawable.dot_active)
                } else {
                    ContextCompat.getDrawable(applicationContext, R.drawable.dot_inactive)
                }
            )
        }
    }

    private fun setView() {
        // setup view artikel
        binding.apply {
            // Set the adapter for ViewPager2
            vpArtikel.adapter = ArtikelAdapter(listArtikel)

            // Register the onPageChangeCallback
            vpArtikel.registerOnPageChangeCallback(slidingCallback)


        }
    }

    private fun setDataArtikel() {
        // Get data for articles from resources
        val titleArtikel = resources.getStringArray(R.array.arr_title_artikel)
        val descArtikel = resources.getStringArray(R.array.arr_desc_artikel)
        val imgArtikel = resources.obtainTypedArray(R.array.arr_img_artikel)

        // Populate the listArtikel ArrayList with article data
        for (data in titleArtikel.indices) {
            val artikel = ArtikelModel(
                imgArtikel.getResourceId(data, 0),
                titleArtikel[data],
                descArtikel[data]
            )
            listArtikel.add(artikel)
        }
        // Recycle the TypedArray to release resources
        imgArtikel.recycle()
    }

    private fun setUpViewPagerArtikel() {
        // Get the LinearLayout for slide indicators
        val llSliderDots = binding.llSliderDots

        // Get the size of the listArtikel
        val size = listArtikel.size

        // Initialize the slideIndicator array with null values
        slideIndicator = Array(size) { null }

        // Create and configure ImageView elements for slide indicators
        for (i in 0 until size) {
            slideIndicator[i] = ImageView(this).apply {
                setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.dot_inactive
                    )
                )
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    setMargins(9, 0, 8, 0)
                    gravity = android.view.Gravity.CENTER_VERTICAL
                }
            }
            // Add ImageView to the LinearLayout
            llSliderDots.addView(slideIndicator[i])
        }
        // Set the initial slide indicator to active
        updateSlideIndicators(0)
    }

    override fun onDestroy() {
        super.onDestroy()
        // Unregister the onPageChangeCallback to prevent memory leaks
        binding.vpArtikel.unregisterOnPageChangeCallback(slidingCallback)
    }
}