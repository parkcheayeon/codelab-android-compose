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
                        },
                        onAccountClick = { accountType ->
                            navController.navigateToSingleAccount(accountType)
                        }
                    )
                }
                composable(route = Accounts.route) {
                    AccountsScreen(
                        onAccountClick = { accountType ->
                            navController.navigateToSingleAccount(accountType)
                        }
                    )
                }
                composable(route = Bills.route) {
                    BillsScreen()
                }
                composable(
                    route = SingleAccount.routeWithArgs,
                    // 경로에 파라미터를 포함
                    arguments = SingleAccount.arguments
                    // composable은 arguments를 통해 인수를 처리하도록 함
                ) { navBackStackEntry ->
                    val accountType = navBackStackEntry.arguments?.getString(SingleAccount.accountTypeArg)
                    SingleAccountScreen(accountType)
                    // 전달된 인수 값 가져오기
                    // 백 스택에 있는 항목의 현재 경로 및 전달된 인수에 관한 정보를 저장하는 클래스인 NavBackStackEntry에 액세스할 수 있다.
                    // navBackStackEntry에서 arguments 목록을 가져온 뒤 필요한 인수를 검색하고 가져와서 컴포저블 화면으로 전달할 수 있다.
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

private fun NavHostController.navigateToSingleAccount(accountType: String) {
    this.navigateSingleTopTo("${SingleAccount.route}/$accountType")
}
