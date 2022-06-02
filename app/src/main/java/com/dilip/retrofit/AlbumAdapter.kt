package com.dilip.retrofit

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.dilip.retrofit.databinding.ListItemBinding
import com.dilip.retrofit.model.AlbumsItem

class AlbumAdapter(private val clickListener: (AlbumsItem) -> Unit) :   RecyclerView.Adapter<MyViewHolder>(){

    private val albumList = ArrayList<AlbumsItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: ListItemBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.list_item, parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return albumList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(albumList[position], clickListener)
    }

    fun setList(albumsItems: List<AlbumsItem>) {
        albumList.clear()
        albumList.addAll(albumsItems)
    }
}

class MyViewHolder(val binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(albumsItem: AlbumsItem, clickListener: (AlbumsItem) -> Unit) {
        binding.nameTextView.text = albumsItem.title
        binding.idTextView.text = albumsItem.id.toString()

        binding.listItemLayout.setOnClickListener {
            clickListener(albumsItem)
        }
    }
}