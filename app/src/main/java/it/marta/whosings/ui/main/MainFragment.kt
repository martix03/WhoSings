package it.marta.whosings.ui.main

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import it.marta.whosings.databinding.MainFragmentBinding

class MainFragment : Fragment() {

    private var binding: MainFragmentBinding? = null
    private val viewModel: MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = MainFragmentBinding.inflate(inflater).let {
        binding = it
        it.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getListTrack()

        observe()
    }

    private fun observe() {
        viewModel.loading.observe(viewLifecycleOwner) {
//            binding?.loader?.visibility = if (it) View.VISIBLE else View.GONE
        }

        viewModel.error.observe(viewLifecycleOwner) {
            findNavController().navigate(MainFragmentDirections.actionMainFragmentToErrorFragment(it))
        }

        viewModel.lyric.observe(viewLifecycleOwner) {
            binding?.lyric?.text = it
        }

        viewModel.artists.observe(viewLifecycleOwner) {
            binding?.firstAnswer?.text = it[0].artistName
            binding?.secondAnswer?.text = it[1].artistName
            binding?.thirdAnswer?.text = it[2].artistName

            object : CountDownTimer(10000, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    binding?.timer?.progress = ((millisUntilFinished / 1000) * 10).toInt()
                    binding?.countDown?.text = ((millisUntilFinished / 1000).toInt()).toString()
                }

                override fun onFinish() {
                   viewModel.getListTrack()
                }
            }.start()

        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}