package com.jobsity.tvseries.presentation.people.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.jobsity.tvseries.R
import com.jobsity.tvseries.domain.model.Person
import com.jobsity.tvseries.presentation.people.list.PeopleAdapter.PeopleViewHolder
import com.jobsity.tvseries.util.GenericDiffCallback
import com.jobsity.tvseries.util.extension.loadRoundPhoto
import kotlinx.android.synthetic.main.person_row.view.*

class PeopleAdapter(private val listener: IPeopleAdapter): RecyclerView.Adapter<PeopleViewHolder>() {
    var people = emptyList<Person>()
        @Synchronized set(value) {
            val result = DiffUtil.calculateDiff(
                GenericDiffCallback(
                    field,
                    value
                ) { oldPerson: Person, newPerson: Person ->
                    oldPerson == newPerson
                }
            )
            result.dispatchUpdatesTo(this)
            field = value
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PeopleViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.person_row, parent, false)
        return PeopleViewHolder(view)
    }

    override fun getItemCount(): Int {
        return people.size
    }

    override fun onBindViewHolder(holder: PeopleViewHolder, position: Int) {
        holder.bind(people[position])
    }

    inner class PeopleViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val imgvPerson = itemView.imgvPerson
        val txvPerson = itemView.txvPerson

        fun bind(person: Person) {
            imgvPerson.loadRoundPhoto(person.mediumImage)
            txvPerson.text = person.name

            itemView.setOnClickListener {
                listener.onClick(person)
            }
        }
    }

    interface IPeopleAdapter {
        fun onClick(person: Person)
    }
}