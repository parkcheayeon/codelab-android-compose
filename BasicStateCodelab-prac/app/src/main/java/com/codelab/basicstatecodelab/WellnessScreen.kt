package com.codelab.basicstatecodelab

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier

@Composable
fun WellnessScreen(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        StatefulCounter()

        // 긴 직렬화 또는 역직렬화가 필요한 복잡한 데이터 구조나 대량의 데이터를 저장하는 데 rememberSaveable을 사용해서는 안 됩니다.
        val list = remember { getWellnessTasks().toMutableStateList() }
        // 목록 생성
        // 확장 함수 toMutableStateList()를 사용하면 변경 가능하거나 변경 불가능한
        // 초기 Collection(예: List)에서 관찰 가능한 MutableList를 만들 수 있습니다.
        // mutableStateListOf를 사용하여 관찰 가능한 MutableList를 만들고 초기 상태의 요소를 추가할 수도 있습니다.
        WellnessTasksList(list = list, onCloseTask = { task -> list.remove(task) })
    }
}

private fun getWellnessTasks() = List(30) { i -> WellnessTask(i, "Task # $i") }
