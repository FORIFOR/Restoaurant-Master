package com.example.restomaster.data.remote

import com.example.restomaster.data.remote.gemini.GeminiRequest
import com.example.restomaster.data.remote.gemini.GeminiResponse
import com.example.restomaster.data.remote.gemini.ImagenRequest
import com.example.restomaster.data.remote.gemini.ImagenResponse
import retrofit2.http.Body
import retrofit2.http.Path
import retrofit2.http.POST
import retrofit2.http.Query

interface GeminiApiService {
    @POST("v1beta/models/{model}:generateContent")
    suspend fun generateContent(
        @Path("model") model: String,
        @Query("key") apiKey: String,
        @Body body: GeminiRequest
    ): GeminiResponse
    
    @POST("v1beta/models/{model}:predict")
    suspend fun generateImages(
        @Path("model") model: String,
        @Query("key") apiKey: String,
        @Body body: ImagenRequest
    ): ImagenResponse
}
