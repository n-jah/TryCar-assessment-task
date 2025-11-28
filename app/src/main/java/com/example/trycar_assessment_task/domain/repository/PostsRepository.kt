package com.example.trycar_assessment_task.domain.repository

import com.example.trycar_assessment_task.data.model.Comment
import com.example.trycar_assessment_task.data.model.Post
import com.example.trycar_assessment_task.util.Resource
import kotlinx.coroutines.flow.Flow

interface PostsRepository {


    suspend fun getPosts(forceRefresh: Boolean = false): Flow<Resource<List<Post>>>

    suspend fun getComments(postId: Int): Resource<List<Comment>>

}