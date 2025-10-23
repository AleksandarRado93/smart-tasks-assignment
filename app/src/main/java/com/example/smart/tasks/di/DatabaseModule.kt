package com.example.smart.tasks.di

import android.content.Context
import androidx.room.Room
import com.example.smart.tasks.data.local.db.SmartTasksDatabase
import com.example.smart.tasks.data.local.dao.TaskDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): SmartTasksDatabase {
        return Room.databaseBuilder(
            context,
            SmartTasksDatabase::class.java,
            "smart_tasks_db"
        ).build()
    }

    @Provides
    fun provideTaskDao(database: SmartTasksDatabase): TaskDao {
        return database.taskDao()
    }
}
