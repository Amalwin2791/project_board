package com.example.boardsdraft.view.adapter

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.boardsDraft.R
import com.example.boardsdraft.db.entities.Task
import com.example.boardsdraft.db.entities.User
import com.google.android.material.card.MaterialCardView
import kotlinx.coroutines.runBlocking

class CardsListAdapter(private val clickListener: OnItemClickListener) :
    RecyclerView.Adapter<CardsListAdapter.CardsViewHolder>() {

    private val tasksList = ArrayList<Task>()

    private val members = ArrayList<User>()

    fun setCardsList(tasks: List<Task>) {
        this.tasksList.clear()
        this.tasksList.addAll(tasks)
    }

        fun setMembers(members:List<User>){
        this.members.clear()
        this.members.addAll(members)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardsViewHolder {
        return CardsViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_card, parent, false)
        )
    }

    override fun onBindViewHolder(holder: CardsViewHolder, position: Int) {

        if (tasksList.isNotEmpty()) {
            holder.bind(tasksList[position])
            holder.card.setOnClickListener {
                clickListener.onItemClick(tasksList[position])
            }

        }

    }


    override fun getItemCount(): Int {
        return tasksList.size
    }


    private fun getAssignedUser(userID: Int): User? {
        val index = members.binarySearchBy(userID) { it.userID }
        return if (index >= 0) members[index] else null
    }


    inner class CardsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val cardName: TextView = view.findViewById(R.id.tv_card_name)
        private val image: ImageView = view.findViewById(R.id.assigned_user_image)
        val card: MaterialCardView = view.findViewById(R.id.task_card)

        fun bind(task: Task) {
//            val assignedMember = getAssignedUser(task.assignedTo)
            cardName.text = task.taskName
            runBlocking{
                getAssignedUser(task.assignedTo)?.image?.let { img ->

                    image.setImageBitmap(BitmapFactory.decodeByteArray(img, 0, img.size))

                }
            }
//            assignedMember?.image?.let {
//                image.setImageBitmap(BitmapFactory.decodeByteArray(assignedMember.image,0,it.size))
//            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(task: Task)
    }
}