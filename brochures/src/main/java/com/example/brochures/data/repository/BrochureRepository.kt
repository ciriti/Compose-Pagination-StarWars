package com.example.brochures.data.repository

import com.example.brochures.domain.model.Brochure


interface BrochureRepository {
    suspend fun getBrochures(): Result<List<Brochure>>
}
