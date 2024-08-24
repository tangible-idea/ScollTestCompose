package net.tangibleidea.scolltestcompose.tab

import androidx.compose.foundation.ScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import net.tangibleidea.scolltestcompose.TabMyView

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