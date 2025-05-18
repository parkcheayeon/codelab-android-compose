package com.codelab.basicstatecodelab

import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel

// ViewModel은 컴포지션의 일부가 아닙니다.
// 따라서 메모리 누수가 발생할 수 있으므로 컴포저블에서 만든 상태(예: 기억된 값)를 보유해서는 안 됩니다.
class WellnessViewModel : ViewModel() {
    private val _tasks = getWellnessTasks().toMutableStateList()
    // 내부 _tasks 변수를 정의하고 tasks를 목록으로 노출하여 ViewModel 외부에서 수정할 수 없도록 합니다.
    val tasks: List<WellnessTask>
        get() = _tasks

    fun remove(item: WellnessTask) {
        _tasks.remove(item)
    }
}

private fun getWellnessTasks() = List(30) { i -> WellnessTask(i, "Task # $i") }
