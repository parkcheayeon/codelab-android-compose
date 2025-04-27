package com.chaeny.basicscodelab

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
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
fun MyApp(modifier: Modifier = Modifier) {
    // rememberSaveable- 구성 변경(예: 회전)과 프로세스 중단에도 각 상태를 저장합니다.
    // 다크모드로 변경해도 온보딩 화면이 표시되지 않음
    var shouldShowOnboarding by rememberSaveable { mutableStateOf(true) }
    // 매번 .value를 입력할 필요가 없도록 해주는 속성 위임
    // OnboardingScreen에서 만든 상태를 MyApp 컴포저블과 공유해야 합니다.
    // 상태 값을 상위 요소와 공유하는 대신 상태를 호이스팅합니다.
    // 즉, 상태 값에 액세스해야 하는 공통 상위 요소로 상태 값을 이동하기만 하면 됩니다.

    Surface(modifier) {
        if (shouldShowOnboarding) {
            OnboardingScreen(onContinueClicked = { number ->
                Log.d("MyApp", "onContinueClicked 호출, number = $number")
                shouldShowOnboarding = false
            })
        } else {
            Greetings()
        }
    }
}

@Composable
fun OnboardingScreen(
    // OnboardingScreen() 안에는 상태가 없다 버튼이 눌리면 onContinueClicked() 호출만 한다.
    // shouldShowOnboarding는 MyApp에만 있다.
    onContinueClicked: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Welcome to the Basics Codelab!")
        Button(
            modifier = Modifier.padding(vertical = 24.dp),
            onClick = { onContinueClicked(10) }
        ) {
            Text("Continue")
        }
    }
}

@Composable
private fun Greetings(
    modifier: Modifier = Modifier,
    names: List<String> = List(1000) { "$it" }
) {
    // LazyColumn은 RecyclerView와 같은 하위 요소를 재활용하지 않습니다.
    // 컴포저블을 방출하는 것은 Android Views를 인스턴스화하는 것보다 상대적으로 비용이 적게 들므로
    // LazyColumn은 스크롤 할 때 새 컴포저블을 방출하고 계속 성능을 유지합니다.
    LazyColumn(modifier = modifier.padding(vertical = 4.dp)) {
        items(items = names) { name ->
            Greeting(name = name)
        }
    }
}

@Preview(showBackground = true, widthDp = 320, heightDp = 320)
@Composable
fun OnboardingPreview() {
    BasicsCodelabTheme {
        OnboardingScreen(onContinueClicked = {}) // Do nothing on click.
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    // State 및 MutableState는 어떤 값을 보유하고 그 값이 변경될 때마다 UI 업데이트(리컴포지션)를 트리거하는 인터페이스입니다.
    // 하지만, 컴포저블 내의 변수에 mutableStateOf를 할당하기만 할 수는 없습니다.
    // 앞에서 설명한 것처럼 false 값을 가진 변경 가능한 새 상태로 상태를 재설정하여 컴포저블을 다시 호출하는 때는 언제든지 리컴포지션이 일어날 수 있습니다.
    // 여러 리컴포지션 간에 상태를 유지하려면 remember를 사용하여 변경 가능한 상태를 기억해야 합니다.
    // remember는 리컴포지션을 방지하는 데 사용되므로 상태가 재설정되지 않습니다.
    Log.d("Greeting", "recomposition : $name")
    // rememberSaveable - 다크모드로 변경해도 expanded 값이 유지됨
    var expanded by rememberSaveable {
        Log.d("Greeting", "rememberSaveable 호출 : $name")
        mutableStateOf(false)
    }
    var expanded2 by remember { mutableStateOf(false) }
    Log.d("Greeting", "rememberSaveable 사용 변수 : $expanded")
    Log.d("Greeting", "remember 사용 변수 : $expanded2")

    // 간단한 계산을 실행하므로 리컴포지션에 대비하여 이 값을 기억할 필요가 없습니다
    val extraPadding = if (expanded) 48.dp else 0.dp
    Surface(
        color = MaterialTheme.colorScheme.primary,
        modifier = modifier.padding(vertical = 4.dp, horizontal = 8.dp)
    ) {
        Row(modifier = Modifier.padding(24.dp)) {
            // alignEnd 수정자가 없으므로 시작 시 컴포저블에 약간의 weight을 제공합니다.
            // weight 수정자는 요소를 유연하게 만들기 위해 가중치가 없는 다른 요소를
            // 효과적으로 밀어내어 요소의 사용 가능한 모든 공간을 채웁니다.
            Column(modifier = Modifier
                .weight(1f)
                .padding(bottom = extraPadding)
            ) {
                Text(text = "Hello ")
                Text(text = name)
            }
            // 상태를 변경하기 위해 Button이 onClick이라는 매개변수를 사용한다고 알고 있을 수도 있지만,
            // 값을 사용하지 않고 함수를 사용합니다.
            // First-class citizen - 함수도 숫자나 문자처럼 자유롭게 변수에 저장하거나 넘길 수 있다
            ElevatedButton(
                onClick = {
                    expanded = !expanded
                    expanded2 = !expanded2
                }
            ) {
                Text(if (expanded) "Show less" else "Show more")
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 320)
@Composable
fun GreetingsPreview() {
    BasicsCodelabTheme {
        Greetings()
    }
}

@Preview
@Composable
fun MyAppPreview() {
    BasicsCodelabTheme {
        MyApp(Modifier.fillMaxSize())
    }
}
