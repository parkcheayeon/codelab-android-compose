package com.codelab.basicstatecodelab

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

// Compose는 상태 value 속성을 읽는 각 컴포저블을 추적하고 그 value가 변경되면 리컴포지션을 트리거합니다.
// mutableStateOf 함수를 사용하여 관찰 가능한 MutableState를 만들 수 있습니다.
// 이 함수는 초깃값을 State 객체에 래핑된 매개변수로 수신한 다음, value의 값을 관찰 가능한 상태로 만듭니다.
@Composable
fun WaterCounter(modifier: Modifier = Modifier) {
    Column(modifier = modifier.padding(16.dp)) {
        val count: MutableState<Int> = mutableStateOf(0)
        // 리컴포지션 예약은 잘 작동합니다. 그러나 리컴포지션이 발생하면 count 변수가 다시 0으로 초기화되므로
        // 리컴포지션 간에 이 값을 유지할 방법이 필요합니다.
        Text(text = "You've had $count glasses.")
        Button(onClick = { count.value++ }, Modifier.padding(top = 8.dp)) {
            Text("Add one")
        }
    }
}
