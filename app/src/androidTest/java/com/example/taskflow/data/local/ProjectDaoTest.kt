package com.example.taskflow.data.local

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.taskflow.data.model.Project
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ProjectDaoTest {

    private lateinit var database: TaskflowDatabase
    private lateinit var projectDao: ProjectDao

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, TaskflowDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        projectDao = database.projectDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun createProject_isRetrievable() = runTest {
        val id = projectDao.insert(Project(name = "Work", sortOrder = 0))

        val retrieved = projectDao.getById(id)
        assertNotNull(retrieved)
        assertEquals("Work", retrieved!!.name)
        assertEquals(0, retrieved.sortOrder)
    }

    @Test
    fun updateProject_changesArePersisted() = runTest {
        val id = projectDao.insert(Project(name = "Work", sortOrder = 0))
        val project = projectDao.getById(id)!!

        projectDao.update(project.copy(name = "Career"))

        val updated = projectDao.getById(id)
        assertEquals("Career", updated!!.name)
    }

    @Test
    fun deleteProject_isRemoved() = runTest {
        val id = projectDao.insert(Project(name = "Temp", sortOrder = 0))
        val project = projectDao.getById(id)!!

        projectDao.delete(project)

        val deleted = projectDao.getById(id)
        assertNull(deleted)
    }

    @Test
    fun getAllOrdered_returnsSortedBySortOrder() = runTest {
        projectDao.insert(Project(name = "C-project", sortOrder = 2))
        projectDao.insert(Project(name = "A-project", sortOrder = 0))
        projectDao.insert(Project(name = "B-project", sortOrder = 1))

        val projects = projectDao.getAllOrdered().first()
        assertEquals(3, projects.size)
        assertEquals("A-project", projects[0].name)
        assertEquals("B-project", projects[1].name)
        assertEquals("C-project", projects[2].name)
    }

    @Test
    fun reorderProjects_persistedOrderChanges() = runTest {
        val id1 = projectDao.insert(Project(name = "First", sortOrder = 0))
        val id2 = projectDao.insert(Project(name = "Second", sortOrder = 1))
        val id3 = projectDao.insert(Project(name = "Third", sortOrder = 2))

        // Move Third to first position
        projectDao.updateSortOrder(id3, 0)
        projectDao.updateSortOrder(id1, 1)
        projectDao.updateSortOrder(id2, 2)

        val projects = projectDao.getAllOrdered().first()
        assertEquals("Third", projects[0].name)
        assertEquals("First", projects[1].name)
        assertEquals("Second", projects[2].name)
    }

    @Test
    fun getMaxSortOrder_returnsCorrectValue() = runTest {
        assertEquals(-1, projectDao.getMaxSortOrder()) // Empty table

        projectDao.insert(Project(name = "A", sortOrder = 0))
        projectDao.insert(Project(name = "B", sortOrder = 5))
        projectDao.insert(Project(name = "C", sortOrder = 3))

        assertEquals(5, projectDao.getMaxSortOrder())
    }
}
