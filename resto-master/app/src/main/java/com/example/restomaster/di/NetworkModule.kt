package com.example.restomaster.di

import com.example.restomaster.BuildConfig
import com.example.restomaster.data.remote.MenuOcrApi
import com.example.restomaster.data.remote.GeminiApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }
        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit {
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
        return Retrofit.Builder()
            .baseUrl(BuildConfig.GEMINI_OCR_BASE_URL)
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    @Provides
    @Singleton
    fun provideMenuOcrApi(retrofit: Retrofit): MenuOcrApi {
        return retrofit.create(MenuOcrApi::class.java)
    }

    @Provides
    @Singleton
    @Named("gemini")
    fun provideGeminiRetrofit(client: OkHttpClient): Retrofit {
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
        return Retrofit.Builder()
            .baseUrl("https://generativelanguage.googleapis.com/")
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    @Provides
    @Singleton
    fun provideGeminiApi(@Named("gemini") retrofit: Retrofit): GeminiApiService {
        return retrofit.create(GeminiApiService::class.java)
    }

    @Provides
    @Singleton
    @Named("stripe")
    fun provideStripeRetrofit(client: OkHttpClient): Retrofit {
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
        // Use 10.0.2.2 for Android Emulator to access host localhost
        // If running on physical device, this needs to be the computer's IP
        val baseUrl = "http://10.0.2.2:8000/" 
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    @Provides
    @Singleton
    fun provideStripeApiService(@Named("stripe") retrofit: Retrofit): com.example.restomaster.data.remote.StripeApiService {
        return retrofit.create(com.example.restomaster.data.remote.StripeApiService::class.java)
    }
    
    @Provides
    @Singleton
    fun providePayPayApiService(@Named("stripe") retrofit: Retrofit): com.example.restomaster.data.remote.PayPayApiService {
        // Reuse the same base URL/Retrofit as Stripe (Python Backend) for simplicity
        return retrofit.create(com.example.restomaster.data.remote.PayPayApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideImageEnhancementApiService(@Named("stripe") retrofit: Retrofit): com.example.restomaster.data.remote.ImageEnhancementApiService {
        return retrofit.create(com.example.restomaster.data.remote.ImageEnhancementApiService::class.java)
    }
}
