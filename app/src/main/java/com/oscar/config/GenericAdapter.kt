package com.oscar.config

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.oscar.R

interface OnGenericAdapterClickListener<T> {
    fun onAdapterClick(t: T)
}

interface SetViewData<T> {
    fun setViewData(title: TextView,
                    genre: TextView,
                    image: ImageView,
                    t: T)

}
open class GenericAdapter <T> (  private val list: List<T>,
                                 private val context: Context,
                                 private val clickListener: OnGenericAdapterClickListener<T>,
                                 private val setViewData: SetViewData<T>
): RecyclerView.Adapter<GenericAdapter<T>.GenericViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): GenericViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.recycler_view_pictures, parent, false)
        return GenericViewHolder(view)
    }


    override fun onBindViewHolder(
        holder: GenericViewHolder,
        position: Int
    ) {

        val item = list[position]
        setViewData.setViewData(holder.title, holder.genre, holder.image, item)

        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class GenericViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title = itemView.findViewById<TextView>(R.id.tvMovieName)
        val genre = itemView.findViewById<TextView>(R.id.tvMovieGenre)
        val image = itemView.findViewById<ImageView>(R.id.ivMoviePoster)

        fun bind(t: T) {
            itemView.setOnClickListener {
                clickListener.onAdapterClick(t)
            }
        }
    }
}