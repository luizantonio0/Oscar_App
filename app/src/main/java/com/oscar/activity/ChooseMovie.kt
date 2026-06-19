package com.oscar.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.oscar.R
import com.oscar.config.ActivityUtil
import com.oscar.config.MovieAdapter
import com.oscar.config.OnGenericAdapterClickListener
import com.oscar.data.model.Movie
import com.oscar.databinding.ActivityChooseMovieBinding
import com.oscar.service.ImageDownloadService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChooseMovie : AppCompatActivity(), OnGenericAdapterClickListener<Movie> {
    private lateinit var binding: ActivityChooseMovieBinding
    private lateinit var recyclerView: RecyclerView
    private var choosedMovie: Movie? = null

    override fun onAdapterClick(t: Movie) {
        binding.tvSelectedName.text = t.nome

        binding.ivSelectionThumbnail

        binding.ivCheck.setImageResource(R.drawable.check_circle_24dp_fill)

        lifecycleScope.launch {
            // Tenta baixar a imagem via bitmap, em caso de erro carrega uma imagem padrão
            val bitmap = withContext(Dispatchers.IO) {
                ImageDownloadService.downloadImageBitmap(t.foto)
            }

            if (bitmap != null) {
                binding.ivSelectionThumbnail.setImageBitmap(bitmap)
            } else {
                binding.ivSelectionThumbnail.setImageResource(
                    R.drawable.oscar_academy_award
                )
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUtil.initialConfig(this, ActivityChooseMovieBinding::inflate)


        recyclerView = binding.picturesRecyclerView
        loadData()
    }

    fun loadData(){
        val list = listOf(
            Movie(
                0L,
                "Titanic",
                "Drama",
                "https://m.media-amazon.com/images/I/91pZ7Rcp4iL._AC_UF894,1000_QL80_.jpg"),
            Movie(
                1L,
                "Neymar",
                "Documentario",
                "https://m.media-amazon.com/images/M/MV5BNjFjMjY2NjMtMDQ1Yy00OTEwLWE1NjQtM2FlMjJiYzM3OTljXkEyXkFqcGc@._V1_.jpg"))

        recyclerView.adapter = MovieAdapter(list, this, this)
        recyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)

    }

    fun comeback(view: View){
        finish()
    }
    fun sendVote(){

    }

}