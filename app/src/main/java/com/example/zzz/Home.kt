package com.example.zzz

import android.R.style
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController


@Composable
fun HomeScreen(navController: NavHostController) {
    Box(modifier = Modifier.fillMaxSize()) {

        Image(
            painter = painterResource(R.drawable.bk1),
            contentDescription = "Fundo",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            // Imagem centralizada no topo
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp),
                contentAlignment = Alignment.TopCenter
            ) {
                Image(
                    painter = painterResource(R.drawable.oneoficial_removebg_preview),
                    contentDescription = "Fundo",
                    modifier = Modifier
                        .width(250.dp) // ajuste o tamanho conforme necessário
                        .wrapContentHeight(),
                    contentScale = ContentScale.Fit
                )
            }
        }

        Text(
            text = "",
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 350.dp, start = 16.dp, end = 16.dp),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleLarge // ou outro estilo se quiser
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 48.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            Image(
                painter = painterResource(R.drawable.start),
                contentDescription = "Botão",
                modifier = Modifier
                    .padding(20.dp)
                    .size(120.dp) // Define o tamanho exato da imagem
                    .clickable(
                        indication = null, // Remove a sombra/efeito padrão
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        navController.navigate("second")
                    }
            )
        }
    }
}
@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    val navController = rememberNavController()
    HomeScreen(navController = navController)
}
