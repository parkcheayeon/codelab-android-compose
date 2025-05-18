package com.codelab.basicstatecodelab

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

//data class WellnessTask(
//    val id: Int,
//    val label: String,
//    val checked: MutableState<Boolean> = mutableStateOf(false)
//)
// 추적하도록 지시하지 않는 한 checkedState의 변경사항을 인식하지 못합니다.

class WellnessTask(
    val id: Int,
    val label: String,
    initialChecked: Boolean = false
) {
    var checked by mutableStateOf(initialChecked)
}
// WellnessTask를 데이터 클래스가 아닌 클래스가 되도록 변경합니다.
// WellnessTask가 생성자에서 기본값이 false인 initialChecked 변수를 수신하도록 하면
// mutableStateOf로 checked 변수를 초기화하여 initialChecked를 기본값으로 사용할 수 있습니다.

// 이제 결합된 내부 구성 가능한 함수가 아닌 ViewModel로 비즈니스 로직이 리팩터링되므로 단위 테스트가 훨씬 간단해집니다.
