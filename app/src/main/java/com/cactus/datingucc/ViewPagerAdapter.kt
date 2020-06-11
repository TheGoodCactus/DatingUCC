package com.cianmc.viewpager2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cactus.datingucc.R
import com.cactus.datingucc.User
import kotlinx.android.synthetic.main.home_layout_item.view.*

class ViewPagerAdapter (val user: List<User>) : RecyclerView.Adapter<ViewPagerAdapter.ViewPagerViewHolder>() {
    inner class ViewPagerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.home_layout_item, parent, false)
        return ViewPagerViewHolder(view)
    }

    override fun getItemCount(): Int {
        return user.size
    }

    override fun onBindViewHolder(holder: ViewPagerViewHolder, position: Int) {
        val usr = user[position]
        Glide.with(holder.itemView.getContext())
            .load(usr.Profileimguri)
            .centerCrop()
            .placeholder(R.drawable.applelogo)
            .skipMemoryCache(true)
            .into(holder.itemView.ivImage)
        holder.itemView.tvBio.text = usr.bio
        holder.itemView.tvName.text = usr.name
    }
}