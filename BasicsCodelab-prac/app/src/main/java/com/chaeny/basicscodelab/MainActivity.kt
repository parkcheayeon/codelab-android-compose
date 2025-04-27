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
    Surface(
        color = MaterialTheme.colorScheme.primary,
        modifier = modifier.padding(vertical = 4.dp, horizontal = 8.dp)
    ) {
        Row(modifier = Modifier.padding(24.dp)) {
            //  alignEnd 수정자가 없으므로 시작 시 컴포저블에 약간의 weight을 제공합니다.
            //  weight 수정자는 요소를 유연하게 만들기 위해 가중치가 없는 다른 요소를
            //  효과적으로 밀어내어 요소의 사용 가능한 모든 공간을 채웁니다.
            Column(modifier = Modifier.weight(1f)) {
                Text(text = "Hello ")
                Text(text = name)
            }
            ElevatedButton(
                onClick = { /* TODO */ }
            ) {
                Text("Show more")
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
