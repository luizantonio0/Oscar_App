package com.oscar.activity

import android.os.Bundle
import android.widget.RadioButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.forEachIndexed
import com.oscar.R
import com.oscar.data.model.Director
import com.oscar.databinding.ActivityChooseDirectorBinding
import com.oscar.databinding.ActivityHomeBinding

class ChooseDirector : AppCompatActivity() {
    private lateinit var binding: ActivityChooseDirectorBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_choose_director)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding = ActivityChooseDirectorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val tempList = listOf( Director(0L, "Luiz"), Director(1L, "Antonio"))

        tempList.forEachIndexed {  index, d ->
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
}