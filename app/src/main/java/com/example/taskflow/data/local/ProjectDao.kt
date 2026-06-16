package com.example.taskflow.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.taskflow.data.model.Project
import kotlinx.coroutines.flow.Flow

@Dao
interface ProjectDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(project: Project): Long

    @Update
    suspend fun update(project: Project)

    @Delete
    suspend fun delete(project: Project)

    @Query("SELECT * FROM projects WHERE id = :id")
    suspend fun getById(id: Long): Project?

    @Query("SELECT * FROM projects ORDER BY sort_order ASC")
    fun getAllOrdered(): Flow<List<Project>>

    @Query("SELECT * FROM projects ORDER BY sort_order ASC")
    suspend fun getAllOrderedList(): List<Project>

    @Query("UPDATE projects SET sort_order = :newOrder WHERE id = :projectId")
    suspend fun updateSortOrder(projectId: Long, newOrder: Int)

    @Query("SELECT COALESCE(MAX(sort_order), -1) FROM projects")
    suspend fun getMaxSortOrder(): Int
}
