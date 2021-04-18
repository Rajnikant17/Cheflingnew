package com.example.chefling.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.library.baseAdapters.BR
import androidx.recyclerview.widget.RecyclerView
import com.example.chefling.databinding.DailyAdapterBinding
import com.example.myapiservicesmodule.di.models.Daily

class DailyAdapter(
    val context: Context,
    private val itemList: List<Daily>,
) : RecyclerView.Adapter<DailyAdapter.Myhandler>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Myhandler {
        val dailyAdapterBinding =
            DailyAdapterBinding.inflate(LayoutInflater.from(context), parent, false)
        return Myhandler(dailyAdapterBinding)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: Myhandler, position: Int) {

        holder.bind(itemList.get(position))
    }

    class Myhandler(val binding: DailyAdapterBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(daily: Daily) {
            binding.dailyData = daily
            binding.setVariable(BR.daily_data, daily)
            binding.executePendingBindings()
        }
    }

}