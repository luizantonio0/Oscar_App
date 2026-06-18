package com.oscar.config

import android.content.Context
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import com.oscar.R

class ActivityUtil {
    companion object {
        fun initialConfig(){

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

