package com.codelab.basicstatecodelab

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun WaterCounter(modifier: Modifier = Modifier) {
    Column(modifier = modifier.padding(16.dp)) {
        var count by remember { mutableStateOf(0) }
        if (count > 0) {
            var showTask by remember { mutableStateOf(true) }
            if (showTask) {
                WellnessTaskItem(
                    onClose = { showTask = false },
                    taskName = "Have you taken your 15 minute walk today?"
                )
            }
            Text("You've had $count glasses.")
        }
        Row(Modifier.padding(top = 8.dp)) {
            Button(onClick = { count++ }, enabled = count < 10) {
                Text("Add one")
            }
            Button(
                onClick = { count = 0 }, Modifier.padding(start = 8.dp)
            ) { Text("Clear water count") }
            // Clear water count 버튼을 눌러 count를 0으로 재설정하면 리컴포지션이 발생합니다.
            // count를 표시하는 Text와 WellnessTaskItem과 관련된 모든 코드가 호출되지 않고 컴포지션을 종료합니다.
            // remember showTask가 호출되는 코드 위치가 호출되지 않았으므로 showTask가 삭제되었습니다
            // Add one 버튼을 눌러 count를 0보다 크게 만듭니다(리컴포지션).
            // WellnessTaskItem 컴포저블이 다시 표시됩니다. 위의 컴포지션을 종료할 때 showTask의 이전 값이 삭제되었기 때문입니다.
        }
    }
}
