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
            onDelete = ForeignKey.SET_NULL
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

    @ColumnInfo(name = "project_id")
    val projectId: Long? = null,

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
