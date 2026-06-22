package com.example.taskflow.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "projects")
data class Project(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val name: String,

    @ColumnInfo(name = "sort_order")
    val sortOrder: Int = 0,

    // Marks the system "Unassigned" Project — the home for every task the user hasn't filed into
    // one of their own Projects. There is exactly one, seeded at id = UNASSIGNED_PROJECT_ID. It is
    // undeletable, excluded from the Strategy doc, and kept out of the user-facing Project list
    // (the ordered-Projects queries filter it out). User-created Projects always have is_system = false.
    @ColumnInfo(name = "is_system")
    val isSystem: Boolean = false
) {
    companion object {
        // Well-known fixed id for the single system Unassigned Project. Task.projectId defaults to
        // this, and the migration/seed insert the row at exactly this id so the default always
        // resolves to a real row (the tasks→projects foreign key requires it).
        const val UNASSIGNED_PROJECT_ID: Long = 1L

        const val UNASSIGNED_PROJECT_NAME: String = "Unassigned"
    }
}
