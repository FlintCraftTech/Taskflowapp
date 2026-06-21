package com.example.taskflow.ui.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.taskflow.data.model.Project
import com.example.taskflow.data.model.StrategyEntry
import com.example.taskflow.data.repository.ProjectRepository
import com.example.taskflow.data.repository.StrategyRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * App-level navigation state backing the drawer and the Projects overview page. Exposes the user's
 * Projects in saved (sort_order) order — the same list both surfaces render, per SPEC §Side menu and
 * §Projects overview — and creates new Projects from the overview's add affordance. Reorder is
 * deferred to the Strategy-doc work (see _build.md scope call), so the list is otherwise read-only.
 */
class AppViewModel(
    private val projectRepository: ProjectRepository,
    private val strategyRepository: StrategyRepository,
) : ViewModel() {

    val projects: StateFlow<List<Project>> =
        projectRepository.getAllOrdered()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    /**
     * Create a Project from a typed name (SPEC §Projects overview). A name is all creation asks for;
     * a blank name is ignored. The Project lands at the end of the sort order (max + 1) so it appears
     * last on the overview, in the side menu, and in the Strategy doc. Its empty Strategy entry is
     * created alongside so the Strategy-doc paragraph exists to be written later.
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
