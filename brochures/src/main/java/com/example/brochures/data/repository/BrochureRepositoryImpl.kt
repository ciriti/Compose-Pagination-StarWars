package com.example.brochures.data.repository

import com.example.brochures.data.datasource.remote.BrochureDataSource
import com.example.brochures.domain.mapper.toBrochures
import com.example.brochures.domain.model.Brochure
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class BrochureRepositoryImpl(
    private val remoteDataSource: BrochureDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : BrochureRepository {
    override suspend fun getBrochures(): Result<List<Brochure>> = runCatching {
        withContext(ioDispatcher) {
            remoteDataSource.fetchBrochures()
                .getOrThrow()
                .toBrochures()
        }
    }
}
