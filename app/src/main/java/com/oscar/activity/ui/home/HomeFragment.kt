package com.oscar.activity.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.oscar.R
import com.oscar.data.model.User
import com.oscar.databinding.FragmentHomeBinding
import com.oscar.repository.DatabaseHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    private var user: User? = null
    private var databaseHelper = DatabaseHelper()
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.containerLeave.setOnClickListener {
            sair()
        }
        carregarUsuario()

        return root
    }

    override fun onResume() {
        super.onResume()
        carregarUsuario()
    }

    private fun carregarUsuario() {
        lifecycleScope.launch {
            val usuarioEncontrado = withContext(Dispatchers.IO) {
                databaseHelper.findUser()
            }

            user = usuarioEncontrado

            if (user == null) {
                Toast.makeText(
                    requireContext(),
                    "Usuário não encontrado. Faça login novamente.",
                    Toast.LENGTH_SHORT
                ).show()
                return@launch
            }

            binding.welcomeText.text =
                "Olá, ${user?.username.orEmpty().replaceFirstChar { it.titlecase() }}"

            binding.txtNumeroTicket.text = user?.tokenVotacao?.toString()
        }
    }

    fun sair(){
        lifecycleScope.launch(Dispatchers.Main){
            withContext(Dispatchers.IO){
                if (user == null) return@withContext
                databaseHelper.deleteUser(user!!)
                databaseHelper.deleteVotacao()
            }
            requireActivity().finish()
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}