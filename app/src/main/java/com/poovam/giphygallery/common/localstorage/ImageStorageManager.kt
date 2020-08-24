package com.poovam.giphygallery.common.localstorage

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.nio.ByteBuffer


class ImageStorageManager {
    companion object {
        suspend fun writeGif(gif: ByteBuffer, filePath: String, fileName: String): Boolean {
            return withContext(Dispatchers.IO) {
                try {
                    val dir = File(filePath)
                    if (!dir.exists()) {
                        dir.mkdir()
                    }
                    val gifFile = File(dir, fileName)
                    if(!gifFile.exists()){
                        gifFile.createNewFile()
                    }
                    val output = FileOutputStream(gifFile)
                    val bytes = ByteArray(gif.capacity())
                    (gif.duplicate().clear() as ByteBuffer).get(bytes)
                    output.write(bytes, 0, bytes.size)
                    output.close()
                    true
                } catch (e: Exception) {
                    e.printStackTrace()
                    false
                }
            }
        }

        suspend fun deleteGif(filePath: String, fileName: String){
            withContext(Dispatchers.IO) {
                try {
                    val file = File(filePath, fileName)
                    if(file.exists()){
                        file.delete()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}