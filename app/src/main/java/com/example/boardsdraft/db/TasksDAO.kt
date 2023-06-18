package com.example.boardsdraft.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.boardsdraft.db.entities.Task
import com.example.boardsdraft.db.entities.relations.ProjectWithTasks
import com.example.boardsdraft.db.entities.relations.UserWithTasks

@Dao
interface TasksDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: Task)

    @Update
    suspend fun updateTask(task: Task)

    @Transaction
    @Query("SELECT * FROM PROJECTS WHERE projectID = :projectID")
    fun getTaskOfProject(projectID: Int) : LiveData<List<ProjectWithTasks>?>

    @Transaction
    @Query("SELECT * FROM USERS WHERE userID = :userID")
    fun getTaskOfUser(userID : Int) : LiveData<List<UserWithTasks>?>

    @Query("SELECT taskID FROM Tasks ORDER BY taskID DESC LIMIT 1")
    fun getLastTaskID(): LiveData<Int?>
    @Query("UPDATE Tasks SET status = :newStatus WHERE status = :oldStatus")
    suspend fun updateTaskStatus(oldStatus: String, newStatus: String)

    @Query("SELECT * FROM Tasks WHERE taskID = :taskID")
    suspend fun getTaskByID(taskID:Int): Task
}