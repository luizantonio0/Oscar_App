package com.oscar.activity

import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.oscar.R
import com.oscar.config.ActivityUtil
import com.oscar.data.model.Director
import com.oscar.databinding.ActivityChooseDirectorBinding

class ChooseDirector : AppCompatActivity() {
    private lateinit var binding: ActivityChooseDirectorBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUtil.initialConfig(this, ActivityChooseDirectorBinding::inflate)

        val tempList = listOf( Director(0L, "Luiz"), Director(1L, "Antonio"))

        tempList.forEachIndexed { _, d ->
            val radioB = RadioButton(this).apply {
                text = d.nome
                id = d.id.toInt()
            }

            binding.cdRadioGroup.addView(radioB)
        }

        binding.cdRadioGroup.setOnCheckedChangeListener { group, checkedId ->
            Toast.makeText(this, "Selected: ${checkedId}", Toast.LENGTH_SHORT).show()
            binding.cdSelectedName.text = group.findViewById<RadioButton>(checkedId).text
        }

    }
    fun sendVote(view: View){

    }
    fun comeback(view: View){
        finish()
    }
}