package com.example.trycar_assessment_task.di

import android.content.Context
import androidx.room.Room
import com.example.trycar_assessment_task.Network.INetworkConnectivityObserver
import com.example.trycar_assessment_task.Network.NetworkConnectivityObserver
import com.example.trycar_assessment_task.data.local.dao.FavoritePostDao
import com.example.trycar_assessment_task.data.local.dao.PostDao
import com.example.trycar_assessment_task.data.local.database.AppDatabase
import com.example.trycar_assessment_task.data.remote.api.PostsApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)

object AppModule {
    private const val BASE_URL = "https://jsonplaceholder.typicode.com/"
    private const val DATABASE_NAME = "posts_database"

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }
    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun providePostsApiService(retrofit: Retrofit): PostsApiService {
        return retrofit.create(PostsApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun providePostDao(database: AppDatabase): PostDao {
        return database.postDao()
    }

    @Provides
    @Singleton
    fun provideFavoritePostDao(database: AppDatabase): FavoritePostDao {
        return database.favoritePostDao()
    }

    @Provides
    @Singleton
    fun provideNetworkConnectivityObserver(
        @ApplicationContext context: Context
    ): INetworkConnectivityObserver {
        return NetworkConnectivityObserver(context)
    }

}