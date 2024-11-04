package app.video.grabadoravideo.utils

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

object FileUtils {
    fun createVideoFile(context: Context): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val fileName = "VID_$timeStamp.mp4"
        val storageDir = context.getExternalFilesDir(null)
        return File(storageDir, fileName)
    }

    fun getVideoFiles(context: Context): List<File> {
        val directory = context.getExternalFilesDir(null)
        return directory?.listFiles { _, name -> name.endsWith(".mp4") }?.toList() ?: emptyList()
    }

    fun getUriForFile(context: Context, file: File): Uri {
        return FileProvider.getUriForFile(
            context,
            "app.video.grabadoravideo.fileprovider",
            file
        )
    }
}