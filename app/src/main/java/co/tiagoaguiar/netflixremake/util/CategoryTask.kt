package co.tiagoaguiar.netflixremake.util

import android.util.Log
import java.io.BufferedInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.lang.Exception
import java.net.URL
import java.util.concurrent.Executors
import javax.net.ssl.HttpsURLConnection

class CategoryTask {
    fun execute(url: String) {
        val executor = Executors.newSingleThreadExecutor()

        executor.execute {
            try {

                val requestUrl = URL(url)
                val urlConnection = requestUrl.openConnection() as HttpsURLConnection
                urlConnection.readTimeout = 2000
                urlConnection.connectTimeout = 2000

                val statusCode = urlConnection.responseCode
                if (statusCode >= 400) {
                    throw IOException("Erro na cominicação com o servidor")
                }

                val stream = urlConnection.inputStream
                // forma 1
//                val jsonAsString = stream.bufferedReader().use { it.readText() }
                // forma 2
                val buffer = BufferedInputStream(stream)
                val jsonAsString = toString(buffer)

                Log.i("teste", jsonAsString)

            } catch (e: IOException) {
                Log.e("Teste", e.message ?: "erro desconhecido", e)
            }
        }
    }

    private fun toString(stream: InputStream): String {
        val bytes = ByteArray(1024)
        var read: Int
        val outputStream = ByteArrayOutputStream()
        while (true) {
            read = stream.read(bytes)
            if (read <= 0) {
                break
            }
            outputStream.write(bytes, 0, read)
        }

        return String(outputStream.toByteArray())
    }
}