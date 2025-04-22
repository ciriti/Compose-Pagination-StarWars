package com.example.googlenoteclone.data.datasource.local.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "tags")
data class TagEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val name: String,
    val color: Int, // ARGB color value
    val createdAt: Long = System.currentTimeMillis()
)
