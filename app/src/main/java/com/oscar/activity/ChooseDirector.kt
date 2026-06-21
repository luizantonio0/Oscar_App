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

class ChooseDirector : AppCompatActivity() {
    private lateinit var binding: ActivityChooseDirectorBinding
    private val api = ApiRequest()
    private var choosedDirector: Director? = null
    private var user: User? = null
    private var databaseHelper = DatabaseHelper()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUtil.initialConfig(this, ActivityChooseDirectorBinding::inflate)

        user = DatabaseHelper().findUser()

        showLoading()
        loadData()
        loadVotacao()

        binding.cdRadioGroup.setOnCheckedChangeListener { group, checkedId ->
            Toast.makeText(this, "Selected: ${checkedId}", Toast.LENGTH_SHORT).show()
            val name = group.findViewById<RadioButton>(checkedId).text
            binding.cdSelectedName.text = name
            val newDirector = Director(checkedId.toLong(), name.toString())
            choosedDirector = newDirector
        }

    }

    fun loadVotacao() {
        lifecycleScope.launch (Dispatchers.Main){
            withContext(Dispatchers.IO){
                val votacao = databaseHelper.findVotacao()
                if (votacao?.diretor != null) {
                    binding.cdSelectedName.text = votacao.diretor?.nome
                    choosedDirector = votacao.diretor
                }
            }
        }
    }

    fun loadData() {
        lifecycleScope.launch {
            try {
                val list = withContext(Dispatchers.IO) {
                    api.getDiretores(user?.accessToken?: "")
                }
                list.forEachIndexed { _, d ->
                    val radioB = RadioButton(this@ChooseDirector).apply {
                        text = d.nome
                        id = d.id.toInt()
                    }
                    binding.cdRadioGroup.addView(radioB)
                }
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
        binding.bdProgressbar.visibility = View.GONE
    }

    fun showLoading() {
        binding.bdProgressbar.visibility = View.VISIBLE
    }

    fun sendVoteDirector(view: View){
        lifecycleScope.launch(Dispatchers.Main) {
            if (choosedDirector == null) {
                Toast.makeText(this@ChooseDirector, "Selecione um Filme Antes", Toast.LENGTH_SHORT).show()
                return@launch
            }
            withContext(Dispatchers.IO) {
                databaseHelper.updateVotacao(
                    Votacao(
                        1L,
                        null,
                        choosedDirector
                    )
                )
            }
            this@ChooseDirector.finish()
        }
    }
    fun comeback(view: View){
        finish()
    }
}