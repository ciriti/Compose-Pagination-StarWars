package com.example.brochures.domain.datasource.repository

import com.example.brochures.domain.model.Brochure
import kotlinx.collections.immutable.ImmutableList


interface BrochureRepository {
    suspend fun getBrochures(): Result<ImmutableList<Brochure>>
}
