package com.example.boardsdraft.view.adapter

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.boardsDraft.R
import com.example.boardsdraft.db.entities.relations.ProjectsWithUsers
import com.google.android.material.card.MaterialCardView

class BoardsAdapter(private val clickListener: OnItemClickListener): RecyclerView.Adapter<BoardsAdapter.BoardsViewHolder>() {

    private val boards = ArrayList<ProjectsWithUsers>()

    inner class BoardsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val card: MaterialCardView = itemView.findViewById(R.id.card_view)
        private val boardName: TextView = itemView.findViewById(R.id.cd_board_name)
        private val boardImage : ImageView = itemView.findViewById(R.id.cd_board_image)
        val createdBy : TextView = itemView.findViewById(R.id.cd_board_creator_name)

        fun bind(project: ProjectsWithUsers){
            boardName.text = project.project.projectName
            createdBy.text = project.project.createdBy
            project.project.image?.let {
                boardImage.setImageBitmap(BitmapFactory.decodeByteArray(project.project.image, 0, it.size))
            }
        }

    }
    fun setBoards(boards : List<ProjectsWithUsers>){
        this.boards.clear()
        this.boards.addAll(boards)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BoardsViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.card_design, parent, false)
        return BoardsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return boards.size
    }

    override fun onBindViewHolder(holder: BoardsViewHolder, position: Int) {
        holder.bind(boards[position])

        holder.card.apply {
            setOnClickListener {
                clickListener.onItemClick(boards[position].project.projectName, boards[position].project.projectID,boards[position].project.projectCode,boards[position].project.createdByID)
            }
        }
    }
    interface OnItemClickListener {
        fun onItemClick(projectName: String, projectID: Int,projectCode:String,projectCreatedBy:Int)
    }

}