package com.example.trycar_assessment_task.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.trycar_assessment_task.data.local.dao.FavoritePostDao
import com.example.trycar_assessment_task.data.local.dao.PostDao
import com.example.trycar_assessment_task.data.local.entity.FavoritePostEntity
import com.example.trycar_assessment_task.data.local.entity.PostEntity

@Database(
    entities = [PostEntity::class, FavoritePostEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun postDao(): PostDao
    abstract fun favoritePostDao(): FavoritePostDao
}
