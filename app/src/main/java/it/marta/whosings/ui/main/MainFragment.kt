package it.marta.whosings.ui.main

import android.app.AlertDialog
import android.os.Bundle
import android.os.CountDownTimer
import android.view.*
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import it.marta.whosings.R
import it.marta.whosings.data.vo.SimpleDetailTrack
import it.marta.whosings.databinding.MainFragmentBinding

class MainFragment : Fragment() {

    private var binding: MainFragmentBinding? = null
    private val viewModel: MainViewModel by viewModels()
    private var question: List<SimpleDetailTrack>? = null

    private val timer = object : CountDownTimer(10000, 1000) {
        override fun onTick(millisUntilFinished: Long) {
            binding?.timer?.progress = ((millisUntilFinished / 1000) * 10).toInt()
            binding?.countDown?.text = ((millisUntilFinished / 1000).toInt()).toString()
        }

        override fun onFinish() {
            viewModel.getListTrack(false)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = MainFragmentBinding.inflate(inflater).let {
        binding = it
        it.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.profile) {
            timer.cancel()
            findNavController().navigate(MainFragmentDirections.actionMainFragmentToProfileFragment())
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)

        viewModel.getListTrack()

        binding?.firstAnswer?.setOnClickListener {
            binding?.firstCardView?.setCardViewBackgroundColor(if (question?.get(0)?.stringTrack != null) R.color.green else R.color.red)
            timer.cancel()
            viewModel.getListTrack(question?.get(0)?.stringTrack != null)
        }

        binding?.secondAnswer?.setOnClickListener {
            binding?.secondCardView?.setCardViewBackgroundColor(if (question?.get(1)?.stringTrack != null) R.color.green else R.color.red)
            timer.cancel()
            viewModel.getListTrack(question?.get(1)?.stringTrack != null)
        }

        binding?.thirdAnswer?.setOnClickListener {
            binding?.thirdCardView?.setCardViewBackgroundColor(if (question?.get(2)?.stringTrack != null) R.color.green else R.color.red)

            timer.cancel()
            viewModel.getListTrack(question?.get(2)?.stringTrack != null)
        }

        observe()
    }

    private fun observe() {
        viewModel.loading.observe(viewLifecycleOwner) {
            binding?.loader?.visibility = if (it) View.VISIBLE else View.GONE
        }

        viewModel.error.observe(viewLifecycleOwner) {
            findNavController().navigate(MainFragmentDirections.actionMainFragmentToErrorFragment(it))
        }

        viewModel.lyric.observe(viewLifecycleOwner) {
            binding?.lyric?.text = it
        }

        viewModel.artists.observe(viewLifecycleOwner) {
            question = it
            binding?.firstCardView?.setCardViewBackgroundColor(R.color.white)
            binding?.secondCardView?.setCardViewBackgroundColor(R.color.white)
            binding?.thirdCardView?.setCardViewBackgroundColor(R.color.white)

            binding?.firstAnswer?.text = it[0].artistName
            binding?.secondAnswer?.text = it[1].artistName
            binding?.thirdAnswer?.text = it[2].artistName

            timer.start()

        }

        viewModel.win.observe(viewLifecycleOwner) {
            timer.cancel()
            YouWinDialogFragment(it, playAgain = {
                viewModel.getListTrack()
            }).show(requireActivity().supportFragmentManager)
        }

    }

    private fun CardView?.setCardViewBackgroundColor(color: Int) {
        this?.setCardBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                color
            )
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}