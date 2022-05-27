package com.kfouri.cryptoprice.ui.view

import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.kfouri.cryptoprice.R
import com.kfouri.cryptoprice.customview.CustomDialog
import com.kfouri.cryptoprice.databinding.FragmentShowCurrencyBinding
import com.kfouri.cryptoprice.domain.model.ChartCurrency
import com.kfouri.cryptoprice.ui.viewmodel.ShowCurrencyViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ShowCurrencyFragment: Fragment() {

    private val viewModel: ShowCurrencyViewModel by viewModels()
    private var localCurrencyId = -1
    private lateinit var binding: FragmentShowCurrencyBinding
    private lateinit var saveButton: MenuItem

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_show_currency,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        localCurrencyId = arguments?.getInt("currencyId") ?: -1

        (activity as MainActivity).supportActionBar?.title = "Info Currency"
        viewModel.getCurrencyById(localCurrencyId)

        binding.lineChart.visibility = View.GONE

        (activity as MainActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setObserver()

        binding.textViewDay.setOnClickListener {
            viewModel.getChartCurrency(viewModel.mCurrencyId, "1")
            changeColorDays("1")
        }
        binding.textViewWeek.setOnClickListener {
            viewModel.getChartCurrency(viewModel.mCurrencyId, "7")
            changeColorDays("7")
        }
        binding.textViewMonth.setOnClickListener {
            viewModel.getChartCurrency(viewModel.mCurrencyId, "30")
            changeColorDays("30")
        }
        binding.textViewYear.setOnClickListener {
            viewModel.getChartCurrency(viewModel.mCurrencyId, "365")
            changeColorDays("365")
        }
        binding.textViewDay.setStatus(true)
        binding.ImageViewEdit.setOnClickListener {
            CustomDialog(requireActivity()).show(
                getString(R.string.app_name),
                getString(R.string.edit_amount),
                binding.textViewCurrencyAmount.text.toString()) {
                if (it.responseType == CustomDialog.ResponseType.YES) {
                    val newValue: String = if (it.value?.isEmpty() == true) {
                        "0.00"
                    } else {
                        it.value.toString()
                    }
                    viewModel.editCurrencyClick(newValue)
                    Toast.makeText(activity, "Amount saved", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun changeColorDays(day: String) {
        binding.textViewDay.setStatus(false)
        binding.textViewWeek.setStatus(false)
        binding.textViewMonth.setStatus(false)
        binding.textViewYear.setStatus(false)

        when (day) {
            "1" -> binding.textViewDay.setStatus(true)
            "7" -> binding.textViewWeek.setStatus(true)
            "30" -> binding.textViewMonth.setStatus(true)
            "365" -> binding.textViewYear.setStatus(true)
        }
    }

    private fun setObserver() {

        viewModel.onFinish.observe(viewLifecycleOwner) {
            activity?.onBackPressed()
        }

        viewModel.infoCurrencyLiveData.observe(viewLifecycleOwner) { currency ->

            with(binding) {
                activity?.let { activity ->
                    Glide.with(activity.applicationContext)
                        .load(currency.icon)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(imageViewLogo)
                }
                textViewCurrencySymbol.text = currency.symbol
                textViewCurrencyPrice.text = currency.currentPriceFormated
                textViewCurrencyAmount.text = currency.amount.toString()
                textViewCurrencyPrice24h.text = currency.open24.toString() + "% 24h"
                if (currency.open24 >= 0) {
                    textViewCurrencyPrice24h.setTextColor(Color.GREEN)
                } else {
                    textViewCurrencyPrice24h.setTextColor(Color.RED)
                }
                textViewCurrencyTotal.text = currency.totalFormated
                textViewCurrencyExchange.text = currency.exchange
                TextViewMarketcaprank.text = currency.marketCap
                TextViewHigh24.text = currency.high24Formated
                TextViewLow24.text = currency.low24Formated
            }

            setupChart()
        }

        viewModel.chartCurrencyLiveData.observe(viewLifecycleOwner) { data ->
            setData(data)
        }
    }

    private fun setupChart() {

        with(binding.lineChart) {
            setViewPortOffsets(0f, 0f, 0f, 0f)
            setBackgroundColor(Color.rgb(27, 27, 27))

            description.isEnabled = false

            setTouchEnabled(false)
            isDragEnabled = true
            setScaleEnabled(true)
            setPinchZoom(false)
            setDrawGridBackground(false)
            maxHighlightDistance = 300f
            axisRight.setDrawGridLines(false)
            axisLeft.setDrawGridLines(false)
            xAxis.setDrawGridLines(false)

            val x: XAxis = xAxis
            x.isEnabled = false
            x.position = XAxis.XAxisPosition.BOTTOM

            val y: YAxis = axisLeft
            y.setLabelCount(6, false)
            y.textColor = Color.WHITE
            y.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART)

            legend.isEnabled = false;

            animateXY(1000, 1000);
        }

        viewModel.getChartCurrency(viewModel.mCurrencyId, "1")
    }

    private fun setData(info: ChartCurrency) {

        val values: ArrayList<Entry> = ArrayList()

        info.prices.forEach { data ->
            values.add(Entry(data[0].toFloat(), data[1].toFloat()))
        }

        val set1: LineDataSet

        with(binding.lineChart) {
            if (data != null &&
                data.dataSetCount > 0) {
                set1 = data.getDataSetByIndex(0) as LineDataSet
                set1.values = values
                data.notifyDataChanged()
                notifyDataSetChanged()
            } else {
                // create a dataset and give it a type
                set1 = LineDataSet(values, "DataSet 1");

                with(set1) {
                    mode = LineDataSet.Mode.CUBIC_BEZIER
                    cubicIntensity = 0.2f
                    setDrawFilled(true)
                    setDrawCircles(false)
                    lineWidth = 2.5f
                    circleRadius = 4f
                    setCircleColor(Color.WHITE)
                    highLightColor = Color.rgb(255, 0, 0)
                    color = Color.rgb(37, 150, 190)
                    fillColor = Color.rgb(37, 150, 190)
                    fillAlpha = 80
                    setDrawHorizontalHighlightIndicator(false)
                    setFillFormatter { _, _ -> axisLeft.axisMinimum }
                }

                // create a data object with the data sets
                val lineData = LineData(set1)
                lineData.setValueTextSize(9f)
                lineData.setDrawValues(false)

                // set data
                data = lineData
            }
            visibility = View.VISIBLE
            invalidate()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.edit_menu, menu)
        if (localCurrencyId == -1) {
            menu.getItem(1).isVisible = false
        }

        saveButton = menu.getItem(0)
        saveButton.isVisible = false
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_remove -> {
                removeCurrency()
                true
            }
            android.R.id.home -> {
                activity?.onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun removeCurrency() {
        CustomDialog(requireActivity()).show(getString(R.string.app_name), getString(R.string.are_you_sure)) {
            if (it.responseType == CustomDialog.ResponseType.YES) {
                viewModel.removeCurrency()
            }
        }
    }
}