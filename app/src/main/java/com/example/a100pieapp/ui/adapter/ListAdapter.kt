package com.example.a100pieapp.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.a100pieapp.R
import com.example.a100pieapp.data.model.Currencies
import kotlinx.android.synthetic.main.item_list.view.*

class ListAdapter(var context: Context, var currencies: List<Currencies>) :
    RecyclerView.Adapter<ListAdapter.ListViewModel>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewModel {
        return ListViewModel(
            LayoutInflater.from(context).inflate(
                R.layout.item_list,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ListViewModel, position: Int) {
        holder.currency?.text = currencies.get(position).currency
        holder.currencyLong?.text = currencies.get(position).currencyLong
        holder.txFee?.text = currencies.get(position).txFee
    }

    override fun getItemCount(): Int {
        return currencies.size
    }


    //private method
    fun addItem(item: List<Currencies>) {
        currencies = item
        notifyDataSetChanged()
    }

    inner class ListViewModel(view: View) : RecyclerView.ViewHolder(view) {
        var currency = view.currency
        var currencyLong = view.currency_long
        var txFee = view.tx_fee

    }
}