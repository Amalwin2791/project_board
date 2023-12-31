package com.example.boardsdraft.view.adapter

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.boardsDraft.R
import com.example.boardsdraft.db.entities.Task
import com.example.boardsdraft.db.entities.TaskTitles
import com.example.boardsdraft.db.entities.User
import com.example.boardsdraft.db.entities.relations.ProjectWithTasks
import com.example.boardsdraft.db.entities.relations.TaskTitlesOfProject
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textfield.TextInputLayout

class TaskListAdapter(
    private val clickListener: OnItemClickListener,
    private val taskTitleID: Int,
    private val projectID: Int,
) : RecyclerView.Adapter<TaskListAdapter.TasksViewHolder>(), CardsListAdapter.OnItemClickListener {


    private var taskList = listOf<ProjectWithTasks>()
    private val taskTitlesList = ArrayList<TaskTitlesOfProject>()
    private var members = listOf<User>()
    private var currentUserID: Int = -1
    private var taskTitlesListSize = 0
    private lateinit var cardsAdapter: CardsListAdapter


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TasksViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_task_card, parent, false)
        val layoutParameters = LinearLayout.LayoutParams(
            (parent.width * 0.65).toInt(), LinearLayout.LayoutParams.WRAP_CONTENT
        )
        layoutParameters.setMargins((15.toDP().toPx()), 0, (40.toDP().toPx()), 0)
        view.layoutParams = layoutParameters
        return TasksViewHolder(view)
    }

    override fun getItemCount(): Int {
        return taskTitlesListSize
    }

    fun setTaskList(tasks: List<ProjectWithTasks>) {
        this.taskList = tasks
    }

    fun setCurrentUser(id: Int) {
        currentUserID = id
    }

    fun setMembers(members: List<User>) {
        this.members = members
    }

    fun setTaskTitlesList(taskTitles: List<TaskTitlesOfProject>) {
        this.taskTitlesList.clear()
        val taskTitlesOfProject = taskTitles[0]
        val projectTaskTitles = taskTitlesOfProject.taskTitles.toMutableList()
        projectTaskTitles.add(TaskTitles(0, "Add List", taskTitlesOfProject.project.projectID))
        val updatedTaskTitlesOfProject =
            TaskTitlesOfProject(taskTitlesOfProject.project, projectTaskTitles)
        this.taskTitlesList.add(updatedTaskTitlesOfProject)
        taskTitlesListSize = taskTitlesList[0].taskTitles.size
    }


    override fun onBindViewHolder(holder: TasksViewHolder, position: Int) {
        if (taskTitlesList.size > 0) {
            holder.bindTitle(taskTitlesList[0].taskTitles[position])

            holder.apply {


                addTaskButton.visibility = View.GONE
                taskListName.visibility = View.GONE
                tasksLayout.visibility = View.VISIBLE

                if (position == taskTitlesList[0].taskTitles.size - 1) {
                    addTaskButton.visibility = View.VISIBLE
                    tasksLayout.visibility = View.GONE
                } else {
                    addTaskButton.visibility = View.GONE
                    tasksLayout.visibility = View.VISIBLE
                    editTitleLayout.visibility = View.VISIBLE
                    editTitleCardView.visibility = View.GONE
                }


                if (taskList[0].project.createdByID != currentUserID) {
                    deleteList.visibility = View.GONE
                }

                addTaskButton.setOnClickListener {
                    addTaskButton.visibility = View.GONE
                    taskListName.visibility = View.VISIBLE

                }
                closeListName.setOnClickListener {
                    addTaskButton.visibility = View.VISIBLE
                    taskListName.visibility = View.GONE
                    newTaskListName.text.clear()
                }
                doneListName.setOnClickListener {
                    if (newTaskListName.text.isNullOrBlank()) {
                        newTaskListName.error = "Task List Title Cannot Be empty"
                    } else {

                        addTaskButton.visibility = View.GONE
                        taskListName.visibility = View.GONE
                        clickListener.insertTaskTitle(
                            TaskTitles(
                                taskTitleID = taskTitleID,
                                taskTitle = newTaskListName.text.toString().trim(),
                                projectID = projectID
                            )
                        )
                        newTaskListName.text.clear()
                    }
                }

                editListName.setOnClickListener {
                    editTaskListName.setText(taskTitlesList[0].taskTitles[position]?.taskTitle)
                    editTitleLayout.visibility = View.GONE
                    editTitleCardView.visibility = View.VISIBLE
                }

                closeEditTitle.setOnClickListener {
                    editTitleLayout.visibility = View.VISIBLE
                    editTitleCardView.visibility = View.GONE
                }

                titleEditDone.setOnClickListener {
                    if (editTaskListName.text.isNullOrBlank()) {
                        editTaskListName.error = " Task Title Cannot be null"
                    } else {
                        val existingTitle = taskTitlesList[0].taskTitles[position]
                        val oldTitle = existingTitle?.taskTitle
                        existingTitle?.taskTitle = editTaskListName.text.toString().trim()
                        if (existingTitle != null) {
                            clickListener.updateTaskTitle(existingTitle, oldTitle)
                        }
                        editTitleCardView.visibility = View.GONE
                        editTitleLayout.visibility = View.VISIBLE
                        editTaskListName.text.clear()
                    }
                }
                deleteList.setOnClickListener {
                    taskTitlesList[0].taskTitles[position]?.let { it1 ->
                        clickListener.deleteTaskTitle(it1)
                    }
                    newTaskListName.text.clear()
                }
                addCardText.setOnClickListener {
                    clickListener.createNewTask(taskTitlesList[0].taskTitles[position]!!.taskTitle)
                }
                if (taskList[0].tasks.isNotEmpty()) {
                    cardsList.apply {
                        layoutManager = LinearLayoutManager(context)
                        setHasFixedSize(false)
                        cardsAdapter = CardsListAdapter(this@TaskListAdapter)
                        adapter = cardsAdapter
                        setCards(filterTasksByStatus(taskTitlesList[0].taskTitles[position]!!.taskTitle))
                        setMembersForCards(members)


                        cardsAdapter.notifyDataSetChanged()

                        taskCard.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT

                    }
                }

                newTaskListName.setOnFocusChangeListener { _, hasFocus ->
                    if (hasFocus) {
                        newTaskListNameLayout.error = null
                    }
                }

                editTaskListName.setOnFocusChangeListener { _, hasFocus ->
                    if (hasFocus) {
                        editTaskListNameLayout.error = null
                    }
                    if (!hasFocus) {
                        editTitleCardView.visibility = View.GONE
                        editTaskListName.text.clear()
                    }

                }
            }
        }
    }


    private fun setCards(tasks: List<Task>) {
        cardsAdapter.setCardsList(tasks)
    }

    private fun setMembersForCards(members: List<User>) {
        cardsAdapter.setMembers(members)
    }


    private fun filterTasksByStatus(status: String): List<Task> {
        val filteredTasks = mutableListOf<Task>()
        for (projectWithTasks in taskList) {
            val tasks = projectWithTasks.tasks.filter { it?.status == status }
            filteredTasks.addAll(tasks.filterNotNull())
        }
        return filteredTasks
    }


    private fun Int.toDP(): Int = (this / Resources.getSystem().displayMetrics.density).toInt()

    private fun Int.toPx(): Int = (this * Resources.getSystem().displayMetrics.density).toInt()
    inner class TasksViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val addTaskButton: TextView = view.findViewById(R.id.tv_add_task_list)
        val tasksLayout: LinearLayout = view.findViewById(R.id.ll_task_item)
        private val taskListTitle: TextView = view.findViewById(R.id.tv_task_list_title)
        val taskListName: CardView = view.findViewById(R.id.cv_add_task_list_name)
        val closeListName: ImageButton = view.findViewById(R.id.ib_close_list_name)
        val doneListName: ImageButton = view.findViewById(R.id.ib_done_list_name)
        val newTaskListName: EditText = view.findViewById(R.id.et_task_list_name)
        val newTaskListNameLayout: TextInputLayout = view.findViewById(R.id.tl_task_list_name)
        val editListName: ImageButton = view.findViewById(R.id.ib_edit_list_name)
        val editTitleLayout: LinearLayout = view.findViewById(R.id.ll_title_view)
        val editTaskListName: EditText = view.findViewById(R.id.et_edit_task_list_name)
        val editTaskListNameLayout: TextInputLayout = view.findViewById(R.id.tl_edit_task_list_name)
        val editTitleCardView: CardView = view.findViewById(R.id.cv_edit_task_list_name)
        val closeEditTitle: ImageButton = view.findViewById(R.id.ib_close_editable_view)
        val titleEditDone: ImageButton = view.findViewById(R.id.ib_done_edit_list_name)
        val deleteList: ImageButton = view.findViewById(R.id.ib_delete_list)
        val addCardText: TextView = view.findViewById(R.id.tv_add_card)
        val cardsList: RecyclerView = view.findViewById(R.id.rv_card_list)
        val taskCard: MaterialCardView = view.findViewById(R.id.task_card_layout)
        fun bindTitle(taskTitle: TaskTitles?) {
            taskListTitle.text = taskTitle!!.taskTitle
        }
    }

    interface OnItemClickListener {
        fun deleteTaskTitle(taskTitle: TaskTitles)
        fun insertTaskTitle(taskTitle: TaskTitles)
        fun updateTaskTitle(taskTitle: TaskTitles, oldTitle: String?)
        fun createNewTask(taskTitle: String)

        fun showTask(task: Task)
    }

    override fun onItemClick(task: Task) {
        clickListener.showTask(task)
    }


}


