package com.example.chefling.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.library.baseAdapters.BR
import androidx.recyclerview.widget.RecyclerView
import com.example.chefling.databinding.HourlyAdapterBinding
import com.example.myapiservicesmodule.di.models.Hourly

class HourlyAdapter(
    val context: Context,
    private val itemList: List<Hourly>,
) : RecyclerView.Adapter<HourlyAdapter.Myhandler>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Myhandler {
        val hourlyAdapterBinding =
            HourlyAdapterBinding.inflate(LayoutInflater.from(context), parent, false)
        return Myhandler(hourlyAdapterBinding)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: Myhandler, position: Int) {

        holder.bind(itemList.get(position))
    }

    class Myhandler(val binding: HourlyAdapterBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(hourly: Hourly) {
            binding.hourlyData = hourly
            binding.setVariable(BR.hourly_data, hourly)
            binding.executePendingBindings()
        }
    }
}