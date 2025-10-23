package com.example.smart.tasks.di

import com.example.smart.tasks.data.local.dao.TaskDao
import com.example.smart.tasks.data.remote.api.ApiService
import com.example.smart.tasks.data.repository.TaskRepositoryImpl
import com.example.smart.tasks.domain.repository.TaskRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideTaskRepository(apiService: ApiService, taskDao: TaskDao): TaskRepository =
        TaskRepositoryImpl(api = apiService, dao = taskDao)
}