package app.video.grabadoravideo.ui

import android.Manifest
import android.content.Context
import android.net.Uri
import androidx.camera.core.CameraSelector
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.video.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FiberManualRecord
import androidx.compose.material.icons.filled.Stop
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import app.video.grabadoravideo.utils.FileUtils
import java.util.concurrent.Executor

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun VideoRecorderScreen(
    onVideoRecorded: (Uri) -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val executor: Executor = ContextCompat.getMainExecutor(context)

    val permissionsState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO
        )
    )

    LaunchedEffect(Unit) {
        permissionsState.launchMultiplePermissionRequest()
    }

    if (permissionsState.allPermissionsGranted) {
        var recording by remember { mutableStateOf<Recording?>(null) }
        val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
        val recorder = remember {
            Recorder.Builder()
                .setQualitySelector(QualitySelector.from(Quality.HIGHEST))
                .build()
        }
        val videoCapture = remember { VideoCapture.withOutput(recorder) }
        val previewView = remember { androidx.camera.view.PreviewView(context) }

        LaunchedEffect(cameraProviderFuture) {
            val cameraProvider = cameraProviderFuture.get()
            val preview = androidx.camera.core.Preview.Builder().build().also {
                it.setSurfaceProvider(previewView.surfaceProvider)
            }
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    lifecycleOwner,
                    cameraSelector,
                    preview,
                    videoCapture
                )
            } catch (e: Exception) {
                // Manejar excepción
            }
        }

        Box(modifier = Modifier.fillMaxSize()) {
            AndroidView(
                factory = { previewView },
                modifier = Modifier.fillMaxSize()
            )
            IconButton(
                onClick = {
                    if (recording == null) {
                        val file = FileUtils.createVideoFile(context)
                        val outputOptions = FileOutputOptions.Builder(file).build()
                        recording = videoCapture.output.prepareRecording(context, outputOptions)
                            .withAudioEnabled()
                            .start(executor) { event ->
                                if (event is VideoRecordEvent.Finalize) {
                                    if (!event.hasError()) {
                                        onVideoRecorded(Uri.fromFile(file))
                                    } else {
                                        // Manejar error
                                    }
                                    recording = null
                                }
                            }
                    } else {
                        recording?.stop()
                        recording = null
                    }
                },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 16.dp)
            ) {
                Icon(
                    imageVector = if (recording == null) Icons.Default.FiberManualRecord else Icons.Default.Stop,
                    contentDescription = "Grabar",
                    tint = if (recording == null) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(64.dp)
                )
            }
        }
    } else {
        // Mostrar mensaje de permisos
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Se requieren permisos para usar la cámara y el micrófono.")
        }
    }
}