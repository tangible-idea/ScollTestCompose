package net.tangibleidea.scolltestcompose

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.unit.dp
import com.afsshlife.ui.activity.compose.widget.toolbar.FlexibleTopBar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import net.tangibleidea.scolltestcompose.ui.theme.ScollTestComposeTheme

class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            // 탑바(툴바) 스크롤 시 사라지게하는 behavior 옵션.
            val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

            ScollTestComposeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()
                            .nestedScroll(scrollBehavior.nestedScrollConnection)
                            .statusBarsPadding(),
                    topBar = {
                        FlexibleTopBar(
                            scrollBehavior = scrollBehavior,
                            content = {
                                Box(Modifier.height(65.dp).background(Color.DarkGray).fillMaxWidth()) {

                                }
                            })
                    }
                ) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        MyScreenWithStickyTabs()
                    }

                }
            }
        }
    }
}
@Composable
fun MyScreenWithStickyTabs() {
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()

    // 탭의 Y 위치를 저장할 변수
    val tabOffsetY = remember { mutableStateOf(0f) }

    // 섹션의 Y 위치를 저장할 리스트
    val sectionOffsets = remember { mutableStateListOf<Float>() }

    /** 선택된 1차 탭 인덱스 */
    var selectedTabState by remember { mutableIntStateOf(0) }

    // 스크롤 상태에 따른 탭 선택 상태 업데이트
    LaunchedEffect(key1 = scrollState.value) {

        if (sectionOffsets.isNotEmpty()) {
            val currentScrollY = scrollState.value.toFloat()
            Log.d("DEBUG", "Current Scroll Y: $currentScrollY")

            for (i in 0 until sectionOffsets.size - 1) {
                val sectionStart = sectionOffsets[i]
                val sectionEnd = sectionOffsets[i + 1]
                Log.d("DEBUG", "Section $i: Start = $sectionStart, End = $sectionEnd")

                if (currentScrollY in sectionStart..sectionEnd) {
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
            SectionContent("탭 1의 내용", Color.Red, sectionOffsets)

            // 탭 영역 (StickyHeader처럼 동작)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .onGloballyPositioned { coordinates ->
                        tabOffsetY.value = coordinates.positionInParent().y
                    }
            ) {
                TabNew(selectedTabState, coroutineScope, scrollState, sectionOffsets) { index ->
                    selectedTabState = index
                }
            }

            // 두 번째 섹션
            SectionContent("탭 2의 내용", Color.Green, sectionOffsets)
            SectionContent("탭 3의 내용", Color.Blue, sectionOffsets)
            SectionContent("탭 4의 내용", Color.Yellow, sectionOffsets)
        }

        // 스크롤 위치에 따라 탭을 화면 상단에 고정
        if (scrollState.value > tabOffsetY.value) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.LightGray)
                    .align(Alignment.TopCenter)
            ) {
                TabNew(selectedTabState, coroutineScope, scrollState, sectionOffsets) { index ->
                    selectedTabState = index
                }
            }
        }
    }
}

@Composable
fun TabNew(
    selectedTabState: Int,
    coroutineScope: CoroutineScope,
    scrollState: ScrollState,
    sectionOffsets: SnapshotStateList<Float>,
    onClickTab : (Int) -> Unit= {}
) {

    TabMyView(selectedTabState) { index->
        onClickTab(index)
        coroutineScope.launch {
            // 해당 섹션 위치로 스크롤
            if (index < sectionOffsets.size) {
                if(!scrollState.isScrollInProgress)
                    scrollState.animateScrollTo(sectionOffsets[index].toInt())
            }
        }

    }
}

@Composable
fun SectionContent(title: String, color: Color, sectionOffsets: SnapshotStateList<Float>) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(if (title == "탭 1의 내용") 300.dp else 1000.dp)
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