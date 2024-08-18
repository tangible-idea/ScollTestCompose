package net.tangibleidea.scolltestcompose

import android.annotation.SuppressLint
import android.view.LayoutInflater
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.material.tabs.TabLayout

@SuppressLint("InflateParams")
@Composable
fun TabMyView(selectedTab: Int, onTabChanged: (Int) -> Unit) {
    //var selectedTab by remember { mutableStateOf(0) }

    // Adds view to Compose
    AndroidView(
        modifier = Modifier.fillMaxWidth(), // Occupy the max size in the Compose UI tree
        factory = { context ->
            val view = LayoutInflater.from(context).inflate(R.layout.main_tab, null, false)
            val tabMainItem= view.findViewById<TabLayout>(R.id.tabMainMy)
            tabMainItem.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab) {
                    onTabChanged(tabMainItem.selectedTabPosition)
                    //Log.d("ViewTabMy::onTabSelected", "$selectedItem")
                }

                override fun onTabUnselected(tab: TabLayout.Tab) {
                }

                override fun onTabReselected(tab: TabLayout.Tab) {
                }
            })
            tabMainItem // return the view
        },
        update = { tabLayout ->
            // Compose 상태를 반영하여 선택된 탭 업데이트
            if (tabLayout.selectedTabPosition != selectedTab) {
                tabLayout.getTabAt(selectedTab)?.select()
            }
        },
        onReset = { view ->

        }
    )
}


@Composable
fun ContentExample() {
    Column(Modifier.fillMaxSize()) {
        Text("Look at this CustomView!")
        //ViewTabMy()
    }
}
