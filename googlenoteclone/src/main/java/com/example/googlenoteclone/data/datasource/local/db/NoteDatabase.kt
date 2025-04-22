package com.example.googlenoteclone.data.datasource.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.googlenoteclone.data.datasource.local.db.entity.NoteEntity
import com.example.googlenoteclone.data.datasource.local.db.entity.NoteTagCrossRef
import com.example.googlenoteclone.data.datasource.local.db.entity.TagEntity

@Database(
    entities = [NoteEntity::class, TagEntity::class, NoteTagCrossRef::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(NoteConverters::class)
abstract class NoteDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
    abstract fun tagDao(): TagDao
}
