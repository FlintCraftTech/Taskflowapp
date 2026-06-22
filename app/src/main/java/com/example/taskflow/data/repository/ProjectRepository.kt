package com.example.taskflow.data.repository

import com.example.taskflow.data.local.ProjectDao
import com.example.taskflow.data.local.TaskDao
import com.example.taskflow.data.model.Project
import kotlinx.coroutines.flow.Flow

class ProjectRepository(
    private val projectDao: ProjectDao,
    private val taskDao: TaskDao,
) {

    suspend fun insert(project: Project): Long = projectDao.insert(project)

    suspend fun update(project: Project) = projectDao.update(project)

    /**
     * Delete a real (non-system) Project. Its tasks are reassigned to the system Unassigned Project
     * first so nothing is orphaned — the tasks→projects foreign key is RESTRICT, so a delete with
     * tasks still pointing at it would otherwise fail. The system Unassigned Project is undeletable;
     * calling this on it is a no-op.
     */
    suspend fun delete(project: Project) {
        if (project.isSystem) return
        taskDao.reassignTasksToProject(project.id, Project.UNASSIGNED_PROJECT_ID)
        projectDao.delete(project)
    }

    suspend fun getById(id: Long): Project? = projectDao.getById(id)

    /** The system Unassigned Project (the home for unfiled tasks), or null before it is seeded. */
    suspend fun getSystemUnassigned(): Project? = projectDao.getSystemUnassigned()

    /** User-created Projects in saved order — excludes the system Unassigned Project. */
    fun getAllOrdered(): Flow<List<Project>> = projectDao.getAllOrdered()

    suspend fun getAllOrderedList(): List<Project> = projectDao.getAllOrderedList()

    suspend fun updateSortOrder(projectId: Long, newOrder: Int) =
        projectDao.updateSortOrder(projectId, newOrder)

    suspend fun getMaxSortOrder(): Int = projectDao.getMaxSortOrder()
}
