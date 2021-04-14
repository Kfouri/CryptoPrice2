package com.kfouri.cryptoprice2.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.kfouri.cryptoprice2.R
import com.kfouri.cryptoprice2.domain.model.Currency
import com.kfouri.cryptoprice2.ext.loadFromUrl
import kotlinx.android.synthetic.main.currency_item.view.*
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class ListAdapter(private val clickListener: (Int) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var list = ArrayList<Currency>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.currency_item, parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = list[position]
        (holder as ViewHolder).bind(item, clickListener)
        holder.itemView.item_parent.animation = AnimationUtils.loadAnimation(holder.itemView.context, R.anim.list_item_animation)
    }

    fun setData(newList: ArrayList<Currency>) {
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(item: Currency, clickListener: (Int) -> Unit){
            val format: NumberFormat = NumberFormat.getCurrencyInstance()

            itemView.imageView_logo.loadFromUrl(item.icon)
            itemView.textView_currencySymbol.text = item.symbol
            itemView.textView_currencyExchange.text = item.exchange
            itemView.textView_currencyPrice.text = item.currentPrice.toString()

            /*
            itemView.textView_currencyAmount.text = item.amount.toString()
            itemView.textView_purchasePrice.text = item.puchasePrice.toString()
            itemView.textView_currencyInitialTotal.text = format.format(item.puchasePrice * item.amount)
            if (item.currentPrice == 0F) {
                itemView.textview_currencyCurrentPrice.text = "N/A"
                itemView.textView_currencyCurrentTotal.text = "--.-"
                itemView.textView_winlossTotal.text = "--.-"
            } else {
                itemView.textview_currencyCurrentPrice.text = item.currentPrice.toString()
                itemView.textView_currencyCurrentTotal.text = format.format(item.amount * item.currentPrice)
                val winloss = item.amount * item.currentPrice - item.amount * item.puchasePrice
                itemView.textView_winlossTotal.text = format.format(winloss)

                if (winloss >= 0) {
                    itemView.textView_winlossTotal.setTextColor(Color.parseColor("#00A973"))
                } else {
                    itemView.textView_winlossTotal.setTextColor(Color.parseColor("#A90017"))
                }

                if (item.oldPrice != item.currentPrice) {
                    itemView.imageView_diffPrice.visibility = View.VISIBLE
                    if (item.oldPrice > item.currentPrice) {
                        itemView.imageView_diffPrice.setImageDrawable(getDrawable(itemView.context, R.drawable.ic_arrow_down))
                    } else {
                        itemView.imageView_diffPrice.setImageDrawable(getDrawable(itemView.context, R.drawable.ic_arrow_up))
                    }
                } else {
                    itemView.imageView_diffPrice.visibility = View.GONE
                }
            }
            */
            itemView.setOnClickListener { clickListener(item._id) }


        }
    }
}