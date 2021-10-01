package com.kfouri.cryptoprice2.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.recyclerview.widget.RecyclerView
import com.kfouri.cryptoprice2.R
import com.kfouri.cryptoprice2.domain.model.Currency
import com.kfouri.cryptoprice2.ext.loadFromUrl
import kotlinx.android.synthetic.main.currency_item.view.*
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.NumberFormat
import kotlin.collections.ArrayList

class ListAdapter(private val clickListener: (Int) -> Unit,
                  private val refreshList: (Double, Double) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>(),
    Filterable {

    private var list = ArrayList<Currency>()
    private var fullList = ArrayList<Currency>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.currency_item, parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = list[position]
        (holder as ViewHolder).bind(item, clickListener)
        //holder.itemView.item_parent.animation = AnimationUtils.loadAnimation(holder.itemView.context, R.anim.list_item_animation)
    }

    fun setData(newList: ArrayList<Currency>) {
        list.clear()
        list.addAll(newList)
        fullList = ArrayList(list)
        notifyDataSetChanged()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(item: Currency, clickListener: (Int) -> Unit){
            val format: NumberFormat = NumberFormat.getCurrencyInstance()

            itemView.imageView_logo.loadFromUrl(item.icon)
            itemView.textView_currencySymbol.text = item.symbol
            itemView.textView_currencyExchange.text = item.exchange
            itemView.textView_currencyPrice.text = "$"+BigDecimal.valueOf(item.currentPrice).toPlainString()
            itemView.textView_currencyAmount.text = BigDecimal.valueOf(item.amount).toPlainString()

            if (item.currentPrice == 0.0) {
                itemView.textView_currencyPrice.text = "$0.00"
                itemView.textView_pricePercentage.text = "$0.00"
                itemView.linearLayout_pricePercentage.setBackgroundResource(R.drawable.percentage_background_green)
                itemView.imageView_arrowPrice.setImageDrawable(getDrawable(itemView.context, R.drawable.ic_arrow_up))

                itemView.textView_earn.text = "$0.00"
                itemView.textView_earnedPercentage.text = "$0.00"
                itemView.linearLayout_earnedPercentage.setBackgroundResource(R.drawable.percentage_background_green)
                itemView.imageView_arrowEarned.setImageDrawable(getDrawable(itemView.context, R.drawable.ic_arrow_up))

            } else {

                //val res = item.currentPrice * 100 / item.open24 - 100
                itemView.textView_pricePercentage.text = "${item.open24.toBigDecimal().setScale(2, RoundingMode.UP).toDouble()}% 1D"
                if (item.open24 < 0.0) {
                    itemView.linearLayout_pricePercentage.setBackgroundResource(R.drawable.percentage_background_red)
                    itemView.imageView_arrowPrice.setImageDrawable(getDrawable(itemView.context, R.drawable.ic_arrow_down))
                } else {
                    itemView.linearLayout_pricePercentage.setBackgroundResource(R.drawable.percentage_background_green)
                    itemView.imageView_arrowPrice.setImageDrawable(getDrawable(itemView.context, R.drawable.ic_arrow_up))
                }

                var earned = item.amount * item.currentPrice - item.amount * item.purchasePrice
                var have = item.amount * item.currentPrice
                itemView.textView_have.text = "$" + have.toBigDecimal().setScale(2, RoundingMode.UP).toDouble().toString()

                var earnedPercentage = 0.0

                if (item.purchasePrice != 0.0 && item.amount != 0.0) {
                    earnedPercentage = ((item.amount * item.currentPrice) * 100 / (item.amount * item.purchasePrice)) - 100
                }
                earned = if (earned >= 1 || earned <= -1) {
                    earned.toBigDecimal().setScale(2, RoundingMode.UP).toDouble()
                } else {
                    earned.toBigDecimal().setScale(8, RoundingMode.UP).toDouble()
                }

                earnedPercentage = if (earnedPercentage.toString() == "NaN") {
                    0.0
                } else {
                    earnedPercentage.toBigDecimal().setScale(2, RoundingMode.UP).toDouble()
                }
                itemView.textView_earnedPercentage.text = "${earnedPercentage}% ALL"

                itemView.textView_earn.text = "$" + earned
                if (item.purchasePrice > item.currentPrice) {
                    itemView.linearLayout_earnedPercentage.setBackgroundResource(R.drawable.percentage_background_red)
                    itemView.imageView_arrowEarned.setImageDrawable(getDrawable(itemView.context, R.drawable.ic_arrow_down))
                } else {
                    itemView.linearLayout_earnedPercentage.setBackgroundResource(R.drawable.percentage_background_green)
                    itemView.imageView_arrowEarned.setImageDrawable(getDrawable(itemView.context, R.drawable.ic_arrow_up))
                }
            }
            itemView.setOnClickListener { clickListener(item._id) }
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
                    if (it.symbol.toLowerCase().contains(constraint.toString().toLowerCase().trim())) {
                        filterList.add(it)
                    }
                }
            } else {
                filterList.addAll(fullList)
            }

            val result = FilterResults()
            result.values = filterList

            return result
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            list.clear()
            list.addAll(results?.values as ArrayList<Currency>)

            var balance = 0.0
            var totalInvested = 0.0

            list.forEach { item ->
                balance += item.currentPrice * item.amount
                totalInvested += item.purchasePrice * item.amount
            }
            
            refreshList(totalInvested, balance)
            notifyDataSetChanged()
        }
    }

}