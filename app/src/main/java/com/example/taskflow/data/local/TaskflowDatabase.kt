package com.example.taskflow.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.taskflow.data.model.Project
import com.example.taskflow.data.model.StrategyEntry
import com.example.taskflow.data.model.Task

@Database(
    entities = [Task::class, Project::class, StrategyEntry::class],
    version = 1,
    exportSchema = false
)
abstract class TaskflowDatabase : RoomDatabase() {

    abstract fun taskDao(): TaskDao
    abstract fun projectDao(): ProjectDao
    abstract fun strategyEntryDao(): StrategyEntryDao

    companion object {
        @Volatile
        private var INSTANCE: TaskflowDatabase? = null

        fun getInstance(context: Context): TaskflowDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TaskflowDatabase::class.java,
                    "taskflow_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
