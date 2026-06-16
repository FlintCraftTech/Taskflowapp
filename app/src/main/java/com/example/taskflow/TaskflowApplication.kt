package com.example.taskflow

import android.app.Application
import com.example.taskflow.data.local.TaskflowDatabase
import com.example.taskflow.data.repository.ProjectRepository
import com.example.taskflow.data.repository.StrategyRepository
import com.example.taskflow.data.repository.TaskRepository

class TaskflowApplication : Application() {

    val database: TaskflowDatabase by lazy {
        TaskflowDatabase.getInstance(this)
    }

    val taskRepository: TaskRepository by lazy {
        TaskRepository(database.taskDao())
    }

    val projectRepository: ProjectRepository by lazy {
        ProjectRepository(database.projectDao())
    }

    val strategyRepository: StrategyRepository by lazy {
        StrategyRepository(database.strategyEntryDao())
    }
}
