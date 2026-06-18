package com.oscar.config

import com.oscar.R
import com.oscar.data.model.Movie


import android.content.Context
import android.widget.ImageView
import android.widget.TextView

class SetMovieViewData: SetViewData <Movie>{
    override fun setViewData(
        title: TextView,
        genre: TextView,
        image: ImageView,
        t: Movie
    ) {
        title.text = t.nome
        genre.text = t.genero
        image.setImageResource(
            try {
                //TODO: Lógica para implementar imagem
                0
            } catch (e: Exception) {
                R.drawable.oscar_academy_award
            }
        )
    }
}

class MovieAdapter(workoutList: List<Movie>,
                     context: Context,
                     clickListener: OnGenericAdapterClickListener<Movie>
) : GenericAdapter<Movie>(
    workoutList,
    context,
    clickListener,
    setViewData = SetMovieViewData()
)