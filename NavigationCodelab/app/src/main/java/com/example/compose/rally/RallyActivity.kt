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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
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
        var currentScreen: RallyDestination by remember { mutableStateOf(Overview) }
        val navController = rememberNavController()
        // NavController 가져오기
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
            // navController를 전달하여 이 NavHost에 연결
            NavHost(
                navController = navController,
                startDestination = Overview.route,
                modifier = Modifier.padding(innerPadding)
            ) {
                // Overview를 composable 확장 함수를 통해 추가하고 고유 문자열 route를 설정
                // 탐색 그래프에 대상이 추가되므로 이 대상으로 이동하면 표시될 UI도 정의해야함
                composable(route = Overview.route) {
                    Overview.screen()
                }
                composable(route = Accounts.route) {
                    Accounts.screen()
                }
                composable(route = Bills.route) {
                    Bills.screen()
                }
            }
        }
    }
}

fun NavHostController.navigateSingleTopTo(route: String) = this.navigate(route) {
    popUpTo(
        this@navigateSingleTopTo.graph.findStartDestination().id
    ) {
        saveState = true
    }
    // 새로 탭을 눌렀을 때 이전 화면 스택들을 정리하고, 탭의 첫 화면만 남기는데 상태는 저장한다.
    launchSingleTop = true
    // 백 스택 위에 대상 탭이 최대 1개만 있도록
    // 동일한 탭을 여러 번 탭해도 동일한 대상의 사본이 여러 개 실행되지 않는다.
    // 새로운 인스턴스를 생성하지 않고 기존 것을 재활용한다.
    restoreState = true
    // 이전에 PopUpToBuilder.saveState 또는 popUpToSaveState 속성에 의한 저장된 상태 복원 여부 결정
}
