package com.example.brochures.ui.navigation

import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.brochures.ui.screen.brochure.BrochureRoute

fun NavGraphBuilder.brochuresGraph(
    modifier: Modifier = Modifier
) {
    composable(Route.Brochures.route) {
        BrochureRoute(modifier)
    }
}
