package com.kevin.asimbaya.cazarpatos

import android.app.Activity
import android.os.Environment
import java.io.File
import java.io.IOException

class FileExternalManager(private val actividad: Activity) : FileHandler {

    private fun isExternalStorageWritable(): Boolean {
        return Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
    }

    private fun isExternalStorageReadable(): Boolean {
        return Environment.getExternalStorageState() in setOf(
            Environment.MEDIA_MOUNTED,
            Environment.MEDIA_MOUNTED_READ_ONLY
        )
    }

    override fun SaveInformation(datosAGrabar: Pair<String, String>) {
        if (!isExternalStorageWritable()) return

        val file = File(actividad.getExternalFilesDir(null), SHAREDINFO_FILENAME)
        try {
            file.bufferedWriter().use { writer ->
                writer.write(datosAGrabar.first)
                writer.newLine()
                writer.write(datosAGrabar.second)
            }
        } catch (e: IOException) {
            e.printStackTrace() // Consider logging or user-friendly error handling
        }
    }

    override fun ReadInformation(): Pair<String, String> {
        if (!isExternalStorageReadable()) return "" to ""

        val file = File(actividad.getExternalFilesDir(null), SHAREDINFO_FILENAME)
        return try {
            file.bufferedReader().use { reader ->
                val lines = reader.readLines()
                val email = lines.getOrNull(0).orEmpty()
                val clave = lines.getOrNull(1).orEmpty()
                email to clave
            }
        } catch (e: Exception) {
            "" to ""
        }
    }
}
