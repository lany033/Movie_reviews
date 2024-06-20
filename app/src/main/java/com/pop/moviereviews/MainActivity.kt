package com.pop.moviereviews

import android.annotation.SuppressLint
import android.content.Intent
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.pop.moviereviews.databinding.ActivityMainBinding
import com.pop.moviereviews.model.Movie
import com.pop.moviereviews.model.MovieDbClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlin.concurrent.thread
import kotlin.coroutines.resume

class MainActivity : AppCompatActivity() {

    companion object {
        private const val DEFAULT_REGION = "US"
    }

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()){ isGranted ->
        doRequestPopularMovies(isGranted)
        /*val message = when {
            isGranted -> "Permission Granted!"
            shouldShowRequestPermissionRationale(android.Manifest.permission.ACCESS_COARSE_LOCATION) -> "Should show Rationale"
            else -> "Permission Rejected"
        }
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()*/
    }

    private val moviesAdapter = MoviesAdapter(emptyList()) { navigateTo(it) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding =
            ActivityMainBinding.inflate(layoutInflater) //POR CADA LAYAOUT SE TIENE UN OBJETO DEFINIDO
        setContentView(binding.root) // root -> la raiz del componente que se ha inflado

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        /*
       binding.showButton.setOnClickListener {
           val message = binding.message.text
           Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
           //context > informaciones que tenemos en el punto actual
       }
         */
        binding.recyclerView.adapter = moviesAdapter

        requestPermissionLauncher.launch(android.Manifest.permission.ACCESS_COARSE_LOCATION)
    }

    private fun doRequestPopularMovies(isLocationGranted: Boolean) {
        lifecycleScope.launch {
            val apikey= getString(R.string.api_key)
            val region = getRegion(isLocationGranted)
            val popularMovies = MovieDbClient.service.listPopularMovies(apikey, region)
            moviesAdapter.movies = popularMovies.results
            moviesAdapter.notifyDataSetChanged()
        }
    }

    @SuppressLint("MissingPermission")
    private suspend fun getRegion(isLocationGranted: Boolean) : String = suspendCancellableCoroutine{continuation ->
        if (isLocationGranted){
            fusedLocationClient.lastLocation.addOnCompleteListener {
                continuation.resume(getRegionFromLocation(it.result))
            }
        } else {
            continuation.resume(DEFAULT_REGION)
        }
    }

    private fun getRegionFromLocation(location: Location?): String {
        if (location == null){
            return DEFAULT_REGION
        }

        val geocoder = Geocoder(this)
        val result = geocoder.getFromLocation(location.latitude, location.longitude, 1)
        Log.d("Location", result?.firstOrNull()?.countryCode ?: DEFAULT_REGION)
        return result?.firstOrNull()?.countryCode ?: "US"
    }

    private fun navigateTo(movie: Movie) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra(DetailActivity.EXTRA_MOVIE, movie)

        startActivity(intent)
    }

}