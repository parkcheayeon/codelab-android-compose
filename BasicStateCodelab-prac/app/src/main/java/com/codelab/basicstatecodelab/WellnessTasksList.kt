package com.codelab.basicstatecodelab

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun WellnessTasksList(
    modifier: Modifier = Modifier,
    list: List<WellnessTask>,
    onCloseTask: (WellnessTask) -> Unit
    // 부모인 WellnessScreen으로 끌어올리기 위해 onCloseTask 추가(삭제할 WellnessTask 수신)
    // onCloseTask를 WellnessTaskItem에 전달
) {
    LazyColumn(
        modifier = modifier
    ) {
        items(
            items = list,
            key = { task -> task.id }
            // 리스트 항목의 순서가 바뀌거나 삭제되면 기억하고 있던 상태를 잃어버릴 수 있어 id를 각 항목의 key로 사용
        ) { task ->
            WellnessTaskItem(taskName = task.label, onClose = { onCloseTask(task) })
        }
    }
}
