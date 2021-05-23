package com.example.wikkipiedia

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class NewsAdapter(private val listener : NewsItemClicked,private val longListener : NewsItemShare) : RecyclerView.Adapter<NewsViewHolder>(){

    private val items: ArrayList<News> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder{
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_news,parent,false)
        val viewHolder = NewsViewHolder(view)
        view.setOnClickListener{
            listener.onItemClicked(items[viewHolder.adapterPosition])
        }
        view.setOnLongClickListener{
            longListener.onLongClicked(items[viewHolder.adapterPosition])
            true
        }
        return viewHolder
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder : NewsViewHolder, position: Int) {
        holder.titleView.text = items[position].title
        holder.authorView.text = items[position].author
        Glide.with(holder.itemView.context).load(items[position].imageUrl).into(holder.imageView)
    }

    fun updateNews(updatedNews: ArrayList<News>){
        items.clear()
        items.addAll(updatedNews)

        notifyDataSetChanged()
    }
}

class NewsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
    var titleView : TextView = itemView.findViewById(R.id.titleView)
    var authorView : TextView = itemView.findViewById(R.id.authorView)
    var imageView : ImageView = itemView.findViewById(R.id.imageView)
}

interface NewsItemClicked{
    fun onItemClicked(item: News)
}
interface NewsItemShare{
    fun onLongClicked(item: News)
}