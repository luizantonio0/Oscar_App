package com.oscar.activity

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.oscar.R
import com.oscar.config.ActivityUtil
import com.oscar.config.MovieAdapter
import com.oscar.config.OnGenericAdapterClickListener
import com.oscar.data.model.Movie
import com.oscar.data.model.User
import com.oscar.data.model.Votacao
import com.oscar.databinding.ActivityChooseMovieBinding
import com.oscar.repository.DatabaseHelper
import com.oscar.service.ApiRequest
import com.oscar.service.ImageDownloadService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChooseMovie : AppCompatActivity(), OnGenericAdapterClickListener<Movie> {
    private lateinit var binding: ActivityChooseMovieBinding
    private lateinit var recyclerView: RecyclerView
    private val api = ApiRequest(this)
    private var choosedMovie: Movie? = null
    private var user: User? = null
    private var databaseHelper = DatabaseHelper()

    override fun onAdapterClick(t: Movie) {
        lifecycleScope.launch(Dispatchers.Main) {
            binding.tvSelectedName.text = t.nome

            binding.ivSelectionThumbnail

            binding.ivCheck.setImageResource(R.drawable.check_circle_24dp_fill)

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

            choosedMovie = t
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUtil.initialConfig(this, ActivityChooseMovieBinding::inflate)


        recyclerView = binding.picturesRecyclerView
        user = DatabaseHelper().findUser()
        showLoading()
        loadData()
        loadVotacao()
    }

    fun loadVotacao() {
        lifecycleScope.launch (Dispatchers.Main){
            withContext(Dispatchers.IO){
                val votacao = databaseHelper.findVotacao()
                if (votacao?.filme != null) {
                    choosedMovie = votacao.filme
                    onAdapterClick(choosedMovie!!)
                }
            }
        }
    }

    fun loadData(){
        lifecycleScope.launch {
            try {
                val list = withContext(Dispatchers.IO) {
                    api.getFilmes(user?.accessToken?: "")
                }
                recyclerView.adapter = MovieAdapter(list, this@ChooseMovie, this@ChooseMovie)
                recyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this@ChooseMovie)
              }
            catch (e: Exception) {
                e.printStackTrace()
            }
            finally {
                hideLoading()
            }

        }
    }

    fun hideLoading() {
        binding.bmProgressbar.visibility = View.GONE
    }

    fun showLoading() {
        binding.bmProgressbar.visibility = View.VISIBLE
    }

    fun comeback(view: View){
        finish()
    }
    fun sendVoteMovie(view: View){
        lifecycleScope.launch(Dispatchers.Main) {
            if (choosedMovie == null) {
                Toast.makeText(this@ChooseMovie, "Selecione um Filme Antes", Toast.LENGTH_SHORT).show()
                return@launch
            }
            withContext(Dispatchers.IO) {
                databaseHelper.updateVotacao(
                    Votacao(
                        1L,
                        choosedMovie,
                        null
                    )
                )
            }
            this@ChooseMovie.finish()
        }
    }

}