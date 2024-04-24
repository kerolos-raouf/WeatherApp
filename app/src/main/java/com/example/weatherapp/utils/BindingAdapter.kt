package com.example.weatherapp.utils


import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weatherapp.R
import com.example.weatherapp.data.models.WeatherItem
import com.example.weatherapp.domain.networking.State
import com.example.weatherapp.domain.weather.WeatherType
import com.example.weatherapp.ui.MainAdapter

@BindingAdapter(value = ["setIcon"])
fun setIconWithCode(view: ImageView,code : Int?)
{
    code?.let {
        val weatherType = WeatherType.fromWC(code)
        Glide.with(view).load(weatherType.iconRes).placeholder(R.drawable.ic_sunny).into(view)
    }
}
@BindingAdapter(value = ["setTextWithCode"])
fun setTextWithCode(view: TextView,code : Int?)
{
    code?.let {
        val weatherType = WeatherType.fromWC(code)
        view.text = weatherType.weatherDesc
    }
}

@BindingAdapter(value = ["app:items"])
fun setItems(recyclerView: RecyclerView,items : List<WeatherItem>?)
{
    if(items != null)
        (recyclerView.adapter as MainAdapter?)?.setList(items)
    else
        (recyclerView.adapter as MainAdapter?)?.setList(emptyList())
}

@BindingAdapter(value = ["app:showProgressBar"])
fun  showProgressBar(view : View, show : Boolean){
    if(show){
        view.visibility = View.VISIBLE
    }
    else
    {
        view.visibility = View.GONE
    }
}