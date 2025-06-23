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
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.compose.rally.ui.accounts.AccountsScreen
import com.example.compose.rally.ui.accounts.SingleAccountScreen
import com.example.compose.rally.ui.bills.BillsScreen
import com.example.compose.rally.ui.components.RallyTabRow
import com.example.compose.rally.ui.overview.OverviewScreen
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
            // navController를 전달하여 이 NavHost에 연결
            NavHost(
                navController = navController,
                startDestination = Overview.route,
                modifier = Modifier.padding(innerPadding)
            ) {
                // Overview를 composable 확장 함수를 통해 추가하고 고유 문자열 route를 설정
                // 탐색 그래프에 대상이 추가되므로 이 대상으로 이동하면 표시될 UI도 정의해야함
                composable(route = Overview.route) {
                    OverviewScreen(
                        onClickSeeAllAccounts = {
                            navController.navigateSingleTopTo(Accounts.route)
                        },
                        onClickSeeAllBills = {
                            navController.navigateSingleTopTo(Bills.route)
                        }
                    )
                }
                composable(route = Accounts.route) {
                    AccountsScreen()
                }
                composable(route = Bills.route) {
                    BillsScreen()
                }
                composable(
                    route = "${SingleAccount.route}/{${SingleAccount.accountTypeArg}}",
                    // 탐색 시에 인수를 경로와 함께 전달하려면 "route/{argument}" 패턴에 따라 경로에 인수를 추가해야 한다

                    //arguments = listOf(
                    //    navArgument(SingleAccount.accountTypeArg) { type = NavType.StringType }
                    //)
                    // 이 composable이 인수를 받아야 한다는 사실을 알려줘야 한다. 그렇게 하려면 arguments 매개변수를 정의한다.
                    // composable 함수는 기본적으로 인수 목록을 받기 때문에 인수는 원하는 개수만큼 정의할 수 있다.
                    // 여기서는 accountTypeArg라는 단일 인수를 추가하고 안전하게 String 유형으로 지정하면 된다.
                    // 유형을 명시적으로 설정하지 않으면 인수의 기본값에서 유형이 추론된다.
                    arguments = SingleAccount.arguments
                ) {
                    SingleAccountScreen()
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
