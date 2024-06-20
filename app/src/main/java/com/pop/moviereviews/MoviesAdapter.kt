package com.pop.moviereviews

import com.pop.moviereviews.model.Movie
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pop.moviereviews.databinding.ViewMovieItemBinding


class MoviesAdapter(
    var movies: List<Movie>,
    private val movieClickedListener: (Movie) -> Unit
) : RecyclerView.Adapter<MoviesAdapter.ViewHolder>() {

    //ViewHolder -> componente dentro del adapter que va contener la vista
    class ViewHolder(private val binding: ViewMovieItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: Movie) {
            binding.titleMovie.text = movie.title
            Glide
                .with(binding.root.context)
                .load("https://image.tmdb.org/t/p/w185${movie.poster_path}")
                .into(binding.movieCover)
        }
    }

    //crea una nueva vista cuando el recycler view se lo pide
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        //val view = LayoutInflater.from(parent.context).inflate(R.layout.view_movie_item, parent, false)
        val binding =
            ViewMovieItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    //actualizar una lista
    override fun getItemCount(): Int {
        return movies.size
    }

    //devuelve el numero de elementos del adpater
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val movie = movies[position]
        holder.bind(movie)
        holder.itemView.setOnClickListener { movieClickedListener(movie) }
    }


}