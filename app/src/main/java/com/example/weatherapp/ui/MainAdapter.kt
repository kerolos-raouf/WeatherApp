package com.example.weatherapp.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.weatherapp.R
import com.example.weatherapp.data.models.WeatherItem
import com.example.weatherapp.databinding.WeatherItemBinding

class MainAdapter(
    private var itemList : List<WeatherItem>,
    private val itemInteractionListener : ItemInteractionListener
) : RecyclerView.Adapter<MainAdapter.ItemViewHolder>(){

    class ItemViewHolder(view : View) : RecyclerView.ViewHolder(view){
        val binding = WeatherItemBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.weather_item,parent,false)
        return ItemViewHolder(view)
    }

    override fun getItemCount(): Int = itemList.size


    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val currentItem = itemList[position]
        holder.binding.item = currentItem
        holder.binding.listener = itemInteractionListener
    }

    fun setList(newList : List<WeatherItem>)
    {
        itemList = newList
        notifyDataSetChanged()
    }
}
interface ItemInteractionListener{
    fun onItemClicked(item : WeatherItem)
}