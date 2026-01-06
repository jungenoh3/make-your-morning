package com.nochunsam.makeyourmorning.pages.record.screen

import android.app.Application
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nochunsam.makeyourmorning.common.compose.CustomColumn
import com.nochunsam.makeyourmorning.common.data.DayRecord
import com.nochunsam.makeyourmorning.utilities.database.AppRepository
import java.text.SimpleDateFormat
import java.util.*

// Y축 설정값
private const val MIN_HOUR = 4  // 오전 4시
private const val MAX_HOUR = 14 // 오후 2시 (14시)
private const val TOTAL_MINUTES_RANGE = (MAX_HOUR - MIN_HOUR) * 60f

fun createMockDate(dayOffset: Int, hour: Int, minute: Int): Date {
    val calendar = Calendar.getInstance()
    calendar.add(Calendar.DAY_OF_YEAR, -dayOffset) // dayOffset일 전으로 이동
    calendar.set(Calendar.HOUR_OF_DAY, hour)
    calendar.set(Calendar.MINUTE, minute)
    calendar.set(Calendar.SECOND, 0)
    return calendar.time
}

// 2. 임시 데이터 20개 리스트
val mockRecords = listOf(
    // 최근 (0일 전 ~ )
    DayRecord(id = 1, date = createMockDate(0, 7, 10), minute = 45),  // 오늘: 07:10 시작, 45분간
    DayRecord(id = 2, date = createMockDate(1, 6, 30), minute = 30),  // 어제: 06:30 시작, 30분간
    DayRecord(id = 3, date = createMockDate(2, 8, 0), minute = 20),   // 2일전: 08:00 시작 (조금 늦음)
    DayRecord(id = 4, date = createMockDate(3, 6, 15), minute = 50),  // 얼리버드
    DayRecord(id = 5, date = createMockDate(4, 7, 45), minute = 15),  // 짧게 함

    // 5~9일 전
    DayRecord(id = 6, date = createMockDate(5, 5, 50), minute = 60),  // 05:50 시작, 1시간 풀타임
    DayRecord(id = 7, date = createMockDate(6, 9, 30), minute = 10),  // 09:30 늦잠
    DayRecord(id = 8, date = createMockDate(7, 7, 0), minute = 40),
    DayRecord(id = 9, date = createMockDate(8, 6, 45), minute = 35),
    DayRecord(id = 10, date = createMockDate(9, 13, 0), minute = 25), // 오후 1시 (늦게 함)

    // 10~14일 전
    DayRecord(id = 11, date = createMockDate(10, 4, 30), minute = 55), // 새벽 4시 반 (가장 빠름)
    DayRecord(id = 12, date = createMockDate(11, 7, 20), minute = 30),
    DayRecord(id = 13, date = createMockDate(12, 8, 10), minute = 45),
    DayRecord(id = 14, date = createMockDate(13, 7, 5), minute = 20),
    DayRecord(id = 15, date = createMockDate(14, 6, 0), minute = 60),

    // 15~19일 전
    DayRecord(id = 16, date = createMockDate(15, 10, 0), minute = 15), // 10시
    DayRecord(id = 17, date = createMockDate(16, 7, 30), minute = 40),
    DayRecord(id = 18, date = createMockDate(17, 6, 50), minute = 35),
    DayRecord(id = 19, date = createMockDate(18, 5, 40), minute = 50),
    DayRecord(id = 20, date = createMockDate(19, 8, 30), minute = 25)
)

@Composable
fun RecordScreen() {
    val context = LocalContext.current
    val repository = remember { AppRepository(context.applicationContext as Application) }

    // DB에서 데이터 구독
    val recordsState by repository.getEarliestRecordPerDay().collectAsState(initial = emptyList())

    CustomColumn (
        paddingValues = PaddingValues(15.dp)
    ) {
        if (recordsState.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("아직 기록이 없습니다.", color = Color.Gray)
            }
        } else {
            // 차트 영역
            MorningRoutineChart(records = recordsState, modifier = Modifier.weight(1f))
        }
    }
}

