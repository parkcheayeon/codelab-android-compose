package com.example.compose.rally

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.compose.rally.ui.accounts.AccountsScreen
import com.example.compose.rally.ui.accounts.SingleAccountScreen
import com.example.compose.rally.ui.bills.BillsScreen
import com.example.compose.rally.ui.overview.OverviewScreen

@Composable
fun RallyNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Overview.route,
        modifier = modifier
    ) { // builder: NavGraphBuilder.() -> Unit
        // navigation graph 정의 및 빌드
        composable(route = Overview.route) {
            OverviewScreen(
                // callback 만 전달
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
            // 경로에 인수를 추가해야 함
            arguments = SingleAccount.arguments,
            // 이 composable이 인수를 받아야 한다는 사실을 알려주는 것
            deepLinks = SingleAccount.deepLinks
        ) { navBackStackEntry ->
            // navBackStackEntry에서 accountTypeArg를 요청한 다음에 이것을 SingleAccountScreen의 accountType 매개변수에 전달해야 한다
            val accountType =
                navBackStackEntry.arguments?.getString(SingleAccount.accountTypeArg)
            SingleAccountScreen(accountType)
            // 어떤 계좌 유형을 표시해야 하는지 알 수 있도록 추가 정보 필요하다.
            // 이때 인수를 사용하여 이러한 종류의 정보를 전달할 수 있다.
            // 경로에 추가로 {account_type} 인수가 필요하다는 것을 지정해야 한다.
        }
    }
}

fun NavHostController.navigateSingleTopTo(route: String) =
    this.navigate(route) { launchSingleTop = true }

private fun NavHostController.navigateToSingleAccount(accountType: String) {
    this.navigateSingleTopTo("${SingleAccount.route}/$accountType")
}
