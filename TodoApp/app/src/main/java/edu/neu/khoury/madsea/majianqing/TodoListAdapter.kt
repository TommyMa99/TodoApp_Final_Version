package edu.neu.khoury.madsea.majianqing

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import java.util.Collections.addAll

class TodoListAdapter(mainActivity: UpdateandDelete) :
    ListAdapter<TodoEntity, TodoListAdapter.WordViewHolder>(WordsComparator()) {
    private var updateandDelete: UpdateandDelete = mainActivity
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
        return WordViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        val current = getItem(position)
        holder.bindView(current.itemDataText)
        holder.bindStatus(current.done)
        holder.bindDescription(current.moreDetails)
        holder.options.setOnClickListener {
            updateandDelete.onOptionsMenuClicked(position, current)
        }
    }

    class WordViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val wordItemView: TextView = itemView.findViewById(R.id.item_textView)
        val options : ImageView = itemView.findViewById(R.id.options)
        val checkItemView: CheckBox = itemView.findViewById(R.id.checkbox)
        val description: TextView = itemView.findViewById(R.id.description)
        private var updateandDelete: UpdateandDelete = itemView.context as UpdateandDelete

        fun bindView(text: String?) {
            wordItemView.text = text
        }

        fun bindStatus(done: Boolean){
            checkItemView.isChecked = done
        }

        fun bindDescription(detail : String?){
            description.text = detail
        }


        companion object {
            fun create(parent: ViewGroup): WordViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.recyclerview_item, parent, false)
                return WordViewHolder(view)
            }
        }
    }

    class WordsComparator : DiffUtil.ItemCallback<TodoEntity>() {
        override fun areItemsTheSame(oldItem: TodoEntity, newItem: TodoEntity): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: TodoEntity, newItem: TodoEntity): Boolean {
            return oldItem.done == newItem.done && oldItem.reminder == newItem.reminder &&
                    oldItem.deadline == newItem.deadline && oldItem.itemDataText == newItem.itemDataText
                    && oldItem.moreDetails == newItem.moreDetails && oldItem.tags == newItem.tags &&
                    oldItem.timeToRemind == newItem.timeToRemind
        }
    }
}