package com.oscar.activity.ui.confirmation

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.oscar.R
import com.oscar.activity.ChooseDirector
import com.oscar.activity.ChooseMovie
import com.oscar.data.dto.response.VotacaoResponseDTO
import com.oscar.data.model.User
import com.oscar.data.model.Votacao
import com.oscar.databinding.FragmentConfirmationBinding
import com.oscar.repository.DatabaseHelper
import com.oscar.service.ApiRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ConfirmationVotingFragment : Fragment() {

    private var _binding: FragmentConfirmationBinding? = null
    private var databaseHelper = DatabaseHelper()
    private val binding get() = _binding!!
    private var votacao: Votacao? = null
    private lateinit var api: ApiRequest
    private var user: User? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentConfirmationBinding.inflate(inflater, container, false)
        val root: View = binding.root
        api = ApiRequest(requireActivity())

        loadUser()
        loadData()

        binding.cardViewMovie.setOnClickListener {
            startActivity(Intent(activity, ChooseMovie::class.java))
        }

        binding.cardViewDirector.setOnClickListener {
            startActivity(Intent(activity, ChooseDirector::class.java))
        }

        return root
    }
    override fun onResume() {
        super.onResume()
        loadUser()
        loadData()
    }

    fun loadUser(){
        lifecycleScope.launch {
            user = withContext(Dispatchers.IO) {
                databaseHelper.findUser()
            }

            if (user == null) {
                Toast.makeText(activity, "Usuário não encontrado. Faça login novamente.", Toast.LENGTH_SHORT).show()
                return@launch
            }

            if (user?.isFinished!!){
                binding.btnSubmitAll.setOnClickListener  {  }
                binding.btnSubmitAll.isEnabled = false

            } else {
                binding.btnSubmitAll.setOnClickListener  { submit() }
                binding.btnSubmitAll.isEnabled = true
            }
        }
    }

    fun loadData(){
        lifecycleScope.launch (Dispatchers.Main){
            withContext(Dispatchers.IO){
                votacao = databaseHelper.findVotacao()
            }
            if (votacao == null) return@launch

            if (votacao?.filme != null) {
                binding.checkMovie.setImageResource(R.drawable.check_circle_24dp_fill)
                binding.tvMovieName.text = votacao?.filme?.nome
            }

            if (votacao?.diretor != null) {
                binding.checkDirector.setImageResource(R.drawable.check_circle_24dp_fill)
                binding.tvDirectorName.text = votacao?.diretor?.nome
            }
        }
    }

    fun validateVotes(): String{
        if (votacao == null) return "Filme e Diretor"
        if (votacao?.filme == null) return "Filme"
        if (votacao?.diretor == null) return "Diretor"

        return ""
    }

    fun submit(){
        val msgError = validateVotes()

        if (msgError.isNotBlank()) {
            Toast.makeText(activity, "Finalize a votação para: $msgError", Toast.LENGTH_SHORT).show()
            return
        }

        lifecycleScope.launch (Dispatchers.Main){
            var msg = VotacaoResponseDTO(false, "Erro inesperado, tente novamente mais tarde.")
            try {
                withContext(Dispatchers.IO){
                    msg = api.confirmarVotos(votacao!!, user?.tokenVotacao?: -1, user?.accessToken?: "")
                    if (msg.sucesso) {
                        votacao!!.isFinished = true
                        databaseHelper.updateVotacao(votacao!!)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            if (msg.sucesso){
                binding.btnSubmitAll.isEnabled = false
                binding.btnSubmitAll.setOnClickListener { }

                Toast.makeText(activity, "Votos enviados com sucesso!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(activity, msg.mensagem, Toast.LENGTH_SHORT).show()
            }
        }
    }





    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}