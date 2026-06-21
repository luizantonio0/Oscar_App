package com.oscar.activity

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.lifecycleScope
import com.oscar.R
import com.oscar.config.ActivityUtil
import com.oscar.config.ActivityUtil.Companion.passwordValid
import com.oscar.config.ActivityUtil.Companion.usernameValid
import com.oscar.data.dto.request.LoginRequestDTO
import com.oscar.data.model.User
import com.oscar.databinding.ActivityLoginBinding
import com.oscar.repository.DatabaseHelper
import com.oscar.service.ApiRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private var api = ApiRequest(this)


    private var user: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                user = DatabaseHelper().findUser()
            }
            binding.username.setText(user?.username)
        }

        binding.username.doAfterTextChanged { text ->
            if (usernameValid(text.toString())){
                binding.username.error = null
            } else {
                binding.username.error = getString(R.string.invalid_username)
            }
            validateForm()
        }

        binding.password.doAfterTextChanged { text ->
            if (passwordValid(text.toString())){
                binding.password.error = null
            } else {
                binding.password.error = getString(R.string.invalid_password)
            }
            validateForm()
        }


        uiConfig()
    }

    private fun uiConfig(){
        ActivityUtil.darkmode(this, binding.imageView!!)
    }

    private fun validateForm() {
        val username = binding.username.text.toString()
        val password = binding.password.text.toString()

        val formularioValido = usernameValid(username) && passwordValid(password)

        binding.login.isEnabled = formularioValido
    }

    fun login(view: View){
        lifecycleScope.launch(Dispatchers.Main) {
            val loginRequest = LoginRequestDTO(
                binding.username.text.toString(),
                binding.password.text.toString()
            )
            try {
                val loginResponse = withContext(Dispatchers.IO) {
                    api.login(loginRequest)
                }


                val newUser = User().apply {
                    this.username = binding.username.text.toString()
                    this.tokenVotacao = loginResponse.tokenVotacao
                    this.accessToken = loginResponse.token
                }

                withContext(Dispatchers.IO) {
                    DatabaseHelper().saveUser(
                        newUser,
                        newUser.accessToken,
                        newUser.tokenVotacao
                    )
                }

                finish()
            } catch (e: Exception) {
                e.printStackTrace()
                finish()
                return@launch
            }
        }
    }

}