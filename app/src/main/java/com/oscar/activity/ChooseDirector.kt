package com.oscar.activity

import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.oscar.config.ActivityUtil
import com.oscar.data.model.Director
import com.oscar.data.model.Movie
import com.oscar.data.model.User
import com.oscar.data.model.Votacao
import com.oscar.databinding.ActivityChooseDirectorBinding
import com.oscar.repository.DatabaseHelper
import com.oscar.service.ApiRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class ChooseDirector : AppCompatActivity() {
    private lateinit var binding: ActivityChooseDirectorBinding
    private val api = ApiRequest(this)
    private var choosedDirector: Director? = null
    private var user: User? = null
    private var votacao: Votacao? = null
    private var databaseHelper = DatabaseHelper()
    private var isFinished = true



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUtil.initialConfig(this, ActivityChooseDirectorBinding::inflate)

        user = DatabaseHelper().findUser()

        lifecycleScope.launch(Dispatchers.Main) {
            showLoading()

            loadVotesInOrder()
            loadData()

            hideLoading()
        }

        binding.cdRadioGroup.setOnCheckedChangeListener { group, checkedId ->
            if (votacao?.isFinished == true) {
                Toast.makeText(
                    this@ChooseDirector,
                    "Votação já enviada. Não é possível alterar o voto.",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnCheckedChangeListener
            }
            val name = group.findViewById<RadioButton>(checkedId).text
            binding.cdSelectedName.text = name
            val newDirector = Director(checkedId.toLong(), name.toString())
            choosedDirector = newDirector
        }

        binding.cdBackVoting.setOnClickListener {
            finish()
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

    //Tenta Carrgar os votos pela API, se der erro tenta carrgar local
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

            if (votacao?.diretor != null) {
                binding.cdSelectedName.text = votacao?.diretor?.nome
                choosedDirector = votacao?.diretor
            }

            binding.btnConfirm.isEnabled = !isFinished
            binding.cdRadioGroup.isEnabled = !isFinished

        } catch (e: HttpException) {
            val votacaoLocal = withContext(Dispatchers.IO) {
                databaseHelper.findVotacao()
            }

            votacao = votacaoLocal
            isFinished = votacao?.isFinished == true

            if (votacao?.diretor != null) {
                binding.cdSelectedName.text = votacao?.diretor?.nome
                choosedDirector = votacao?.diretor
            }

            binding.btnConfirm.isEnabled = !isFinished
            binding.cdRadioGroup.isEnabled = !isFinished
        }
    }


    private suspend fun loadData() {
        try {
            val list = withContext(Dispatchers.IO) {
                api.getDiretores(user?.accessToken ?: "")
            }

            binding.cdRadioGroup.removeAllViews()

            list.forEachIndexed { _, d ->
                val radioB = RadioButton(this@ChooseDirector).apply {
                    text = d.nome
                    id = d.id.toInt()
                }
                binding.cdRadioGroup.addView(radioB)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun hideLoading() {
        binding.bdProgressbar.visibility = View.GONE
    }

    fun showLoading() {
        binding.bdProgressbar.visibility = View.VISIBLE
    }

    fun sendVoteDirector(view: View){
        lifecycleScope.launch(Dispatchers.Main) {
            if (choosedDirector == null) {
                Toast.makeText(this@ChooseDirector, "Selecione um Diretor Antes", Toast.LENGTH_SHORT).show()
                return@launch
            }
            withContext(Dispatchers.IO) {
                databaseHelper.updateVotacao(
                    Votacao(
                        1L,
                        null,
                        choosedDirector
                    ).apply {
                        isFinished = votacao?.isFinished == true
                    }
                )
            }
            this@ChooseDirector.finish()
        }
    }
    fun comeback(view: View){
        finish()
    }
}