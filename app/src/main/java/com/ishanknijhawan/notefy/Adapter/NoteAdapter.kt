package com.ishanknijhawan.notefy.Adapter

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.ishanknijhawan.notefy.Entity.Note
import com.ishanknijhawan.notefy.R
import com.ishanknijhawan.notefy.ui.TextNoteActivity
import kotlinx.android.synthetic.main.activity_text_note.view.*
import kotlinx.android.synthetic.main.item_note_layout.view.*


class NoteAdapter(var items: List<Note>, val context: Context)
    : RecyclerView.Adapter<ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_note_layout,parent,false))
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val mDrawable = ContextCompat.getDrawable(context, R.drawable.white_oval_background)
        mDrawable?.colorFilter = PorterDuffColorFilter(items[position].color,PorterDuff.Mode.MULTIPLY)

        holder.itemNoteLayout.background = mDrawable
        holder.tvTitleView.text = items[position].title
        holder.tvNoteView.text = items[position].description
        holder.itemNoteLayout.cardElevation = 0F

        holder.cardCheckList.layoutManager = LinearLayoutManager(this.context)
        holder.cardCheckList.adapter = CardListAdapter(items[position].checkList, this.context)

        if (items[position].color != -1)
            holder.itemNoteLayout.strokeColor = items[position].color
        else
            holder.itemNoteLayout.strokeColor = Color.parseColor("#DCDCDC")

        if (holder.tvTitleView.text.isEmpty()){
            holder.tvTitleView.visibility = View.GONE
        }

        if (items[position].checkList.size == 0){
            holder.cardCheckList.visibility = View.GONE
        }

        if (holder.tvNoteView.text.isEmpty()){
            holder.tvNoteView.visibility = View.GONE
        }


        if (!items[position].deleted){
            holder.itemNoteLayout.setOnClickListener {

                val intent = Intent(context,TextNoteActivity::class.java)
                intent.putExtra("REQUEST_CODE","opened_from_main_activity")
                intent.putExtra("INTENT_TITLE",holder.tvTitleView.text.toString())
                intent.putExtra("INTENT_NOTE",holder.tvNoteView.text.toString())
                intent.putExtra("INTENT_NOTE_ID",items[position].id)
                intent.putExtra("INTENT_COLOR",items[position].color)
                intent.putExtra("CARD_SIZE",items[position].checkList.size)

                for(i in 0 until items[position].checkList.size){
                    intent.putExtra(i.toString(),items[position].checkList[i].inputName)
                    if (i == 0)
                        intent.putExtra("FIRST_CHECK",items[position].checkList[i].inputCheck)
                    else
                    intent.putExtra((-i).toString(),items[position].checkList[i].inputCheck)
                }

                if (items[position].pinned)
                    intent.putExtra("BOOL","true")
                else
                    intent.putExtra("BOOL","false")

                if (items[position].archive)
                    intent.putExtra("ARC","true")
                else
                    intent.putExtra("ARC","false")

                context.startActivity(intent)
            }

        }
    }
}

class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    val tvTitleView: TextView =itemView.tv_title
    val tvNoteView:TextView = itemView.tv_note
    val itemNoteLayout: MaterialCardView = itemView.note_layout_cardview
    val cardCheckList = itemView.rv_card_list
}