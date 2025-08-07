package com.example.weatherapp.pomodoro

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PaintingStyle.Companion.Stroke
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherapp.MainViewModel
import com.example.weatherapp.ui.book.Book
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PomodoroScreen(mainViewModel: MainViewModel) {
    val books = mainViewModel.books
    var selectedBook by remember { mutableStateOf<Book?>(null) }
    var isWorkMode by remember { mutableStateOf(true) }
    var workMinutes by remember { mutableStateOf(25f) }
    var restMinutes by remember { mutableStateOf(5f) }
    var timerRunning by remember { mutableStateOf(false) }
    var secondsLeft by remember { mutableStateOf((workMinutes * 60).toInt()) }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    // Atualiza o tempo quando o usuário muda o slider
    LaunchedEffect(workMinutes, restMinutes, isWorkMode) {
        if (!timerRunning) {
            secondsLeft = if (isWorkMode) (workMinutes * 60).toInt() else (restMinutes * 60).toInt()
        }
    }

    Column(
        Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Selecione o livro para a sessão Pomodoro", style = MaterialTheme.typography.titleLarge)
        if (books.isEmpty()) {
            Text("Nenhum livro encontrado. Adicione livros na aba de busca.", color = MaterialTheme.colorScheme.error)
        } else {
            LazyColumn(Modifier.height(120.dp)) {
                items(books) { book ->
                    ListItem(
                        headlineContent = { Text(book.title) },
                        supportingContent = { Text(book.author) },
                        trailingContent = {
                            if (selectedBook == book) {
                                Icon(Icons.Default.Book, contentDescription = null)
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedBook = book }
                    )
                }
            }
        }
        Spacer(Modifier.height(16.dp))
        if (selectedBook != null) {
            Box(
                modifier = Modifier
                    .size(240.dp)
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularTimer(
                    totalSeconds = if (isWorkMode) (workMinutes * 60).toInt() else (restMinutes * 60).toInt(),
                    secondsLeft = secondsLeft,
                    isWorkMode = isWorkMode
                )
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.align(Alignment.Center)
                ) {
                    Text(
                        text = "%02d:%02d".format(secondsLeft / 60, secondsLeft % 60),
                        fontWeight = FontWeight.Bold,
                        fontSize = 44.sp,
                        color = if (isWorkMode) Color(0xFF388E3C) else Color(0xFF455A64)
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = if (isWorkMode) "Work" else "Rest",
                        fontWeight = FontWeight.Medium,
                        fontSize = 18.sp,
                        color = Color.Gray
                    )
                }
            }

            Spacer(Modifier.height(24.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text("Pomodoro", modifier = Modifier.width(90.dp), fontWeight = FontWeight.Medium)
                Slider(
                    value = workMinutes,
                    onValueChange = { workMinutes = it },
                    valueRange = 1f..60f,
                    enabled = !timerRunning && isWorkMode,
                    colors = SliderDefaults.colors(
                        thumbColor = Color(0xFF388E3C),
                        activeTrackColor = Color(0xFF81C784)
                    )
                )
                Text("${workMinutes.roundToInt()} min", modifier = Modifier.width(50.dp), fontWeight = FontWeight.Bold)
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text("Descanso", modifier = Modifier.width(90.dp), fontWeight = FontWeight.Medium)
                Slider(
                    value = restMinutes,
                    onValueChange = { restMinutes = it },
                    valueRange = 1f..30f,
                    enabled = !timerRunning && !isWorkMode,
                    colors = SliderDefaults.colors(
                        thumbColor = Color(0xFF455A64),
                        activeTrackColor = Color(0xFF90A4AE)
                    )
                )
                Text("${restMinutes.roundToInt()} min", modifier = Modifier.width(50.dp), fontWeight = FontWeight.Bold)
            }

            Spacer(Modifier.height(24.dp))

            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        if (timerRunning) {
                            timerRunning = false
                        } else {
                            timerRunning = true
                            mainViewModel.startPomodoroSession(selectedBook!!)
                            scope.launch {
                                while (secondsLeft > 0 && timerRunning) {
                                    delay(1000)
                                    secondsLeft--
                                }
                                if (timerRunning) {
                                    if (isWorkMode) {
                                        mainViewModel.addPomodoroSession(selectedBook!!)
                                    }
                                    sendPomodoroNotification(context, selectedBook!!.title, isWorkMode)
                                    isWorkMode = !isWorkMode
                                    secondsLeft = if (isWorkMode) (workMinutes * 60).toInt() else (restMinutes * 60).toInt()
                                    timerRunning = false
                                }
                            }
                        }
                    },
                    modifier = Modifier.size(80.dp)
                ) {
                    Icon(
                        imageVector = if (timerRunning) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = if (timerRunning) "Pause" else "Play",
                        modifier = Modifier.size(64.dp),
                        tint = if (isWorkMode) Color(0xFF388E3C) else Color(0xFF455A64)
                    )
                }
            }
        }
    }
}

@Composable
fun CircularTimer(totalSeconds: Int, secondsLeft: Int, isWorkMode: Boolean) {
    val progress = 1f - (secondsLeft / totalSeconds.toFloat())
    val color = if (isWorkMode) Color(0xFF4CAF50) else Color(0xFF607D8B)
    Canvas(modifier = Modifier.size(180.dp)) {
        drawArc(
            color = Color.LightGray,
            startAngle = -90f,
            sweepAngle = 360f,
            useCenter = false,
            style = Stroke(width = 18f, cap = StrokeCap.Round)
        )
        drawArc(
            color = color,
            startAngle = -90f,
            sweepAngle = 360f * progress,
            useCenter = false,
            style = Stroke(width = 18f, cap = StrokeCap.Round)
        )
        drawCircle(
            color = Color.White,
            radius = size.minDimension / 2 - 18f,
            center = center
        )
    }
}

fun sendPomodoroNotification(context: Context, bookTitle: String, isWorkMode: Boolean) {
    val channelId = "pomodoro_channel"
    val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(channelId, "Pomodoro", NotificationManager.IMPORTANCE_DEFAULT)
        manager.createNotificationChannel(channel)
    }
    val modeText = if (isWorkMode) "Sessão de trabalho finalizada!" else "Descanso finalizado!"
    val builder = androidx.core.app.NotificationCompat.Builder(context, channelId)
        .setSmallIcon(android.R.drawable.ic_dialog_info)
        .setContentTitle(modeText)
        .setContentText("Livro: \"$bookTitle\"")
        .setPriority(androidx.core.app.NotificationCompat.PRIORITY_DEFAULT)
    manager.notify(System.currentTimeMillis().toInt(), builder.build())
}