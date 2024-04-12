package com.example.android.bmicalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.core.view.ViewCompat
import com.example.android.bmicalculator.ui.theme.BMICalculatorTheme
import java.math.RoundingMode
import java.text.DecimalFormat
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BMICalculatorTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    BMI()
                }
            }
        }
    }
}

@Composable
fun BMI() {
    var pokaz by rememberSaveable { mutableStateOf(false) }
    var weight_input by rememberSaveable {mutableStateOf("")}
    val weight = weight_input.toDoubleOrNull() ?: 0.0
    var height_input by rememberSaveable {mutableStateOf("")}
    val height = height_input.toDoubleOrNull() ?: 0.0
    val BMI_result = BMI_calculate(weight,height)
    val focusManager = LocalFocusManager.current
    var przedzialy:String = ""
    if(pokaz) {
        przedzialy ="poniżej 16 - wygłodzenie.\n" +
                "16 - 16.99 - wychudzenie.\n" +
                "17 - 18.49 - niedowagę\n" +
                "18.5 - 24.99 - wagę prawidłową\n" +
                "25.0 - 29.9 - nadwagę\n" +
                "30.0 - 34.99 - I stopień otyłości.\n" +
                "35.0 - 39.99 - II stopień otyłości.\n" +
                "powyżej 40.0 - otyłość skrajną\n"
    }
    else{
        przedzialy = ""
    }


    Column(modifier = Modifier.padding(32.dp),verticalArrangement = Arrangement.spacedBy(4.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            "BMI",
            fontSize = 70.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
           "Calculator",
            fontSize = 30.sp
        )
        Spacer(Modifier.height(16.dp))
        EditWeight(
            value_1 = weight_input,
            value_2 = height_input,
            onValueChange_1 = { weight_input = it },
            onValueChange_2 = { height_input = it },
            keyboardOptions_next = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            keyboardActions_next = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            ),
            keyboardOptions_done = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            keyboardActions_done = KeyboardActions(
                onDone = { focusManager.clearFocus() }
            ),
        )
        BMI_compartments(pokaz = pokaz, onpokazChanged = {pokaz=it})
        Spacer(Modifier.height(16.dp))
        Text(
            text= stringResource(R.string.Your_BMI_is,BMI_result),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text= przedzialy,
            fontSize = 20.sp,

        )
    }
}
@Composable
fun EditWeight(
               value_1: String,
               value_2: String,
               onValueChange_1: (String) -> Unit,
               onValueChange_2: (String) -> Unit,
               keyboardOptions_next: KeyboardOptions,
               keyboardActions_next: KeyboardActions,
               keyboardOptions_done: KeyboardOptions,
               keyboardActions_done: KeyboardActions
) {
    TextField(
        value = value_1,
        onValueChange = onValueChange_1,
        label = { Text("Waga w kg") },
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = keyboardOptions_next,
        keyboardActions = keyboardActions_next,
        singleLine = true
    )
    TextField(
        value = value_2,
        onValueChange = onValueChange_2,
        label = { Text("Wzrost w centymetrach") },
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = keyboardOptions_done,
        keyboardActions = keyboardActions_done,
        singleLine = true
    )
}

@Composable
fun BMI_compartments(pokaz: Boolean,
                     onpokazChanged: (Boolean) -> Unit,
                     modifier: Modifier = Modifier){
    Row(
        modifier = modifier
            .fillMaxWidth()
            .size(48.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Pokaz przedzialy BMI"
        )
        Switch(
            modifier = modifier
                .fillMaxWidth()
                .wrapContentWidth(Alignment.End),
            checked = pokaz,
            onCheckedChange = onpokazChanged,

        )
    }

}


private fun BMI_calculate(weight:Double,height:Double):String{

    var result:Double = 0.0
    var height_m = height/100 //zamiana z centymetrow na metry
    result += weight
    val df = DecimalFormat("#.##")
    df.roundingMode = RoundingMode.DOWN
    result = result /(height_m*height_m)
    val result_end = df.format(result)

    return result_end

}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    BMICalculatorTheme {
        BMI()
    }
}