package app.video.grabadoravideo.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import app.video.grabadoravideo.model.VideoFile
import app.video.grabadoravideo.viewmodel.MainViewModel

@Composable
fun VideoListScreen(
    onVideoSelected: (VideoFile) -> Unit,
    viewModel: MainViewModel = viewModel()
) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.loadVideos(context)
    }

    val videos by viewModel.videoList.collectAsState()

    LazyColumn(
        contentPadding = PaddingValues(16.dp)
    ) {
        items(videos) { video ->
            VideoListItem(
                videoFile = video,
                onClick = { onVideoSelected(video) },
                onDelete = { viewModel.deleteVideo(video) }
            )
            Divider()
        }
    }
}

@Composable
fun VideoListItem(
    videoFile: VideoFile,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = videoFile.name, modifier = Modifier.weight(1f))
        IconButton(onClick = onDelete) {
            Icon(imageVector = Icons.Default.Delete, contentDescription = "Eliminar")
        }
    }
}