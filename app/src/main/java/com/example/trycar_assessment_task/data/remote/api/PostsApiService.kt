package com.example.trycar_assessment_task.data.remote.api

import com.example.trycar_assessment_task.data.model.Comment
import com.example.trycar_assessment_task.data.model.Post
import retrofit2.http.GET
import retrofit2.http.Path

interface PostsApiService {

    @GET("posts")
    suspend fun getPosts(): List<Post>

    @GET("posts/{id}/comments")
    suspend fun getComments(@Path("id") postId: Int): List<Comment>
}
