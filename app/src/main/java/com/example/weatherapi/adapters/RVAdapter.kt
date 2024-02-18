package com.example.weatherapi.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapi.data.Forecastday
import com.example.weatherapi.data.Hour
import com.example.weatherapi.R
import com.example.weatherapi.databinding.ItemBinding
import com.example.weatherapi.utils.getDate
import com.example.weatherapi.utils.getHour
import com.example.weatherapi.utils.tempToInt
import com.squareup.picasso.Picasso

class RVAdapter(val context: Context, val tag:String, val listener: Listener?):RecyclerView.Adapter<RVAdapter.WeatherForecastdayHolder>(){

    lateinit var binding: ItemBinding
    var forecastdayList:List<Forecastday> = listOf<Forecastday>()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    var hoursList:List<Hour> = arrayListOf<Hour>()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherForecastdayHolder {
        binding = ItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)

        return WeatherForecastdayHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: WeatherForecastdayHolder, position: Int) {
        if (tag=="f1") {//DaysFragment
            val forecastday = forecastdayList.get(position)
            with(holder) {
                tvTemp.text = String.format(
                    context.resources.getString(R.string.temp_template),
                    forecastday.day?.maxtemp_c?.let { tempToInt(it) }
                )
                tvTime.text = getDate(forecastday.date.toString())
                Picasso.get().load("https:${forecastday.day?.condition?.icon}").into(imWeather)

                itemView.setOnClickListener { listener?.onForecastdayClick(forecastday) }
            }
        }
        else {//HoursFragment

            val hour = hoursList.get(position)
            with(holder) {
                tvTemp.text = String.format(
                    context.resources.getString(R.string.temp_template),
                    tempToInt( hour.temp_c)
                )
                tvDate.text = getDate(hour.time.toString(), tag)
                tvTime.text = getHour( hour.time.toString())
                Picasso.get().load("https:${hour.condition?.icon}").into(imWeather)
            }
        }

    }

    override fun getItemCount(): Int {
        return if(tag == "f1") forecastdayList.size
        else hoursList.size
    }

    inner class WeatherForecastdayHolder(binding: ItemBinding) : RecyclerView.ViewHolder(binding.root) {

        val tvTemp = binding.tvTemp
        val imWeather = binding.imVWeather
        val tvTime = binding.tvTime
        val tvDate = binding.tvDate

    }

    interface Listener{
        fun onForecastdayClick(forecastday: Forecastday)
    }

}