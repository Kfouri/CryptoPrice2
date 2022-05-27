package com.kfouri.cryptoprice.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.recyclerview.widget.RecyclerView
import com.kfouri.cryptoprice.R
import com.kfouri.cryptoprice.databinding.CurrencyItemBinding
import com.kfouri.cryptoprice.domain.model.Currency
import com.kfouri.cryptoprice.ext.loadFromUrl
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.NumberFormat
import java.util.*

class ListAdapter(private val clickListener: (Int) -> Unit,
                  private val refreshList: (Double, Double) -> Unit,
                  ) : RecyclerView.Adapter<RecyclerView.ViewHolder>(),
    Filterable {

    private var list = ArrayList<Currency>()
    private var fullList = ArrayList<Currency>()
    private var visible: Boolean = true
    private lateinit var binding: CurrencyItemBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        binding = CurrencyItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = list[position]
        (holder as ViewHolder).bind(item, clickListener, visible)

        //binding.itemParent.animation = AnimationUtils.loadAnimation(holder.itemView.context, R.anim.list_item_animation)
    }

    fun setData(newList: ArrayList<Currency>) {
        list.clear()
        list.addAll(newList)
        fullList = ArrayList(list)
        notifyDataSetChanged()
    }

    fun setBalanceVisibility(visibility: Boolean) {
        visible = visibility
        notifyDataSetChanged()
    }

    class ViewHolder(private val binding: CurrencyItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Currency, clickListener: (Int) -> Unit, visible: Boolean){

            binding.imageViewLogo.loadFromUrl(item.icon)
            binding.textViewCurrencySymbol.text = item.symbol.uppercase()
            binding.textViewCurrencyPrice.text = "$"+BigDecimal.valueOf(item.currentPrice).toPlainString()
            binding.textViewCurrencyAmount.text = BigDecimal.valueOf(item.amount).toPlainString()

            if (item.currentPrice == 0.0) {
                binding.textViewCurrencyPrice.text = "$0.00"
                binding.textViewPricePercentage.text = "$0.00"
                binding.linearLayoutPricePercentage.setBackgroundResource(R.drawable.percentage_background_green)
                binding.imageViewArrowPrice.setImageDrawable(getDrawable(itemView.context, R.drawable.ic_arrow_up))
            } else {
                binding.textViewPricePercentage.text = "${item.open24.toBigDecimal().setScale(2, RoundingMode.UP).toDouble()}%"
                if (item.open24 < 0.0) {
                    binding.linearLayoutPricePercentage.setBackgroundResource(R.drawable.percentage_background_red)
                    binding.imageViewArrowPrice.setImageDrawable(getDrawable(itemView.context, R.drawable.ic_arrow_down))
                } else {
                    binding.linearLayoutPricePercentage.setBackgroundResource(R.drawable.percentage_background_green)
                    binding.imageViewArrowPrice.setImageDrawable(getDrawable(itemView.context, R.drawable.ic_arrow_up))
                }

                val have = item.amount * item.currentPrice
                binding.textViewHave.text = "$" + have.toBigDecimal().setScale(2, RoundingMode.UP).toDouble().toString()
            }

            if (visible) {
                binding.textViewHave.visibility = View.VISIBLE
                binding.textViewHaveHidden.visibility = View.GONE

                binding.textViewCurrencyAmount.visibility = View.VISIBLE
                binding.textViewCurrencyAmountHidden.visibility = View.GONE
            } else {
                binding.textViewHave.visibility = View.GONE
                binding.textViewHaveHidden.visibility = View.VISIBLE

                binding.textViewCurrencyAmount.visibility = View.GONE
                binding.textViewCurrencyAmountHidden.visibility = View.VISIBLE
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
                    if (it.symbol.lowercase().contains(constraint.toString().lowercase().trim())) {
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