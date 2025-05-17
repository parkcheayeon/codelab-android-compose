package com.codelab.basicstatecodelab

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier

@Composable
fun WellnessTasksList(
    modifier: Modifier = Modifier,
    list: List<WellnessTask> = remember { getWellnessTasks() }
) {
    LazyColumn(
        modifier = modifier
    ) {
        items(list) { task ->
            WellnessTaskItem(taskName = task.label)
        }
    }
    // LazyColumn의 구성 가능한 함수 rememberLazyListState는 rememberSaveable을 사용하여 목록의 초기 상태를 만듭니다.
    // 활동이 다시 생성되면 스크롤 상태는 아무런 코딩을 하지 않아도 유지됩니다.
    // LazyColumn 또는 LazyRow와 같은 지연 구성요소는 LazyListState를 끌어올려 이 사용 사례를 지원합니다.
}

// 가짜 데이터 생성
fun getWellnessTasks() = List(30) { i -> WellnessTask(i, "Task # $i") }
