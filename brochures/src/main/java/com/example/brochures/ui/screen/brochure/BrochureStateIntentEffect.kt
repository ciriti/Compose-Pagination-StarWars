package com.example.brochures.ui.screen.brochure

import androidx.compose.runtime.Immutable
import com.example.brochures.domain.model.Brochure
import com.example.brochures.ui.component.UiEffect
import com.example.brochures.ui.component.UiIntent
import com.example.brochures.ui.component.UiState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

sealed interface BrochureIntent : UiIntent {
    data object LoadBrochures : BrochureIntent
    data class FilterByDistance(val enabled: Boolean) : BrochureIntent
}

@Immutable
data class BrochureState(
    val brochures: ImmutableList<Brochure> = persistentListOf(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val filterByDistance: Boolean = false
) : UiState

sealed interface BrochureEffect : UiEffect {
    data class ShowError(val message: String) : BrochureEffect
}
