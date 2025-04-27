package com.chaeny.basicscodelab

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.chaeny.basicscodelab.ui.theme.BasicsCodelabTheme

class MainActivity : ComponentActivity() {
    // AppCompatActivity()사용 시 AppCompat 테마를 사용하라는 Exception 발생
    // java.lang.IllegalStateException:
    // You need to use a Theme.AppCompat theme (or descendant) with this activity.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BasicsCodelabTheme {
                MyApp(modifier = Modifier.fillMaxSize())
            }
        }
    }
}

// 재사용할 수 있는 작은 구성요소를 만들면 앱에서 사용하는 UI 요소의 라이브러리를 쉽게 만들 수 있습니다.
// 각 요소는 화면의 작은 부분을 담당하며 독립적으로 수정할 수 있습니다.
// 함수는 기본적으로 빈 수정자가 할당되는 modifier를 포함하는 것이 좋습니다.
// 이렇게 하면 호출 사이트가 구성 가능한 함수 외부에서 레이아웃 안내와 동작을 조정할 수 있습니다
@Composable
fun MyApp(
    modifier: Modifier = Modifier,
    names: List<String> = listOf("World", "Compose")
) {
    Column(modifier = modifier.padding(vertical = 4.dp)) {
        for (name in names) {
            Greeting(name = name)
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    // State 및 MutableState는 어떤 값을 보유하고 그 값이 변경될 때마다 UI 업데이트(리컴포지션)를 트리거하는 인터페이스입니다.
    // 하지만, 컴포저블 내의 변수에 mutableStateOf를 할당하기만 할 수는 없습니다.
    // 앞에서 설명한 것처럼 false 값을 가진 변경 가능한 새 상태로 상태를 재설정하여 컴포저블을 다시 호출하는 때는 언제든지 리컴포지션이 일어날 수 있습니다.
    // 여러 리컴포지션 간에 상태를 유지하려면 remember를 사용하여 변경 가능한 상태를 기억해야 합니다.
    // remember는 리컴포지션을 방지하는 데 사용되므로 상태가 재설정되지 않습니다.
    val expanded = remember { mutableStateOf(false) }
    Surface(
        color = MaterialTheme.colorScheme.primary,
        modifier = modifier.padding(vertical = 4.dp, horizontal = 8.dp)
    ) {
        Row(modifier = Modifier.padding(24.dp)) {
            // alignEnd 수정자가 없으므로 시작 시 컴포저블에 약간의 weight을 제공합니다.
            // weight 수정자는 요소를 유연하게 만들기 위해 가중치가 없는 다른 요소를
            // 효과적으로 밀어내어 요소의 사용 가능한 모든 공간을 채웁니다.
            Column(modifier = Modifier.weight(1f)) {
                Text(text = "Hello ")
                Text(text = name)
            }
            // 상태를 변경하기 위해 Button이 onClick이라는 매개변수를 사용한다고 알고 있을 수도 있지만,
            // 값을 사용하지 않고 함수를 사용합니다.
            // First-class citizen - 함수도 숫자나 문자처럼 자유롭게 변수에 저장하거나 넘길 수 있다
            ElevatedButton(
                onClick = { expanded.value = !expanded.value }
            ) {
                Text(if (expanded.value) "Show less" else "Show more")
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 320)
@Composable
fun GreetingPreview() {
    BasicsCodelabTheme {
        MyApp()
    }
}
