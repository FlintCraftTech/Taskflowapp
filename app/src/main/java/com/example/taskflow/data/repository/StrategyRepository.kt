package com.example.taskflow.data.repository

import com.example.taskflow.data.local.StrategyEntryDao
import com.example.taskflow.data.model.Project
import com.example.taskflow.data.model.StrategyEntry
import kotlinx.coroutines.flow.Flow

class StrategyRepository(private val strategyEntryDao: StrategyEntryDao) {

    /**
     * Mirror a Strategy entry for a Project. The system Unassigned Project is excluded from the
     * Strategy doc — it is not a real area of life, only the home for unfiled tasks (SPEC §Strategy
     * doc) — so an entry for it is never written; the call is a no-op returning -1. Identified by its
     * well-known id (there is exactly one system Project, seeded at [Project.UNASSIGNED_PROJECT_ID]).
     */
    suspend fun upsert(entry: StrategyEntry): Long {
        if (entry.projectId == Project.UNASSIGNED_PROJECT_ID) return -1L
        return strategyEntryDao.upsert(entry)
    }

    suspend fun getByProjectId(projectId: Long): StrategyEntry? =
        strategyEntryDao.getByProjectId(projectId)

    fun observeByProjectId(projectId: Long): Flow<StrategyEntry?> =
        strategyEntryDao.observeByProjectId(projectId)

    fun getAllOrdered(): Flow<List<StrategyEntry>> = strategyEntryDao.getAllOrdered()

    suspend fun getAllOrderedList(): List<StrategyEntry> = strategyEntryDao.getAllOrderedList()

    suspend fun deleteByProjectId(projectId: Long) =
        strategyEntryDao.deleteByProjectId(projectId)
}
