package com.kfouri.cryptoprice.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.kfouri.cryptoprice.CurrencyJson
import com.kfouri.cryptoprice.databinding.CurrencyJsonItemBinding
import java.util.Locale

class FindAdapter(private val clickListener: (String) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>(),
    Filterable {

    private var list = ArrayList<CurrencyJson>()
    private var fullList = ArrayList<CurrencyJson>()
    private lateinit var binding: CurrencyJsonItemBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        binding = CurrencyJsonItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = list[position]
        (holder as ViewHolder).bind(item, clickListener)
    }

    fun setData(newList: List<CurrencyJson>) {
        list.clear()
        list.addAll(newList)
        fullList = ArrayList(list)
        notifyDataSetChanged()
    }

    class ViewHolder(private val binding: CurrencyJsonItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CurrencyJson, clickListener: (String) -> Unit) {
            binding.textViewCurrencySymbol.text = item.symbol
            binding.textViewCurrencyName.text = item.name
            itemView.setOnClickListener { clickListener(item.id+"#|#"+item.name+"#|#"+item.symbol+"#|#") }
        }
    }

    override fun getFilter(): Filter {
        return itemFilter
    }

    private val itemFilter = object: Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val filterList = ArrayList<Any>()
            if (!constraint.isNullOrEmpty()) {
                fullList.forEach {
                    if (it.symbol.lowercase().contains(constraint.toString().lowercase().trim())
                        ||
                        it.name.lowercase().contains(constraint.toString().lowercase().trim())
                    ) {
                        filterList.add(it)
                    }
                }
            } else {
                filterList.addAll(list)
            }

            val result = FilterResults()
            result.values = filterList

            return result
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            list.clear()
            list.addAll(results?.values as ArrayList<CurrencyJson>)
            notifyDataSetChanged()
        }
    }
}