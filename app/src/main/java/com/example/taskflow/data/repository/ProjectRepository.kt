package com.example.taskflow.data.repository

import com.example.taskflow.data.local.ProjectDao
import com.example.taskflow.data.model.Project
import kotlinx.coroutines.flow.Flow

class ProjectRepository(private val projectDao: ProjectDao) {

    suspend fun insert(project: Project): Long = projectDao.insert(project)

    suspend fun update(project: Project) = projectDao.update(project)

    suspend fun delete(project: Project) = projectDao.delete(project)

    suspend fun getById(id: Long): Project? = projectDao.getById(id)

    fun getAllOrdered(): Flow<List<Project>> = projectDao.getAllOrdered()

    suspend fun getAllOrderedList(): List<Project> = projectDao.getAllOrderedList()

    suspend fun updateSortOrder(projectId: Long, newOrder: Int) =
        projectDao.updateSortOrder(projectId, newOrder)

    suspend fun getMaxSortOrder(): Int = projectDao.getMaxSortOrder()
}
