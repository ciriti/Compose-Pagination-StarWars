package com.example.brochures

import com.example.brochures.data.datasource.remote.BrochureContentDto
import com.example.brochures.data.datasource.remote.BrochureResponse
import com.example.brochures.data.datasource.remote.ContentDto
import com.example.brochures.data.datasource.remote.EmbeddedContentsDto
import com.example.brochures.data.datasource.remote.PublisherDto
import com.example.brochures.domain.model.Brochure
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import retrofit2.Response

val testPublisher = PublisherDto("Test Retailer")

val testBrochureContent = BrochureContentDto(
    imageUrl = "image.jpg",
    publisher = testPublisher,
    id = "123",
    distance = 3.5
)

// Content lists
val validContentDtos = listOf(
    ContentDto("brochure", testBrochureContent),
    ContentDto("brochurePremium", testBrochureContent.copy(id = "456"))
)

val mixedContentDtos = validContentDtos + listOf(
    ContentDto("invalidType", null),
    ContentDto("brochure", testBrochureContent.copy(imageUrl = null))
)

// Mock responses
fun successfulResponse(contents: List<ContentDto> = validContentDtos): Response<BrochureResponse> {
    return mockk {
        coEvery { isSuccessful } returns true
        coEvery { body() } returns BrochureResponse(
            EmbeddedContentsDto(contents)
        )
    }
}

fun emptyBodyResponse(): Response<BrochureResponse> {
    return mockk {
        coEvery { isSuccessful } returns true
        coEvery { body() } returns null
    }
}

val testContentDtos = listOf(
    ContentDto("brochure", testBrochureContent),
    ContentDto("brochurePremium", testBrochureContent.copy(id = "456")),
    ContentDto("invalidType", null) // Should be filtered out
)

val testBrochure1 = Brochure(
    id = "1",
    image = "image1.jpg",
    retailer = "Retailer 1",
    distance = 2.5,
    contentType = "brochure"
)
val testBrochure2 = Brochure(
    id = "2",
    image = "image2.jpg",
    retailer = "Retailer 2",
    distance = 6.5,
    contentType = "brochure"
)

val testBrochures: ImmutableList<Brochure> = persistentListOf(testBrochure1)
