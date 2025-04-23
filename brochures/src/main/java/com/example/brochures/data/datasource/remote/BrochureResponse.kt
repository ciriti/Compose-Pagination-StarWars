package com.example.brochures.data.datasource.remote

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.annotations.SerializedName
import java.lang.reflect.Type

data class BrochureResponse(
    @SerializedName("_embedded") val embedded: EmbeddedContentsDto,
)

data class EmbeddedContentsDto(
    @SerializedName("contents") val contents: List<ContentDto>
)

data class ContentDto(
    @SerializedName("contentType") val contentType: String,
    @SerializedName("content") val content: BrochureContentDto?
){
    fun toContentData(): ContentData? = when(contentType) {
        "brochure", "brochurePremium" -> content?.let { ContentData.Brochure(it) }
        else -> null
    }
}

sealed class ContentData {
    data class Brochure(val data: BrochureContentDto) : ContentData()
}

data class BrochureContentDto(
    @SerializedName("brochureImage") val imageUrl: String?,
    @SerializedName("publisher") val publisher: PublisherDto,
    @SerializedName("id") val id: String?,
    @SerializedName("distance") val distance: Double?,
)

data class PublisherDto(
    @SerializedName("name") val retailerName: String
)

class BrochureResponseDeserializer : JsonDeserializer<BrochureResponse> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): BrochureResponse {
        val jsonObject = json.asJsonObject
        val embedded = jsonObject.getAsJsonObject("_embedded")
        val contentsArray = embedded.getAsJsonArray("contents")

        val filteredContents = contentsArray.mapNotNull { contentElement ->
            if (contentElement.isJsonObject) {
                val contentObj = contentElement.asJsonObject
                val contentType = contentObj.get("contentType").asString

                if (contentType == "brochure" || contentType == "brochurePremium") {
                    val content = contentObj.get("content")
                    ContentDto(
                        contentType = contentType,
                        content = context.deserialize(content, BrochureContentDto::class.java)
                    )
                } else {
                    null
                }
            } else {
                null
            }
        }

        return BrochureResponse(
            embedded = EmbeddedContentsDto(contents = filteredContents)
        )
    }
}
