package com.oscar.config

import com.oscar.R
import com.oscar.data.model.Movie


import android.content.Context
import android.widget.ImageView
import android.widget.TextView
import com.oscar.service.ImageDownloadService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SetMovieViewData: SetViewData <Movie>{
    override fun setViewData(
        title: TextView,
        genre: TextView,
        image: ImageView,
        t: Movie
    ) {
        title.text = t.nome
        genre.text = t.genero

        CoroutineScope(Dispatchers.Main).launch {
            //Tenta baixar a imagem via bitmap, em caso de erro carrega uma imagem padrão
            val bitmap = withContext(Dispatchers.IO){
                ImageDownloadService.downloadImageBitmap(t.foto)
            }
            if (bitmap != null) {
                image.setImageBitmap(bitmap)
            } else {
                image.setImageResource(
                    R.drawable.oscar_academy_award)
            }
        }


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