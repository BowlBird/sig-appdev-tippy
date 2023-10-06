package com.github.bowlbird.tippy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.viewModelFactory
import com.github.bowlbird.tippy.ui.theme.TippyTheme
import java.lang.Math.round
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TippyTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Layout(Modifier)
                }
            }
        }
    }
}

@Composable
fun Layout(modifier: Modifier = Modifier) = Box(modifier = modifier, contentAlignment = Alignment.Center) {
    val viewModel: TipViewModel = viewModel()
    val tipUiState = viewModel.tipAmount.collectAsState()

    Column(modifier = Modifier.width(300.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        TipInput()
        Percentage()
        Text("%.0f%%".format(tipUiState.value.percentage))
        Amount()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TipInput(modifier: Modifier = Modifier) {
    val viewModel: TipViewModel = viewModel()
    val tipUiState = viewModel.tipAmount.collectAsState()
    TextField(
        modifier = modifier,
        value = tipUiState.value.tipAmount.toString(),
        onValueChange = {
            if (it.toDoubleOrNull() != null) {
                //no more than two decimal places
                val splitDouble = it.split(".")

                when (splitDouble.size) {
                    1 -> {
                        viewModel.updateTipState(
                            tipUiState.value.copy(
                                tipAmount = it
                            )
                        )
                    }
                    2 -> {
                        if (splitDouble[1].length <=2)
                            viewModel.updateTipState(
                                tipUiState.value.copy(
                                    tipAmount = it
                                )
                            )
                    }
                }
            }
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
        placeholder = {
            Text("Base Amount...")
        }
    )
}

@Composable
fun Percentage(modifier: Modifier = Modifier) {
    val viewModel: TipViewModel = viewModel()
    val tipUiState = viewModel.tipAmount.collectAsState()

    Slider(
        value = tipUiState.value.percentage,
        onValueChange = {
            viewModel.updateTipState(
                tipUiState.value.copy(
                    percentage = it
                )
            )
        },
        valueRange = 0f..30f,
        steps = 29
    )
}

@Composable
fun Amount(modifier: Modifier = Modifier) {
    val viewModel: TipViewModel = viewModel()
    val tipUiState = viewModel.tipAmount.collectAsState()

    with(tipUiState.value.tipAmount.toDoubleOrNull()) {
        if (this != null) {
            val amount = this + (this * (tipUiState.value.percentage / 100))
            val twoDecimalRound = (amount * 100).roundToInt().toFloat() / 100
            Text(text = "$$twoDecimalRound")
        }
        else
            Text("Please Enter a Valid Amount!")
    }
}