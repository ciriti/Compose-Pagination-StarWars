package com.example.brochures.data.datasource.remote

import com.example.brochures.domain.getException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response


class BrochureDataSourceImpl(
    private val api: BrochureApi,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val exceptionMapper: (Response<*>) -> Exception = { r -> getException(r) },
) : BrochureDataSource {

    override suspend fun fetchBrochures(): Result<List<ContentDto>> = runCatching {
        withContext(ioDispatcher) {
            val response = api.getBrochures()
            if (response.isSuccessful) {
                val brochureResponse = response.body()
                    ?: throw RuntimeException("Empty response") // TODO create custom exception
                brochureResponse.embedded.contents
                    .filter { it.isBrochure() }
            } else {
                throw exceptionMapper(response)
            }
        }
    }
}


private fun ContentDto.isBrochure(): Boolean {
    return contentType in setOf("brochure", "brochurePremium") &&
            content?.imageUrl != null
}
