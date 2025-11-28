package com.example.trycar_assessment_task.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_posts")
data class FavoritePostEntity(
    @PrimaryKey
    val id: Int,

    val title: String,

    val body: String,

    val isSynced: Boolean = false
)
