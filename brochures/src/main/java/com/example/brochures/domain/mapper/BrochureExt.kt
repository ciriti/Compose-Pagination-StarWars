package com.example.brochures.domain.mapper

import com.example.brochures.data.datasource.remote.ContentDto
import com.example.brochures.domain.InvalidBrochureException
import com.example.brochures.domain.model.Brochure

fun ContentDto.toBrochure(): Brochure {
    return content.let { brochureContent ->
        Brochure(
            id = brochureContent?.id ?: throw InvalidBrochureException("Invalid Id"),
            image = brochureContent.imageUrl,
            retailer = brochureContent.publisher.retailerName,
            distance = brochureContent.distance,
            contentType = contentType
        )
    }
}

fun List<ContentDto>.toBrochures(): List<Brochure> {
    return this
        .map { runCatching { it.toBrochure() } }
        .mapNotNull { it.getOrNull() }
}
