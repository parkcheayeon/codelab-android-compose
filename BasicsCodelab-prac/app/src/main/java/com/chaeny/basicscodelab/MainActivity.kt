package com.chaeny.basicscodelab

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons.Filled
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
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

@Composable
private fun Greeting(name: String, modifier: Modifier = Modifier) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        modifier = modifier.padding(vertical = 4.dp, horizontal = 8.dp)
    ) {
        CardContent(name)
    }
}

@Composable
private fun CardContent(name: String) {
    Log.d("Greeting", "recomposition : $name")
    var expanded = rememberSaveable {
        Log.d("Greeting", "remember 호출 : $name")
        false
    }
    Log.d("Greeting", "mutableStateOf 미사용 변수 : $expanded")

    Row(
        modifier = Modifier
            .padding(12.dp)
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
        // extraPadding을 삭제하고 대신 animateContentSize 수정자를 Row에 적용합니다.
        // 그러면 애니메이션 생성 프로세스가 자동화되고 이를 수동으로 실행하기가 어려워집니다.
        // 또한 coerceAtLeast가 더 이상 필요하지 않습니다.
        // animateDpAsState - 특정 값(ex. padding) 하나를 바꾸는 것
        // animateContentSize - Row 전체 크기를 바꾸는 것
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(12.dp)
        ) {
            Text(text = "Hello, ")
            Text(
                text = name, style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.ExtraBold
                )
            )
            if (expanded) {
                Text(
                    text = ("Composem ipsum color sit lazy, " +
                            "padding theme elit, sed do bouncy. ").repeat(4),
                )
            }
        }
        IconButton(onClick = { expanded = !expanded }) {
            Icon(
                imageVector = if (expanded) Filled.ExpandLess else Filled.ExpandMore,
                // 아이콘 지정
                contentDescription = if (expanded) {
                    stringResource(R.string.show_less)
                } else {
                    stringResource(R.string.show_more)
                }
                //'더보기' 및 '간략히 보기'를 위한 콘텐츠 설명이 있어야 하며 간단한 if 문을 사용하여 설명을 추가할 수 있습니다
            )
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

@Preview(
    showBackground = true,
    widthDp = 320,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    name = "GreetingPreviewDark"
)
@Preview(showBackground = true, widthDp = 320)
@Composable
fun GreetingPreview() {
    BasicsCodelabTheme {
        Greetings()
    }
}
