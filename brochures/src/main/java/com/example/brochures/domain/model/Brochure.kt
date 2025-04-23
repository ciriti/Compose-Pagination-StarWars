package com.example.brochures.domain.model

import androidx.compose.runtime.Immutable

@Immutable
data class Brochure(
    val id: String,
    val image: String?,
    val retailer: String,
    val distance: Double?,
    val contentType: String,
){
    val isPremium: Boolean = (contentType == "brochurePremium")
}
