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
import com.oscar.data.model.Director
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
import retrofit2.HttpException

class ChooseMovie : AppCompatActivity(), OnGenericAdapterClickListener<Movie> {
    private lateinit var binding: ActivityChooseMovieBinding
    private lateinit var recyclerView: RecyclerView
    private val api = ApiRequest(this)
    private var choosedMovie: Movie? = null
    private var user: User? = null
    private var votacao: Votacao? = null
    private var databaseHelper = DatabaseHelper()
    private var isFinished = true

    override fun onAdapterClick(t: Movie) {
        lifecycleScope.launch(Dispatchers.Main) {

            if (isFinished) {
                binding.btnConfirm.isEnabled = false
                Toast.makeText(
                    this@ChooseMovie,
                    "Votação já enviada. Não é possível alterar o voto.",
                    Toast.LENGTH_SHORT
                ).show()
                return@launch
            }

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
        lifecycleScope.launch(Dispatchers.Main) {
            showLoading()

            loadVotesInOrder()
            loadData()

            hideLoading()
        }
    }

    override fun onResume(){
        super.onResume()
        lifecycleScope.launch(Dispatchers.Main) {
            showLoading()

            loadVotesInOrder()
            loadData()

            hideLoading()
        }
    }

    private suspend fun loadVotesInOrder() {
        try {
            val votacaoConfirmada = withContext(Dispatchers.IO) {
                val votacaoDto = api.getVotosConfirmados(user?.accessToken ?: "")

                // Se encontrar Salva no banco de dados
                val votacaoServidor = Votacao(
                    1L,
                    Movie(
                        votacaoDto.filme.id,
                        votacaoDto.filme.nome,
                        votacaoDto.filme.genero,
                        votacaoDto.filme.foto
                    ),
                    Director(
                        votacaoDto.diretor.id,
                        votacaoDto.diretor.nome
                    )
                ).apply {
                    isFinished = true
                }

                databaseHelper.updateVotacao(votacaoServidor)

                databaseHelper.findVotacao()
            }

            votacao = votacaoConfirmada
            isFinished = votacao?.isFinished == true

            if (votacao?.filme != null) {
                binding.tvSelectedName.text = votacao?.filme?.nome
                choosedMovie = votacao?.filme
            }

            binding.btnConfirm.isEnabled = !isFinished
            binding.picturesRecyclerView.isEnabled = !isFinished

        } catch (e: HttpException) {
            val votacaoLocal = withContext(Dispatchers.IO) {
                databaseHelper.findVotacao()
            }

            votacao = votacaoLocal
            isFinished = votacao?.isFinished == true

            if (votacao?.filme != null) {
                binding.tvSelectedName.text = votacao?.filme?.nome
                choosedMovie = votacao?.filme
            }

            binding.btnConfirm.isEnabled = !isFinished
            binding.picturesRecyclerView.isEnabled = !isFinished
        }
    }


    suspend fun loadData(){
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
                    ).apply {
                        isFinished = votacao?.isFinished == true
                    }
                )
            }
            this@ChooseMovie.finish()
        }
    }

}