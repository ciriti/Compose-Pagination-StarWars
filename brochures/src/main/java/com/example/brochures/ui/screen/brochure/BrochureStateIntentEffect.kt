package com.example.brochures.ui.screen.brochure

import androidx.compose.runtime.Stable
import com.example.brochures.domain.model.Brochure
import com.example.brochures.ui.component.UiEffect
import com.example.brochures.ui.component.UiIntent
import com.example.brochures.ui.component.UiState

sealed interface BrochureIntent : UiIntent {
    data object LoadBrochures : BrochureIntent
    data class FilterByDistance(val enabled: Boolean) : BrochureIntent
}

@Stable
data class BrochureState(
    val brochures: List<Brochure> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val filterByDistance: Boolean = false
) : UiState

sealed interface BrochureEffect : UiEffect {
    data class ShowError(val message: String) : BrochureEffect
}
