package com.pop.moviereviews

import android.os.Bundle
import android.text.SpannableStringBuilder
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.bold
import androidx.core.text.buildSpannedString
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.pop.moviereviews.databinding.ActivityDetailBinding
import com.pop.moviereviews.model.Movie

class DetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_MOVIE = "DetailActivity:movie"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val movie = intent.getParcelableExtra<Movie>(EXTRA_MOVIE)
        title = movie?.title
        Glide
            .with(this)
            .load("https://image.tmdb.org/t/p/w780${movie?.backdrop_path}")
            .into(binding.backdrop)
        binding.summary.text = movie?.overview

        bindDetailInfo(binding.detailInfo,movie)

        binding.fab.setOnClickListener {

        }
    }

    private fun bindDetailInfo(detailInfo: TextView, movie: Movie?) {
        detailInfo.text = buildSpannedString {

            appendInfo(R.string.original_language, movie?.original_language)
            appendInfo(R.string.original_title, movie?.original_title)
            appendInfo(R.string.release_date, movie?.release_date)
            appendInfo(R.string.popularity, movie?.popularity.toString())
            appendInfo(R.string.vote_average, movie?.vote_average.toString())

            /*bold { append("Original language: ") }
            appendLine(movie?.original_language)

            bold { append("Original title: ") }
            appendLine(movie?.original_title)

            bold { append("Release date: ") }
            appendLine(movie?.release_date)

            bold { append("Populariry: ") }
            appendLine(movie?.popularity.toString())

            bold { append("Vote Average: ") }
            appendLine(movie?.vote_average.toString())*/
        }
    }

    private fun SpannableStringBuilder.appendInfo(stringRes: Int, value: String?) {
        bold {
            append(getString(stringRes))
            append(": ")
        }
        appendLine(value)
    }
}