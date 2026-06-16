package com.example.taskflow.data.repository

import com.example.taskflow.data.local.StrategyEntryDao
import com.example.taskflow.data.model.StrategyEntry
import kotlinx.coroutines.flow.Flow

class StrategyRepository(private val strategyEntryDao: StrategyEntryDao) {

    suspend fun upsert(entry: StrategyEntry): Long = strategyEntryDao.upsert(entry)

    suspend fun getByProjectId(projectId: Long): StrategyEntry? =
        strategyEntryDao.getByProjectId(projectId)

    fun observeByProjectId(projectId: Long): Flow<StrategyEntry?> =
        strategyEntryDao.observeByProjectId(projectId)

    fun getAllOrdered(): Flow<List<StrategyEntry>> = strategyEntryDao.getAllOrdered()

    suspend fun getAllOrderedList(): List<StrategyEntry> = strategyEntryDao.getAllOrderedList()

    suspend fun deleteByProjectId(projectId: Long) =
        strategyEntryDao.deleteByProjectId(projectId)
}
