package com.oscar.service

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URL

class ImageDownloadService {
    companion object {
        suspend fun downloadImageBitmap(_url: String): Bitmap? {
            return withContext(Dispatchers.IO) {
                try {
                    val url = URL(_url)
                    url.openStream().use { inputStream ->
                        BitmapFactory.decodeStream(inputStream)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    null
                }
            }
        }
    }
}