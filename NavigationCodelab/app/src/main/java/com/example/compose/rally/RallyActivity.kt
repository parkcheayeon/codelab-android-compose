/*
 * Copyright 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.compose.rally

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.compose.rally.ui.components.RallyTabRow
import com.example.compose.rally.ui.theme.RallyTheme

/**
 * This Activity recreates part of the Rally Material Study from
 * https://material.io/design/material-studies/rally.html
 */
class RallyActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RallyApp()
        }
    }
}

@Composable
fun RallyApp() {
    RallyTheme {
        val navController = rememberNavController()
        // NavController 가져오기

        val currentBackStack by navController.currentBackStackEntryAsState()
        // 백 스택에서 현재 대상의 실시간 업데이트를 State의 형식으로 받아볼 수 있다.
        val currentDestination = currentBackStack?.destination
        // currentBackStack?.destination은 NavDestination.을 반환한다.
        // currentScreen을 다시 올바르게 업데이트하려면 반환된 NavDestination을 Rally의 3가지 기본 화면 컴포저블에 매칭할 방법이 있어야 한다.
        // 현재 어느 컴포저블이 표시되어 있는지 확인하여 이 정보를 RallyTabRow.에 전달해야 한다.
        val currentScreen = rallyTabRowScreens.find { it.route == currentDestination?.route } ?: Accounts
        // 각 대상에는 고유 경로가 있으므로 이 문자열 경로를 일종의 ID로 사용하여 비교해 보고 고유한 일치 항목을 찾을 수 있다.
        // currentScreen을 업데이트하려면 rallyTabRowScreens 목록을 순환하여 일치하는 경로를 찾은 다음 그 RallyDestination을 반환해야 한다.

        Scaffold(
            topBar = {
                RallyTabRow(
                    allScreens = rallyTabRowScreens,
                    onTabSelected = { newScreen ->
                        navController.navigateSingleTopTo(newScreen.route)
                    },
                    // 탭을 탭하면 특정 대상으로 이동하기를 원하므로 어느 탭 아이콘이 선택되었는지 알려준다
                    currentScreen = currentScreen
                )
            }
        ) { innerPadding ->
            RallyNavHost(
                navController = navController,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}
