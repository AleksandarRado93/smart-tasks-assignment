package com.example.smart.tasks.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.smart.tasks.data.local.dao.TaskDao
import com.example.smart.tasks.data.local.model.TaskEntity

@Database(
    entities = [TaskEntity::class],
    version = 1,
    exportSchema = false
)
abstract class SmartTasksDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
}