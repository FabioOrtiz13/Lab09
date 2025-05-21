package com.tecsup.lab09

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path


interface PostApiService {
    @GET("posts")
    suspend fun getUserPosts(): List<PostModel>

    @GET("posts/{id}")
    suspend fun getUserPostById(@Path("id") id: Int): PostModel

    @POST("posts")
    suspend fun createPost(@Body post: PostModel): PostModel


}

