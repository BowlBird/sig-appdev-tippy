package com.github.bowlbird.tippy

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

data class TipUiState(
    val tipAmount: String = "",
    val percentage: Float = 0f
)

class TipViewModel : ViewModel() {
    private var _tipState = MutableStateFlow(TipUiState())
    var tipAmount: StateFlow<TipUiState> = _tipState

    fun updateTipState(state: TipUiState) {
        _tipState.update { _ ->
            state
        }
    }
}