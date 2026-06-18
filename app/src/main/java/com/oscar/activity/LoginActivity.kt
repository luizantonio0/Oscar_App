package com.oscar.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

import com.oscar.R
import com.oscar.config.ActivityUtil
import com.oscar.config.ActivityUtil.Companion.passwordValid
import com.oscar.config.ActivityUtil.Companion.usernameValid
import com.oscar.data.model.User
import com.oscar.databinding.ActivityLoginBinding
import com.oscar.repository.DatabaseHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding


    private var user: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)


        user = DatabaseHelper().findUser()
        binding.username.setText(user?.username)

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


        val newUser = User().apply {
            this.username = binding.username.text.toString()
        }

        lifecycleScope.launch(Dispatchers.IO) {
            DatabaseHelper().saveUser(newUser, "", -1)
        }


        startActivity(Intent(this, HomeActivity::class.java))
    }

}