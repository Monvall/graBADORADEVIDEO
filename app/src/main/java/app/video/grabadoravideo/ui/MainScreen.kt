package app.video.grabadoravideo.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MainScreen(
    onRecordVideo: () -> Unit,
    onViewVideos: () -> Unit,
    onPlayLastVideo: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = onRecordVideo, modifier = Modifier.fillMaxWidth()) {
            Text(text = "Grabar Video")
        }
        Button(onClick = onViewVideos, modifier = Modifier.fillMaxWidth()) {
            Text(text = "Ver Videos Guardados")
        }
        Button(onClick = onPlayLastVideo, modifier = Modifier.fillMaxWidth()) {
            Text(text = "Reproducir Último Video")
        }
    }
}