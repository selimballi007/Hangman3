package org.hangman3.screens

import NotebookBackground
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.hangman3.ads.BannerAdView
import org.hangman3.ads.provideAdsService
import org.hangman3.navigation.GameViewModel
import org.hangman3.utils.getGloriaFontFamily
import kotlin.math.PI
import kotlin.math.sin
import kotlin.random.Random
import kotlin.time.Clock
import kotlin.time.ExperimentalTime


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreen(gameViewModel: GameViewModel) {
    var adCount=0
    val ads = provideAdsService()

    LaunchedEffect(Unit) {
        // Prepare Interstitial when the game screen is on
        ads.loadInterstitial()
    }

    val gameState by gameViewModel.gameState.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        NotebookBackground(
            modifier = Modifier.fillMaxSize()
        )
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "Level: ${gameState.wordToGuess.id}",
                            fontFamily = getGloriaFontFamily()
                        )
                    },
                    navigationIcon = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(onClick = { gameViewModel.navigateBack() }) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Back"
                                )
                            }
                        }
                    },
                    actions = {
                        Text(
                            text = "Score: ${gameState.score}",
                            fontFamily = getGloriaFontFamily(),
                            fontSize = 22.sp,
                            modifier = Modifier.padding(end = 16.dp)
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors( containerColor = Color.Transparent)
                )
            },
            bottomBar = {
                // Test banner id (development) — don't publish, change with your own id before publish
                BannerAdView(adUnitId = "ca-app-pub-3940256099942544/6300978111")
            },
            containerColor = Color.Transparent,
            contentColor = Color.Unspecified
        )
        { innerPadding ->

            Column(
                modifier = Modifier.fillMaxSize().padding(innerPadding),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    HangmanDrawing(
                        remainingAttempts = gameState.remainingAttempts,
                        modifier = Modifier
                            .weight(3f)
                            .height(200.dp)
                            .padding(20.dp),
                        gameOver = gameState.isGameOver
                    )
                    ExplanationCard(
                        modifier = Modifier.weight(3f),
                        text = gameState.wordToGuess.explanation
                    )
                }

                Spacer(
                    modifier = Modifier
                        .height(16.dp)
                )

                Text(
                    text = gameState.displayWord,
                    fontFamily = getGloriaFontFamily(),
                    fontSize = 30.sp,
                    textAlign = TextAlign.Center,
                    color = Color.Green

                )
                Text(
                    text = gameState.remainingAttempts.toString(),
                    fontFamily = getGloriaFontFamily()
                )
                if (!gameViewModel.gameState.value.isGameOver) {
                    DisplayKeyboard(
                        onLetterClick = { letter -> gameViewModel.guessLetter(letter) },
                        isDisabled = { letter -> gameState.guessedLetters.contains(letter) },
                        isCorrect = {letter -> gameState.wordToGuess.word.contains(letter)}
                    )
                }else{
                    adCount++
                    if(adCount==3){
                        ads.showInterstitial()
                        adCount=0
                    }
                    SketchButton("Play Again", { gameViewModel.resetGame() })
                    ads.loadInterstitial()
                }
            }
        }
    }
}

