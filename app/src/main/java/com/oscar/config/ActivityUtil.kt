package com.oscar.config

import android.app.Activity
import android.content.Context
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.ImageViewCompat
import com.oscar.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.oscar.config.OnGenericAdapterClickListener
import com.oscar.data.model.Movie
import com.oscar.databinding.ActivityHomeBinding


class ActivityUtil {
    companion object {
        fun <T: ViewBinding> initialConfig(activity: Activity, bindingInflater: (LayoutInflater) -> T): T {

            val binding = bindingInflater(activity.layoutInflater)
            activity.setContentView(binding.root)

            val mainView = binding.root.findViewById<View>(R.id.main)

            ViewCompat.setOnApplyWindowInsetsListener(mainView) { v, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                insets
            }

            return binding
        }

        fun darkmode (context: Context, imageView: ImageView){
            val nightModeFlags = context.resources.configuration.uiMode and android.content.res.Configuration.UI_MODE_NIGHT_MASK
            val isDarkMode = nightModeFlags == android.content.res.Configuration.UI_MODE_NIGHT_YES
            val colorStateList = ContextCompat.getColorStateList(context, R.color.white)

            if(isDarkMode){
                ImageViewCompat.setImageTintList(imageView, colorStateList)
            } else{
                imageView.setColorFilter(ContextCompat.getColor(context, R.color.white_darkmode), android.graphics.PorterDuff.Mode.MULTIPLY)
            }
        }

        fun usernameValid(string: String) : Boolean {
            return string != "" && string.all { it.isLetter() }
        }

        fun passwordValid(string: String) : Boolean {
            return string != ""
        }
    }
    private constructor()
}

