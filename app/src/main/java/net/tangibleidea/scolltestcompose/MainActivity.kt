package net.tangibleidea.scolltestcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import com.afsshlife.ui.activity.compose.widget.toolbar.FlexibleTopBar
import kotlinx.coroutines.launch
import net.tangibleidea.scolltestcompose.screen.MyScreenWithStickyTabs
import net.tangibleidea.scolltestcompose.ui.theme.ScollTestComposeTheme

class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val scope = rememberCoroutineScope()

            // 탑바(툴바) 스크롤 시 사라지게하는 behavior 옵션.
            val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

//            val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
//
//            // 뒤로 가기 버튼 처리
//            BackHandler(enabled = drawerState.isOpen) {
//                scope.launch {
//                    drawerState.close()
//                }
//            }

            ScollTestComposeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()
                    .nestedScroll(scrollBehavior.nestedScrollConnection)
                    .statusBarsPadding(),
                    topBar = {
                        FlexibleTopBar(
                            scrollBehavior = scrollBehavior,
                            content = {
                                Box(
                                    Modifier.height(65.dp).background(Color.LightGray)
                                        .fillMaxWidth()
                                ) {
                                    IconButton(onClick = {
                                        scope.launch {
                                            //drawerState.open()
                                        }
                                    }) {
                                        Icon(Icons.Filled.Menu, contentDescription = "메뉴 열기")
                                    }
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

