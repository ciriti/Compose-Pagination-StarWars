package com.example.brochures.domain.datasource.remote

import com.example.brochures.data.datasource.remote.ContentDto

interface BrochureDataSource {
    suspend fun fetchBrochures(): Result<List<ContentDto>>
}
