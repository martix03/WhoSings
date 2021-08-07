package it.marta.whosings.ui.chart

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.XAxis.XAxisPosition
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import it.marta.whosings.R
import it.marta.whosings.databinding.ChartFragmentBinding


class ChartFragment : Fragment() {
    private var binding: ChartFragmentBinding? = null
    private val viewModel: ChartViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = ChartFragmentBinding.inflate(inflater).let {
        binding = it
        it.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getHigherScores()
        observe()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    @SuppressLint("SetTextI18n")
    private fun observe() {
        viewModel.users.observe(viewLifecycleOwner) {
            val barEntryList = mutableListOf<BarEntry>()

            it.forEachIndexed { index, user ->
                val x = index + 1
                barEntryList.add(BarEntry(x.toFloat(), user.matchWon?.toFloat()!!))
            }

            val startColor = ContextCompat.getColor(requireContext(), R.color.rose)

            val set1 = BarDataSet(barEntryList, "")
            set1.color = startColor

            val dataSets: ArrayList<IBarDataSet> = ArrayList()
            dataSets.add(set1)

            val data = BarData(dataSets)

            val xAxis: XAxis = binding?.chart?.xAxis!!
            xAxis.position = XAxisPosition.BOTTOM
            xAxis.setDrawGridLines(false)
            xAxis.granularity = 1f

            xAxis.labelCount = 3
            xAxis.valueFormatter = object : ValueFormatter() {
                override fun getAxisLabel(value: Float, axis: AxisBase?): String {
                    return it.get((value - 1).toInt()).name ?: ""
                }
            }

            binding?.chart?.legend?.isEnabled = false
            binding?.chart?.description?.isEnabled = false
            binding?.chart?.setPinchZoom(false)
            binding?.chart?.data = data
            binding?.chart?.invalidate()

        }
    }

}