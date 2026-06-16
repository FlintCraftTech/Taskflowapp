package com.example.taskflow.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.taskflow.data.model.StrategyEntry
import kotlinx.coroutines.flow.Flow

@Dao
interface StrategyEntryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entry: StrategyEntry): Long

    @Query("SELECT * FROM strategy_entries WHERE project_id = :projectId")
    suspend fun getByProjectId(projectId: Long): StrategyEntry?

    @Query("SELECT * FROM strategy_entries WHERE project_id = :projectId")
    fun observeByProjectId(projectId: Long): Flow<StrategyEntry?>

    @Query("""
        SELECT se.* FROM strategy_entries se
        INNER JOIN projects p ON se.project_id = p.id
        ORDER BY p.sort_order ASC
    """)
    fun getAllOrdered(): Flow<List<StrategyEntry>>

    @Query("""
        SELECT se.* FROM strategy_entries se
        INNER JOIN projects p ON se.project_id = p.id
        ORDER BY p.sort_order ASC
    """)
    suspend fun getAllOrderedList(): List<StrategyEntry>

    @Query("DELETE FROM strategy_entries WHERE project_id = :projectId")
    suspend fun deleteByProjectId(projectId: Long)
}
