package com.kfouri.cryptoprice2.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.kfouri.cryptoprice2.CurrencyJson
import com.kfouri.cryptoprice2.R
import com.kfouri.cryptoprice2.domain.model.Currency
import java.util.Locale
import kotlinx.android.synthetic.main.currency_json_item.view.textView_currencySymbol
import kotlinx.android.synthetic.main.currency_json_item.view.textView_currencyName

class FindAdapter(private val clickListener: (String) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>(),
    Filterable {

    private var list = ArrayList<CurrencyJson>()
    private var fullList = ArrayList<CurrencyJson>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.currency_json_item, parent, false)
        )
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

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(item: CurrencyJson, clickListener: (String) -> Unit) {
            itemView.textView_currencySymbol.text = item.symbol
            itemView.textView_currencyName.text = item.name
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
                    if (it.symbol.toLowerCase(Locale.getDefault()).contains(constraint.toString().toLowerCase(
                            Locale.getDefault()
                        ).trim()
                        )
                        || it.name.toLowerCase(Locale.getDefault()).contains(constraint.toString().toLowerCase(
                            Locale.getDefault()
                        ).trim()
                        )
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