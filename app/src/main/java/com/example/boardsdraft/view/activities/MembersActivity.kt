package com.example.boardsdraft.view.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.boardsDraft.R
import com.example.boardsDraft.databinding.ActivityMemebersBinding
import com.example.boardsdraft.view.adapter.MembersListAdapter
import com.example.boardsdraft.view.fragments.InputBottomSheetFragment
import com.example.boardsdraft.view.fragments.ShowProfileFragment
import com.example.boardsdraft.view.viewModel.MembersViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MembersActivity : AppCompatActivity(), InputBottomSheetFragment.OnItemClickListener, MembersListAdapter.OnItemClickListener {

    private lateinit var binding: ActivityMemebersBinding
    private lateinit var membersListAdapter: MembersListAdapter
    private val viewModel: MembersViewModel by viewModels()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getAllUsersOfProject(intent.getIntExtra("projectID",0))
        binding = ActivityMemebersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.membersToolbar.apply {
            title = "Members"
            setNavigationOnClickListener {
                finish()
            }
        }

        binding.rvMembersList.layoutManager =LinearLayoutManager(this@MembersActivity)
        membersListAdapter = MembersListAdapter(this@MembersActivity)
        binding.rvMembersList.adapter = membersListAdapter

        binding.fab.apply {
            setOnClickListener {
                InputBottomSheetFragment("Enter The Email ID", "Invite","Email",
                    this@MembersActivity).show(supportFragmentManager,"BottomFrag")
            }

        }
        viewModel.membersOfProject.observe(this@MembersActivity, Observer {
            viewModel.membersOfProject.value?.let { it1 ->
                membersListAdapter.setUsers(it1).apply {
                    membersListAdapter.setCurrentUser(viewModel.getCurrentUserID())
                    membersListAdapter.notifyDataSetChanged()
                }

            }
        })
    }

    override fun action(value: String) {
        val emailAddresses = arrayOf(value)
        val emailIntent= Intent(Intent.ACTION_SENDTO)
        emailIntent.apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL,emailAddresses)
            putExtra(Intent.EXTRA_SUBJECT,"Invitation to Work Together")
            putExtra(
                Intent.EXTRA_TEXT,"I have created a Trello board to " +
                    "streamline our project management process, and I think your expertise and input would be valuable. The board code to join is ${intent.getStringExtra("projectCode")} in the Project Board App.")
            if(emailIntent.resolveActivity(packageManager) != null){
                startActivity(Intent.createChooser(emailIntent,"Choose An App"))
            }
        }

    }

    override fun onItemClick(userID: Int) {
        val bundle = Bundle()
        val showProfile = ShowProfileFragment("NO")
        bundle.putInt("userID",userID)

        showProfile.arguments = bundle

        if(userID != viewModel.getCurrentUserID()){
            supportFragmentManager.beginTransaction()
                .replace(R.id.members_layout,showProfile)
                .addToBackStack(null)
                .commit()
        }

    }


}