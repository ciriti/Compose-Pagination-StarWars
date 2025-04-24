package com.example.brochures.ui.screen.brochure

import androidx.lifecycle.viewModelScope
import com.example.brochures.domain.datasource.repository.BrochureRepository
import com.example.brochures.domain.model.Brochure
import com.example.brochures.ui.component.BaseViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.launch

class BrochureViewModel(
    private val repository: BrochureRepository
) : BaseViewModel<BrochureIntent, BrochureState, BrochureEffect>() {

    init {
        loadBrochures()
    }

    private var originalBrochures: ImmutableList<Brochure> = persistentListOf()

    override fun createInitialState(): BrochureState = BrochureState()

    override fun handleIntent(intent: BrochureIntent) {
        when (intent) {
            is BrochureIntent.LoadBrochures -> loadBrochures()
            is BrochureIntent.FilterByDistance -> filterByDistance(intent.enabled)
        }
    }

    private fun loadBrochures() {
        viewModelScope.launch {
            setState { copy(isLoading = true) }
            repository.getBrochures()
                .onSuccess {
                    originalBrochures = it.toImmutableList()
                    setState {
                        copy(
                            brochures = originalBrochures,
                            isLoading = false,
                            error = null
                        )
                    }
                }
                .onFailure { e ->
                    setState { copy(isLoading = false, error = e.message ?: "Unknown error") }
                    setEffect { BrochureEffect.ShowError(e.message ?: "Unknown error") }
                }
        }
    }

    private fun filterByDistance(enabled: Boolean) {
        setState {
            copy(
                brochures = when {
                    enabled -> originalBrochures.filter { (it.distance ?: 0.0) < 5.0 }.toImmutableList()
                    else -> originalBrochures
                },
                filterByDistance = enabled
            )
        }
    }
}
