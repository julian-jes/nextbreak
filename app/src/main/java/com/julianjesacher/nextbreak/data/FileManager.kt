package com.julianjesacher.nextbreak.data

import android.content.Context
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okio.IOException
import java.io.File

object FileManager {
    private lateinit var context: Context

    fun init(appContext: Context) {
        context = appContext.applicationContext
    }

    suspend fun saveFile(fileName: String, content: String){
        withContext(Dispatchers.IO) {
            try {
                val file = File(context.filesDir, fileName)
                file.writeText(content)
            } catch (e: IOException) {
                Log.e("File Manager", "Error while saving $fileName : $e")
            }
        }
    }

    suspend fun loadFile(fileName: String): String? {
        return withContext(Dispatchers.IO) {
            try {
                val file = File(context.filesDir, fileName)
                if(file.exists()) {
                    file.readText()
                } else {
                    null
                }
            } catch (e: IOException) {
                Log.e("File Manager", "Error while loading $fileName : $e")
                null
            }
        }
    }
}