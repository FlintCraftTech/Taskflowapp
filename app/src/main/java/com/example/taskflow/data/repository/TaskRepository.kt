package com.example.taskflow.data.repository

import com.example.taskflow.data.local.TaskDao
import com.example.taskflow.data.model.ScheduleSlot
import com.example.taskflow.data.model.Task
import kotlinx.coroutines.flow.Flow

class TaskRepository(private val taskDao: TaskDao) {

    suspend fun insert(task: Task): Long = taskDao.insert(task)

    suspend fun update(task: Task) = taskDao.update(task)

    suspend fun delete(task: Task) = taskDao.delete(task)

    suspend fun getById(id: Long): Task? = taskDao.getById(id)

    fun observeById(id: Long): Flow<Task?> = taskDao.observeById(id)

    fun observeActiveTopLevel(): Flow<List<Task>> = taskDao.observeActiveTopLevel()

    fun getTasksBySlot(slot: ScheduleSlot): Flow<List<Task>> = taskDao.getTasksBySlot(slot)

    fun getTasksByDate(date: Long): Flow<List<Task>> = taskDao.getTasksByDate(date)

    fun getDatedTasksByProject(projectId: Long): Flow<List<Task>> =
        taskDao.getDatedTasksByProject(projectId)

    fun getUndatedTasksByProject(projectId: Long): Flow<List<Task>> =
        taskDao.getUndatedTasksByProject(projectId)

    fun getSubtasks(parentId: Long): Flow<List<Task>> = taskDao.getSubtasks(parentId)

    suspend fun getSubtasksList(parentId: Long): List<Task> = taskDao.getSubtasksList(parentId)

    fun getCompletedTasks(): Flow<List<Task>> = taskDao.getCompletedTasks()

    suspend fun updateSlotSortOrder(taskId: Long, newOrder: Int) =
        taskDao.updateSlotSortOrder(taskId, newOrder)

    suspend fun updateProjectSortOrder(taskId: Long, newOrder: Int) =
        taskDao.updateProjectSortOrder(taskId, newOrder)

    suspend fun updateProjectId(taskId: Long, projectId: Long?) =
        taskDao.updateProjectId(taskId, projectId)

    suspend fun updateDateAndSlot(taskId: Long, date: Long?, slot: ScheduleSlot?) =
        taskDao.updateDateAndSlot(taskId, date, slot)

    suspend fun updateCompletion(taskId: Long, isCompleted: Boolean) =
        taskDao.updateCompletion(taskId, isCompleted)

    suspend fun getMaxSlotSortOrder(slot: ScheduleSlot): Int =
        taskDao.getMaxSlotSortOrder(slot)

    suspend fun getMaxProjectSortOrder(projectId: Long): Int =
        taskDao.getMaxProjectSortOrder(projectId)

    suspend fun getAll(): List<Task> = taskDao.getAll()
}
