package app.video.grabadoravideo

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import app.video.grabadoravideo.ui.*
import app.video.grabadoravideo.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel: MainViewModel = viewModel()
            val navController = rememberNavController()
            NavHost(navController, startDestination = "main") {
                composable("main") {
                    MainScreen(
                        onRecordVideo = { navController.navigate("record") },
                        onViewVideos = { navController.navigate("videos") },
                        onPlayLastVideo = {
                            val lastVideo = viewModel.getLastVideo()
                            if (lastVideo != null) {
                                navController.navigate("player?uri=${lastVideo.uri}")
                            }
                        }
                    )
                }
                composable("record") {
                    VideoRecorderScreen(
                        onVideoRecorded = { uri ->
                            navController.popBackStack()
                        }
                    )
                }
                composable("videos") {
                    VideoListScreen(
                        onVideoSelected = { videoFile ->
                            navController.navigate("player?uri=${videoFile.uri}")
                        },
                        viewModel = viewModel
                    )
                }
                composable(
                    route = "player?uri={uri}",
                    arguments = listOf(navArgument("uri") { type = NavType.StringType })
                ) { backStackEntry ->
                    val uriString = backStackEntry.arguments?.getString("uri")
                    if (uriString != null) {
                        VideoPlayerScreen(videoUri = Uri.parse(uriString))
                    }
                }
            }
        }
    }
}