package app.video.grabadoravideo.utils

import android.Manifest
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.google.accompanist.permissions.*

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RequestPermissions(
    permissions: List<String>,
    onPermissionsResult: (Boolean) -> Unit
) {
    val multiplePermissionsState = rememberMultiplePermissionsState(permissions)
    LaunchedEffect(Unit) {
        multiplePermissionsState.launchMultiplePermissionRequest()
    }

    onPermissionsResult(multiplePermissionsState.allPermissionsGranted)
}