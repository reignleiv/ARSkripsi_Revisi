package com.example.arskripsi_revisi.helpers

import android.content.Context
import android.util.Log
import java.io.File
import java.io.IOException
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request

class Model(private val name: String) {

    private val baseUrl = "https://arskripsi.irnhakim.com/public/"

    // Encode the model name to be URL-safe, ignoring the "model/" part
    private val urlSafeName: String
        get() {
            val prefix = "models/"
            return if (name.startsWith(prefix)) {
                prefix + URLEncoder.encode(name.substring(prefix.length), StandardCharsets.UTF_8.toString())
                    .replace("+", "%20")
            } else {
                URLEncoder.encode(name, StandardCharsets.UTF_8.toString())
                    .replace("+", "%20")
            }
        }


    // Construct full URL with the URL-safe name
    private val downloadUrl: String
        get() = "$baseUrl$urlSafeName"

    suspend fun download(context: Context): File? = withContext(Dispatchers.IO) {
        Log.d("ModelDownload", "Starting download for: $downloadUrl")

        val client = OkHttpClient()
        val request = Request.Builder().url(downloadUrl).build()

        try {
            val response = client.newCall(request).execute()
            if (!response.isSuccessful) {
                Log.e("ModelDownload", "Download failed with HTTP code: ${response.code}")
                throw IOException("Failed to download file: HTTP ${response.code}")
            }

            Log.d("ModelDownload", "Download successful. Saving file...")

            val directory = File(context.getExternalFilesDir(null), "models")
            if (!directory.exists()) {
                val dirCreated = directory.mkdirs() // Create the directory if it doesn't exist
                if (dirCreated) {
                    Log.d("ModelDownload", "Directory created: ${directory.absolutePath}")
                } else {
                    Log.e("ModelDownload", "Failed to create directory: ${directory.absolutePath}")
                }
            }

            val file = File(directory, name.substringAfterLast("/"))
            Log.d("ModelDownload", "Saving to: ${file.absolutePath}")

            response.body?.byteStream()?.use { inputStream ->
                file.outputStream().use { outputStream ->
                    inputStream.copyTo(outputStream)
                    Log.d("ModelDownload", "File saved successfully.")
                }
            }

            return@withContext file
        } catch (e: IOException) {
            Log.e("ModelDownload", "Error during download: ${e.message}")
            e.printStackTrace()
            return@withContext null
        }
    }

    override fun toString(): String {
        return "Model(name='$name', downloadUrl='$downloadUrl')"
    }
}

