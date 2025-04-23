package com.example.brochures.data.datasource.remote

interface BrochureDataSource {
    suspend fun fetchBrochures(): Result<List<ContentDto>>
}