@Composable
fun MorningRoutineChart(
    records: List<DayRecord>,
    modifier: Modifier = Modifier
) {
    // 정렬
    val sortedRecords = remember(records) { records.sortedBy { it.date } }

    val scrollState = rememberScrollState()
    val textMeasurer = rememberTextMeasurer()
    val colorScheme = MaterialTheme.colorScheme

    // 화면이 처음 그려질 때 스크롤을 맨 오른쪽(최신 날짜)으로 이동
    LaunchedEffect(Unit) {
        scrollState.scrollTo(scrollState.maxValue)
    }

    val barWidth = 30.dp // 막대 너비를 조금 키웠습니다
    val barSpacing = 40.dp

    // 전체 너비 계산
    val totalWidth = (barWidth + barSpacing) * sortedRecords.size + 20.dp

    Row(
        modifier = modifier.fillMaxWidth() // 받아온 modifier(weight) 적용
    ) {
        // Y축 라벨 (높이를 fillMaxHeight로 설정하여 전체 높이 대응)
        YAxisLabels(modifier = Modifier.fillMaxHeight())

        // 그래프 영역
        Box(
            modifier = Modifier
                .fillMaxHeight() // 높이 꽉 채우기
                .horizontalScroll(scrollState) // 스크롤 적용
                .width(totalWidth.coerceAtLeast(100.dp))
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val canvasHeight = size.height
                val startX = 10.dp.toPx()

                // 가이드라인
                drawGuideLines(chartHeight = canvasHeight, color = colorScheme.onSurface)

                // 데이터 그리기 (정렬된 데이터 사용)
                sortedRecords.forEachIndexed { index, record ->
                    val xOffset = startX + index * (barWidth.toPx() + barSpacing.toPx())

                    drawRecordBar(
                        record = record,
                        xOffset = xOffset,
                        barWidth = barWidth.toPx(),
                        canvasHeight = canvasHeight,
                        textMeasurer = textMeasurer,
                        barColor = colorScheme.primary
                    )
                }
            }
        }
    }
}

@Composable
fun YAxisLabels(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(end = 8.dp), // fillMaxHeight는 호출하는 쪽에서 제어
        verticalArrangement = Arrangement.SpaceBetween // 위아래로 균등 배치
    ) {
        Text("14:00", style = TextStyle(fontSize = 15.sp))
        Text("12:00", style = TextStyle(fontSize = 15.sp))
        Text("10:00", style = TextStyle(fontSize = 15.sp))
        Text("08:00", style = TextStyle(fontSize = 15.sp))
        Text("06:00", style = TextStyle(fontSize = 15.sp))
        Text("04:00", style = TextStyle(fontSize = 15.sp))
    }
}

private fun DrawScope.drawGuideLines(chartHeight: Float, color: Color) {
    val step = chartHeight / 5
    for (i in 0..5) {
        val y = i * step
        drawLine(
            color = color,
            start = Offset(0f, y),
            end = Offset(size.width, y),
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
        )
    }
}

private fun DrawScope.drawRecordBar(
    record: DayRecord,
    xOffset: Float,
    barWidth: Float,
    canvasHeight: Float,
    textMeasurer: TextMeasurer,
    barColor: Color
) {
    val calendar = Calendar.getInstance().apply { time = record.date }
    val hour = calendar.get(Calendar.HOUR_OF_DAY)
    val minute = calendar.get(Calendar.MINUTE)

    val startTimeInMinutes = hour * 60 + minute
    val minLimit = MIN_HOUR * 60
    val maxLimit = MAX_HOUR * 60
    val clampedStartTime = startTimeInMinutes.coerceIn(minLimit, maxLimit)

    val ratio = (clampedStartTime - minLimit) / TOTAL_MINUTES_RANGE
    val barBottomY = canvasHeight - (ratio * canvasHeight)
    val heightPerMinute = canvasHeight / TOTAL_MINUTES_RANGE
    val barHeight = record.minute * heightPerMinute

    drawRoundRect(
        color = barColor,
        topLeft = Offset(xOffset, barBottomY - barHeight),
        size = Size(barWidth, barHeight),
        cornerRadius = CornerRadius(4.dp.toPx())
    )

    val dateFormat = SimpleDateFormat("yy/MM/dd\nhh:mm", Locale.KOREA)
    val dateText = dateFormat.format(record.date)

    val textLayoutResult = textMeasurer.measure(
        text = dateText,
        style = TextStyle(fontSize = 12.sp, textAlign = TextAlign.Center)
    )

    drawText(
        textLayoutResult = textLayoutResult,
        topLeft = Offset(
            xOffset + (barWidth - textLayoutResult.size.width) / 2,
            canvasHeight + 5.dp.toPx() // 텍스트를 그래프 바닥선 아래로 배치
        )
    )
}