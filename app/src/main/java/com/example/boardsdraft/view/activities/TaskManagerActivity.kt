package com.example.boardsdraft.view.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.example.boardsDraft.R
import com.example.boardsDraft.databinding.ActivityTaskManagerBinding
import com.example.boardsdraft.db.entities.Task
import com.example.boardsdraft.view.fragments.EditTaskDetailsFragment
import com.example.boardsdraft.view.fragments.TaskInfoFragment
import com.example.boardsdraft.view.viewModel.MyTasksViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TaskManagerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTaskManagerBinding
    private val viewModel: MyTasksViewModel by viewModels()
    private lateinit var task: Task

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getTaskByID(intent.getIntExtra("taskID",0))
        binding = ActivityTaskManagerBinding.inflate(layoutInflater)
        setContentView(binding.root)



        viewModel.task.observe(this, Observer {
            task= it
            supportFragmentManager.beginTransaction()
                .replace(R.id.task_manager_fragment_container, TaskInfoFragment(it))
                .addToBackStack("TaskInfoFragment")
                .commit()
        })


       binding.taskManagerToolbar.apply {
           title = intent.getStringExtra("taskName")

           inflateMenu(R.menu.profile_menu_item)

           setNavigationOnClickListener {
               val fragmentManager = supportFragmentManager
               val backStackEntryCount = fragmentManager.backStackEntryCount
               if (backStackEntryCount > 0){
                   val currentFragmentTag = fragmentManager.getBackStackEntryAt(backStackEntryCount - 1).name
                   val editTaskDetailsFragmentName = EditTaskDetailsFragment::class.java.simpleName
                   if (currentFragmentTag == editTaskDetailsFragmentName){
                       fragmentManager.popBackStack()
                   }else{
                       finish()
                   }
               }
               else{
                   finish()
               }
           }

           setOnMenuItemClickListener {
               when(it.itemId){
                   R.id.edit -> {
                       val editTaskDetailsFragment = EditTaskDetailsFragment(task)
                       supportFragmentManager.beginTransaction()
                           .replace(binding.taskManagerFragmentContainer.id,editTaskDetailsFragment)
                           .addToBackStack("EditTaskDetailsFragment").commit()
                   }
               }
               true
           }


       }

    }


}