@Composable
fun SketchButton(
    text: String,
    onClick: () -> Unit
) {
    var pressed by remember { mutableStateOf(false) }

    // Ölçek animasyonu (0.95x - 1.1x arası gidip geliyor)
    val scale by animateFloatAsState(
        targetValue = if (pressed) 1.1f else 1f,
        animationSpec = tween(durationMillis = 150),
        label = "buttonScale"
    )

    Box(
        modifier = Modifier
            .graphicsLayer(
                scaleX = scale,
                scaleY = scale
            )
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        pressed = true
                        tryAwaitRelease() // parmağı kaldırana kadar bekle
                        pressed = false
                        onClick()
                    }
                )
            }
            .padding(8.dp)
    ) {
        Canvas(
            modifier = Modifier.matchParentSize()
        ) {
            val strokeWidth = 6f
            val paintColor = Color.Black

            val path = Path().apply {
                moveTo(10f, 20f)
                lineTo(size.width - 25f, 5f)
                lineTo(size.width - 5f, size.height - 25f)
                lineTo(15f, size.height - 5f)
                close()
            }

            drawPath(path, color = Color(0xFFFFEB3B), style = Fill)

            drawPath(
                path,
                color = paintColor,
                style = Stroke(
                    width = strokeWidth,
                    pathEffect = PathEffect.cornerPathEffect(20f)
                )
            )
        }

        Text(
            text = text,
            fontSize = 18.sp,
            color = Color.Black,
            fontFamily = getGloriaFontFamily(),
            modifier = Modifier
                .align(Alignment.Center)
                .padding(horizontal = 24.dp, vertical = 12.dp)
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DisplayKeyboard(
    onLetterClick: (Char) -> Unit,
    isDisabled: (Char) -> Boolean,
    isCorrect: (Char) -> Boolean
){
    FlowRow(
        horizontalArrangement = Arrangement.Center,
        verticalArrangement = Arrangement.Center
    ) {
        ('A'..'Z').forEach { letter ->
            TextButton(
                onClick = {onLetterClick(letter)},
                enabled = !isDisabled(letter) ,
                modifier = Modifier.padding(2.dp).width(30.dp).height(42.dp),
                contentPadding = PaddingValues(0.dp),

            ) {
                Text(
                    text = letter.uppercase(),
                    fontSize = 30.sp,
                    fontFamily = getGloriaFontFamily(),
                    textAlign = TextAlign.Center,
                    color = if (isDisabled(letter)){
                        if (isCorrect(letter)) Color.Green else Color.Red
                    }else Color.Black
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DisplayKeyboard0(
    onLetterClick: (Char) -> Unit,
    isDisabled: (Char) -> Boolean,
    isCorrect: (Char) -> Boolean
){
    FlowRow(
        horizontalArrangement = Arrangement.Center,
        verticalArrangement = Arrangement.Center
    ) {
        ('A'..'Z').forEach { letter ->
            TextButton(
                onClick = {onLetterClick(letter)},
                enabled = !isDisabled(letter) ,
                modifier = Modifier.padding(2.dp).width(30.dp)

            ) {
                Text(
                    text = letter.uppercase(),
                    fontSize = 30.sp,
                    fontFamily = getGloriaFontFamily(),
                    textAlign = TextAlign.Center,
                    color = if (isDisabled(letter)){
                                if (isCorrect(letter)) Color.Green else Color.Red
                            }else Color.Black
                )
            }
        }
    }
}


@Composable
fun ExplanationCard(modifier: Modifier, text : String) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent,
        )
    ) {
        Text(
            text = text,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = getGloriaFontFamily(),
            modifier = Modifier
                .padding(16.dp),
            textAlign = TextAlign.Center,
        )
    }
}

@OptIn(ExperimentalTime::class)
@Composable
fun HangmanDrawing(
    remainingAttempts: Int,
    modifier: Modifier,
    gameOver: Boolean = false // true ise sallama animasyonu başlar
) {
    val random = remember { Random(Clock.System.now().toEpochMilliseconds()) }

    // Sallanma animasyonu
    val swing by rememberInfiniteTransition().animateFloat(
        initialValue = -15f,
        targetValue = 15f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Canvas(modifier = modifier) {
        val partCount = 10 - remainingAttempts.coerceIn(0, 10)
        val topX = size.width
        val topY = size.height * 0.1f

        // Rope pivot
        val pivot = Offset(topX * 0.6f, topY)

        // === Helper Fonksiyonlar ===
        fun taperedLine(start: Offset, end: Offset, width: Float = 6f) {
            drawTaperedLine(color = Color.Black, start = start, end = end, baseWidth = width, segments = 20, random = random)
        }

        fun taperedCircle(center: Offset, radius: Float, fill: Boolean = false) {
            if (fill) {
                drawCircle(color = Color.Black, radius = radius, center = center)
            } else {
                drawCircle(color = Color.Black, radius = radius, center = center, style = Stroke(width = 6f))
            }
        }

        // === Sabit parçalar ===
        if (partCount >= 1) taperedLine(Offset(0f, size.height), Offset(topX, size.height), 8f) // Base
        if (partCount >= 2) taperedLine(Offset(topX / 5, size.height), Offset(topX / 5, topY), 8f) // Pole
        if (partCount >= 3) taperedLine(Offset(topX / 5, topY), Offset(topX * 0.6f, topY), 8f) // Beam
        if (partCount >= 4) taperedLine(Offset(topX * 0.6f, topY), Offset(topX * 0.6f, topY * 1.5f), 6f) // Rope

        // === Sallanacak parçalar ===
        val drawSwing: DrawScope.() -> Unit = {
            if (partCount >= 5) taperedCircle(Offset(topX * 0.6f, topY * 2.4f), topY * 0.9f, fill = false) // Head
            if (partCount >= 6) taperedLine(Offset(topX * 0.6f, topY * 3.3f), Offset(topX * 0.6f, topY * 6f), 6f) // Body
            if (partCount >= 7) taperedLine(Offset(topX * 0.6f, topY * 3.5f), Offset(topX * 0.45f, topY * 5f), 5f) // Left Arm
            if (partCount >= 8) taperedLine(Offset(topX * 0.6f, topY * 3.5f), Offset(topX * 0.75f, topY * 5f), 5f) // Right Arm
            if (partCount >= 9) taperedLine(Offset(topX * 0.6f, topY * 6f), Offset(topX * 0.45f, topY * 7.5f), 5f) // Left Leg
            if (partCount >= 10) taperedLine(Offset(topX * 0.6f, topY * 6f), Offset(topX * 0.75f, topY * 7.5f), 5f) // Right Leg
        }

        if (gameOver) {
            withTransform({ rotate(swing, pivot) }) {
                drawSwing()
            }
        } else {
            drawSwing()
        }
    }
}

fun DrawScope.drawTaperedLine(
    color: Color,
    start: Offset,
    end: Offset,
    baseWidth: Float = 6f,
    segments: Int = 20,
    random: Random? = null
) {
    val dx = (end.x - start.x) / segments
    val dy = (end.y - start.y) / segments

    for (i in 0 until segments) {
        val x1 = start.x + dx * i
        val y1 = start.y + dy * i
        val x2 = start.x + dx * (i + 1)
        val y2 = start.y + dy * (i + 1)

        val factor = sin((i.toFloat() / segments) * PI).toFloat()
        var width = baseWidth * (0.7f + 0.6f * factor)
        if (random != null) width *= (0.9f + random.nextFloat() * 0.2f)

        drawLine(
            color = color,
            start = Offset(x1, y1),
            end = Offset(x2, y2),
            strokeWidth = width,
            cap = StrokeCap.Round
        )
    }
}