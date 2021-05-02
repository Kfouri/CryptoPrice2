package com.kfouri.cryptoprice2.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.recyclerview.widget.RecyclerView
import com.kfouri.cryptoprice2.R
import com.kfouri.cryptoprice2.domain.model.Currency
import com.kfouri.cryptoprice2.ext.loadFromUrl
import kotlinx.android.synthetic.main.currency_item.view.*
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.abs

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
        //holder.itemView.item_parent.animation = AnimationUtils.loadAnimation(holder.itemView.context, R.anim.list_item_animation)
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
            itemView.textView_currencyPrice.text = "$"+BigDecimal.valueOf(item.currentPrice).toPlainString()

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

                val res = abs((item.currentPrice - item.oldPrice) * 100 / item.oldPrice)
                itemView.textView_pricePercentage.text = "${res.toBigDecimal().setScale(2, RoundingMode.UP).toDouble()}%"
                if (item.oldPrice > item.currentPrice) {
                    itemView.linearLayout_pricePercentage.setBackgroundResource(R.drawable.percentage_background_red)
                    itemView.imageView_arrowPrice.setImageDrawable(getDrawable(itemView.context, R.drawable.ic_arrow_down))
                } else {
                    itemView.linearLayout_pricePercentage.setBackgroundResource(R.drawable.percentage_background_green)
                    itemView.imageView_arrowPrice.setImageDrawable(getDrawable(itemView.context, R.drawable.ic_arrow_up))
                }

                var earned = item.amount * item.currentPrice - item.amount * item.purchasePrice
                var earnedPercentage = abs(earned * 100 / item.purchasePrice)

                earned = if (earned >= 1) {
                    earned.toBigDecimal().setScale(2, RoundingMode.UP).toDouble()
                } else {
                    earned.toBigDecimal().setScale(8, RoundingMode.UP).toDouble()
                }

                earnedPercentage = earnedPercentage.toBigDecimal().setScale(2, RoundingMode.UP).toDouble()
                itemView.textView_earnedPercentage.text = "${earnedPercentage}%"

                itemView.textView_earn.text = "${earned}"
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
}