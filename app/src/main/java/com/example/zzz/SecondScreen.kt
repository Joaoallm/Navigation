package com.example.zzz

import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.DatePickerDefaults.colors
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

@Composable
fun SecondScreen(navController: NavHostController) {
    val context = LocalContext.current

    // Fundo
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable._backgroud),
            contentDescription = "Fundo",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Botão de voltar no canto superior esquerdo
        IconButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.TopStart)
                .background(Color.White.copy(alpha = 0.5f), shape = CircleShape)
        ) {
            Icon(
                imageVector = Icons.Default.Refresh,
                contentDescription = "Voltar",
                tint = Color.Black
            )
        }
    }
    val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC).toFloat()
    var volume by remember {
        mutableStateOf(
            audioManager.getStreamVolume(AudioManager.STREAM_MUSIC).toFloat()
        )
    }

    val musicas = listOf(
        Musica("A Mother's Love", R.raw.amotherslove),
        Musica("Dr. Hiruluk", R.raw.drhiruluk),
        Musica("Gold and Oden", R.raw.goldandoden),
        Musica("If You Live", R.raw.ifyoulive),
        Musica("Mother Sea", R.raw.mothersea),
        Musica("Oden Store", R.raw.odenstore)
    )

    var mediaPlayer by remember { mutableStateOf<MediaPlayer?>(null) }
    var musicaAtual by remember { mutableStateOf<Musica?>(null) }
    var isPlaying by remember { mutableStateOf(false) }
    var currentPosition by remember { mutableStateOf(0) }
    var duration by remember { mutableStateOf(0) }

    DisposableEffect(Unit) {
        val handler = Handler(Looper.getMainLooper())
        val updateTime = object : Runnable {
            override fun run() {
                mediaPlayer?.let {
                    currentPosition = it.currentPosition
                    duration = it.duration
                    handler.postDelayed(this, 1000)
                }
            }
        }
        handler.post(updateTime)

        onDispose {
            handler.removeCallbacks(updateTime)
            mediaPlayer?.release()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.bk2),
            contentDescription = "Fundo",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Image(
                    painter = painterResource(R.drawable.oneoficial_removebg_preview),
                    contentDescription = "Logo",
                    modifier = Modifier
                        .width(250.dp)
                        .padding(top = 16.dp),
                    contentScale = ContentScale.Fit
                )

                Spacer(modifier = Modifier.height(8.dp))
                Text("Escolha uma música:", style = MaterialTheme.typography.titleLarge)
            }

            items(musicas) { musica ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {

                        // Linha superior: nome + botão play
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = musica.nome,
                                style = MaterialTheme.typography.titleMedium
                            )

                            Button(onClick = {
                                if (musicaAtual == musica) {
                                    if (isPlaying) {
                                        mediaPlayer?.pause()
                                        isPlaying = false
                                    } else {
                                        mediaPlayer?.start()
                                        isPlaying = true
                                    }
                                } else {
                                    mediaPlayer?.release()
                                    mediaPlayer = MediaPlayer.create(context, musica.resId).apply {
                                        isLooping = false
                                        start()
                                    }
                                    musicaAtual = musica
                                    isPlaying = true
                                }
                            },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF0077BE),
                                    contentColor = Color.White
                                ),
                                ) {
                                Text(if (musicaAtual == musica && isPlaying) "Pause" else "Play")
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Linha inferior: tempo atual / duração
                        Text(
                            text = if (musicaAtual == musica) {
                                "${formatTime(currentPosition)} / ${formatTime(duration)}"
                            } else {
                                "00:00 / 00:00"
                            },
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.align(Alignment.Start)
                        )

                        // Slider apenas se for a música atual
                        if (musicaAtual == musica) {
                            Slider(
                                value = currentPosition.toFloat(),
                                onValueChange = {
                                    mediaPlayer?.seekTo(it.toInt())
                                    currentPosition = it.toInt()
                                },
                                valueRange = 0f..(duration.toFloat().coerceAtLeast(1f)),
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))

                Text("Volume: ${volume.toInt()}/${maxVolume.toInt()}")
                Slider(
                    value = volume,
                    onValueChange = {
                        volume = it
                        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, it.toInt(), 0)
                    },
                    valueRange = 0f..maxVolume
                )
            }
        }
    }
}

data class Musica(val nome: String, val resId: Int)

fun formatTime(ms: Int): String {
    val totalSeconds = ms / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return "%02d:%02d".format(minutes, seconds)
}


@Preview(showBackground = true)
@Composable
fun SecondScreenPreview() {
    SecondScreen(navController = rememberNavController())
}
