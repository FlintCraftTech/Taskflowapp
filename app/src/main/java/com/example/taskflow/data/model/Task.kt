package com.example.taskflow.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "tasks",
    foreignKeys = [
        ForeignKey(
            entity = Project::class,
            parentColumns = ["id"],
            childColumns = ["project_id"],
            // project_id is non-null (every task belongs to a Project — its own, or the system
            // Unassigned one), so SET_NULL is invalid. Deleting a real Project reassigns its tasks
            // to Unassigned in ProjectRepository before the delete; RESTRICT is the backstop that
            // surfaces a bug loudly if that reassignment is ever skipped, rather than orphaning rows.
            onDelete = ForeignKey.RESTRICT
        ),
        ForeignKey(
            entity = Task::class,
            parentColumns = ["id"],
            childColumns = ["parent_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("project_id"),
        Index("parent_id"),
        Index("slot"),
        Index("date")
    ]
)
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val title: String,

    val notes: String = "",

    // Every task belongs to exactly one Project — never null. Defaults to the system Unassigned
    // Project (see Project.UNASSIGNED_PROJECT_ID) until the user files it into one of their own.
    @ColumnInfo(name = "project_id")
    val projectId: Long = Project.UNASSIGNED_PROJECT_ID,

    val date: Long? = null,

    val slot: ScheduleSlot? = null,

    @ColumnInfo(name = "parent_id")
    val parentId: Long? = null,

    @ColumnInfo(name = "is_completed")
    val isCompleted: Boolean = false,

    @ColumnInfo(name = "project_suggestion_declined")
    val projectSuggestionDeclined: Boolean = false,

    @ColumnInfo(name = "slot_sort_order")
    val slotSortOrder: Int = 0,

    @ColumnInfo(name = "project_sort_order")
    val projectSortOrder: Int = 0
)
