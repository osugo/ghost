package com.android.dreams.data.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.android.dreams.R
import com.android.dreams.data.model.Dream
import io.realm.RealmList
import org.jetbrains.anko.find

/**
 * Created by kombo on 27/06/2018.
 */
class DreamsAdapter(private val context: Context, private val dreams: RealmList<Dream>) : RecyclerView.Adapter<DreamsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.dream_layout, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = dreams.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(context, dreams[holder.adapterPosition]!!)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(context: Context, dream: Dream) {
            val desc = itemView.find<TextView>(R.id.dreamDescription)
            val label = itemView.find<TextView>(R.id.tag)
            val period = itemView.find<TextView>(R.id.date)

            desc.text = dream.description
            label.text = dream.tag
            period.text = dream.date
        }
    }
}