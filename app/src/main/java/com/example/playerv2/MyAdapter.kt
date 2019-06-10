package com.example.playerv2

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class MyAdapter(var context: Context ,private var songs: ArrayList<Song>): RecyclerView.Adapter<MyAdapter.DataHolder>() {

    override fun getItemCount(): Int = songs.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val cell = layoutInflater.inflate(R.layout.data_view, parent, false)
        return DataHolder(cell)
    }

    override fun onBindViewHolder(holder: DataHolder, position: Int) {
        holder.title.text = songs[position].name
        holder.band.text = songs[position].band

        onClickListener(holder, position)
    }

    private fun onClickListener(holder: DataHolder, position: Int) {
        holder.itemView.setOnClickListener {
            //Toast.makeText(context, position.toString(), Toast.LENGTH_SHORT).show()
            (context as MainActivity).playSong(songs[position], position)
        }
    }

    class DataHolder(v: View) : RecyclerView.ViewHolder(v) {
        val title: TextView = itemView.findViewById(R.id.songTitle)
        val band: TextView = itemView.findViewById(R.id.bandName)
    }
}