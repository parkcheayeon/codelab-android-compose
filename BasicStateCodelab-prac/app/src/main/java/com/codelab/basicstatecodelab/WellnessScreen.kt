package com.codelab.basicstatecodelab

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.Modifier

@Composable
fun WellnessScreen(
    modifier: Modifier = Modifier,
    wellnessViewModel: WellnessViewModel = viewModel()
    // ViewModel은 탐색 그래프의 활동이나 프래그먼트, 대상에서
    // 호출되는 루트 컴포저블에 가까운 화면 수준 컴포저블에서 사용하는 것이 좋습니다.
    // ViewModel은 다른 컴포저블로 전달하면 안 됩니다.
    // 대신 필요한 데이터와 필수 로직을 실행하는 함수만 매개변수로 전달해야 합니다.
) {
    Column(modifier = modifier) {
        StatefulCounter()

        WellnessTasksList(
            list = wellnessViewModel.tasks,
            onCloseTask = { task -> wellnessViewModel.remove(task) }
        )
        // 상태는 컴포지션 외부에 유지되고 ViewModel에 의해 저장되므로 목록의 변형은 구성이 변경되어도 유지
    }
}
