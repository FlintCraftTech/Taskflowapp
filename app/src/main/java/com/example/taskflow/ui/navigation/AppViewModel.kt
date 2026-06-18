package com.example.taskflow.ui.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.taskflow.data.model.Project
import com.example.taskflow.data.repository.ProjectRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

/**
 * App-level navigation state backing the drawer and the Projects overview page. Exposes the user's
 * Projects in saved (sort_order) order — the same list both surfaces render, per SPEC §Side menu and
 * §Projects overview. Reorder is deferred to the Strategy-doc work (see _build.md scope call), so
 * this is read-only for now.
 */
class AppViewModel(repository: ProjectRepository) : ViewModel() {

    val projects: StateFlow<List<Project>> =
        repository.getAllOrdered()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    companion object {
        fun factory(repository: ProjectRepository) = viewModelFactory {
            initializer { AppViewModel(repository) }
        }
    }
}
