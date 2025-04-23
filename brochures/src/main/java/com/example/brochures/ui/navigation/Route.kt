package com.example.brochures.ui.navigation

sealed class Route(val route: String) {
    data object Brochures : Route("brochures")
}
