package com.codelab.basicstatecodelab

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun StatelessCounter(count: Int, onIncrement: () -> Unit, modifier: Modifier = Modifier) {
    Column(modifier = modifier.padding(16.dp)) {
        if (count > 0) {
            Text("You've had $count glasses.")
        }
        Button(onClick = onIncrement, Modifier.padding(top = 8.dp), enabled = count < 10) {
            Text("Add one")
        }
    }
}

@Composable
fun StatefulCounter(modifier: Modifier = Modifier) {
    var count by rememberSaveable { mutableStateOf(0) }
    StatelessCounter(count, { count++ }, modifier)
}

// 1. 여러 컴포저블이 값을 쓴다면 공통 부모에
// 2. 값을 바꾸는 주체가 있다면 그 위에
// 3. 같이 바뀌는 값은 같이 관리
@Composable
fun StatefulCounter2() {
    var waterCount by remember { mutableStateOf(0) }
    var juiceCount by remember { mutableStateOf(0) }

    StatelessCounter(waterCount, { waterCount++ })
    StatelessCounter(juiceCount, { juiceCount++ })
}
// 사용자가 탭하여 juiceCount를 늘리면 StatefulCounter가 재구성되고
// juiceCount를 읽는 StatelessCounter도 재구성됩니다.
// 하지만 waterCount를 읽는 StatelessCounter는 재구성되지 않습니다.


@Composable
fun StatefulCounter3() {
    var count by remember { mutableStateOf(0) }

    StatelessCounter(count, { count++ })
    //AnotherStatelessMethod(count, { count *= 2 })
}
// 이 경우 개수가 StatelessCounter 또는 AnotherStatelessMethod에 의해 업데이트되면 예상대로 모든 항목이 재구성됩니다.
// 끌어올린 상태는 공유할 수 있으므로 불필요한 리컴포지션을 방지하고 재사용성을 높이려면 컴포저블에 필요한 상태만 전달해야 합니다.
// 컴포저블 디자인 권장사항은 필요한 매개변수만 전달하는 것입니다.
