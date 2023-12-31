package com.example.boardsdraft.db

import androidx.lifecycle.LiveData
import com.example.boardsdraft.db.entities.Task
import com.example.boardsdraft.db.entities.relations.ProjectWithTasks
import com.example.boardsdraft.db.entities.relations.UserWithTasks

class TasksRepoImp(
    private val dao: TasksDAO
): TasksRepo {

    private val lastTaskID = dao.getLastTaskID()
    override suspend fun insertTask(task: Task) {
        dao.insertTask(task)
    }

    override suspend fun updateTask(task: Task) {
        dao.updateTask(task)
    }

    override suspend fun deleteTask(task: Task) {
        dao.deleteTask(task)
    }

    override fun getTasksOfCurrentUser(userID: Int): LiveData<List<UserWithTasks>?> {
        return dao.getTaskOfUser(userID)
    }

    override fun getTasksOfProject(projectID: Int): LiveData<List<ProjectWithTasks>?> {
        return dao.getTaskOfProject(projectID)
    }

    override fun getLastTaskID(): LiveData<Int?> {
        return lastTaskID
    }

    override suspend fun updateTaskStatus(oldStatus: String, newStatus: String) {
        dao.updateTaskStatus(oldStatus,newStatus)
    }

    override suspend fun getTaskByID(taskID: Int): Task {
        return dao.getTaskByID(taskID)
    }

    override suspend fun deleteTasksByProjectAndUser(projectID: Int, userID: Int) {
        dao.deleteTasksByProjectAndUser(projectID,userID)
    }

    override suspend fun deleteTaskByTitle(title: String) {
        dao.deleteTaskByTitle(title)
    }

    override suspend fun updateAssignedToName(id: Int, name: String) {
        dao.updateAssignedToName(id, name)
    }

    override suspend fun updateCreatedByToName(id: Int, name: String) {
        dao.updateCreatedByToName(id, name)
    }

    override suspend fun updateTasKProjectName(id: Int, name: String) {
        dao.updateTasKProjectName(id,name)
    }
}