package com.example.taskflow.ui.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.taskflow.data.model.Project
import com.example.taskflow.data.model.StrategyEntry
import com.example.taskflow.data.repository.ProjectRepository
import com.example.taskflow.data.repository.StrategyRepository
import kotlinx.coroutines.launch

/**
 * App-level Project-creation plumbing. The drawer's per-Project list and the Projects-overview page
 * that once consumed a `projects` flow here are both gone (Projects now live inside Later, grouped by
 * the schedule layer's own observation). What remains is [createProject], the name-only creation path
 * — preserved from [project-create] for the Later-by-Project lifecycle UI ([project-lifecycle-later])
 * to wire into the editor's "New Project" picker entry. It has no caller in this batch.
 */
class AppViewModel(
    private val projectRepository: ProjectRepository,
    private val strategyRepository: StrategyRepository,
) : ViewModel() {

    /**
     * Create a Project from a typed name (SPEC §Create or delete a Project). A name is all creation
     * asks for; a blank name is ignored. The Project lands at the end of the sort order (max + 1) so
     * it appears last on Later (above the pinned Unassigned card) and in the Strategy doc. Its empty
     * Strategy entry is created alongside so the Strategy-doc paragraph exists to be written later.
     */
    fun createProject(name: String) {
        val trimmed = name.trim()
        if (trimmed.isEmpty()) return
        viewModelScope.launch {
            val nextOrder = projectRepository.getMaxSortOrder() + 1
            val projectId = projectRepository.insert(Project(name = trimmed, sortOrder = nextOrder))
            strategyRepository.upsert(StrategyEntry(projectId = projectId))
        }
    }

    companion object {
        fun factory(
            projectRepository: ProjectRepository,
            strategyRepository: StrategyRepository,
        ) = viewModelFactory {
            initializer { AppViewModel(projectRepository, strategyRepository) }
        }
    }
}
