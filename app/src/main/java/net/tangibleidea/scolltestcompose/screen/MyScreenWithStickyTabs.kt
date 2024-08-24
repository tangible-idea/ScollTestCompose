package net.tangibleidea.scolltestcompose.screen
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.unit.dp
import net.tangibleidea.scolltestcompose.tab.CustomScrollableTabRow


@Composable
fun MyScreenWithStickyTabs() {
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()

    // 탭의 Y 위치를 저장할 변수
    val tabOffsetY = remember { mutableFloatStateOf(0f) }

    // 섹션의 Y 위치를 저장할 리스트
    val sectionOffsets = remember { mutableStateListOf<Float>() }

    /** 선택된 1차 탭 인덱스 */
    var selectedTabState by remember { mutableIntStateOf(0) }

    val tabs = listOf("전체계약", "대출", "보장", "자산")

    // 스크롤 상태에 따른 탭 선택 상태 업데이트
    LaunchedEffect(key1 = scrollState.value) {

        if (sectionOffsets.isNotEmpty()) {
            val currentScrollY = scrollState.value.toFloat()
            //Log.d("DEBUG", "Current Scroll Y: $currentScrollY")

            for (i in 0 until sectionOffsets.size - 1) {
                val sectionStart = sectionOffsets[i]
                val sectionEnd = sectionOffsets[i + 1]
                //Log.d("DEBUG", "Section $i: Start = $sectionStart, End = $sectionEnd")

                // 조건 수정: sectionStart <= currentScrollY < sectionEnd
                if (currentScrollY >= sectionStart && currentScrollY < sectionEnd) {
                    selectedTabState = i
                    Log.d("DEBUG", "Selected Tab State: $selectedTabState")
                    break
                }
            }

            // 마지막 섹션에 대한 처리
            if (currentScrollY >= sectionOffsets.last()) {
                selectedTabState = sectionOffsets.size - 1
                Log.d("DEBUG", "Selected Tab State (Last Section): $selectedTabState")
            }
        } else {
            Log.d("DEBUG", "Section Offsets are Empty")
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .verticalScroll(scrollState)
        ) {
            // 첫 번째 섹션
            SectionContent("탭 1의 내용", Color.Green.copy(alpha = 0.4f), sectionOffsets)

            // 탭 영역 (StickyHeader처럼 동작)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .onGloballyPositioned { coordinates ->
                        tabOffsetY.value = coordinates.positionInParent().y
                    }
            ) {
//                TabNew(selectedTabState, coroutineScope, scrollState, sectionOffsets) { index ->
//                    selectedTabState = index
//                }

                CustomScrollableTabRow(
                    tabs = tabs,
                    selectedTabIndex = selectedTabState,
                ) { tabIndex ->
                    selectedTabState = tabIndex
                }
            }

            // 두 번째 섹션
            SectionContent("탭 2의 내용", Color.Green.copy(alpha = 0.3f), sectionOffsets)
            SectionContent("탭 3의 내용", Color.Green.copy(alpha = 0.2f), sectionOffsets)
            SectionContent("탭 4의 내용", Color.Green.copy(alpha = 0.1f), sectionOffsets)
        }

        // 스크롤 위치에 따라 탭을 화면 상단에 고정
        if (scrollState.value > tabOffsetY.value) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.LightGray)
                    .align(Alignment.TopCenter)
            ) {
//                TabNew(selectedTabState, coroutineScope, scrollState, sectionOffsets) { index ->
//                    selectedTabState = index
//                }
                CustomScrollableTabRow(
                    tabs = tabs,
                    selectedTabIndex = selectedTabState,
                ) { tabIndex ->
                    selectedTabState = tabIndex
                }
            }
        }
    }
}


@Composable
fun SectionContent(title: String, color: Color, sectionOffsets: SnapshotStateList<Float>) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(if (title == "탭 1의 내용") 200.dp else 500.dp)
            .background(color)
            .onGloballyPositioned { coordinates ->
                // 섹션의 Y 위치 저장
                if (sectionOffsets.size < 4) {
                    sectionOffsets.add(coordinates.positionInParent().y)
                }
            },
        contentAlignment = Alignment.Center
    ) {
        Text(text = title, color = Color.White, style = MaterialTheme.typography.titleLarge)
    }
}