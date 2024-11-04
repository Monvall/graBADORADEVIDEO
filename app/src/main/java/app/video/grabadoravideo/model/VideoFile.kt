package app.video.grabadoravideo.model


import android.net.Uri
import java.io.File

data class VideoFile(
    val file: File,
    val name: String,
    val uri: Uri
)
