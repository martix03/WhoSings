package it.marta.whosings.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import it.marta.whosings.R
import it.marta.whosings.databinding.YouWinDialogFragmentBinding

class YouWinDialogFragment(val match: ArrayList<Boolean>, val playAgain: () -> Unit) :
    DialogFragment() {

    private var binding: YouWinDialogFragmentBinding? = null

    fun show(fragmentManager: FragmentManager) {
        super.show(fragmentManager, YouWinDialogFragment::class.java.name)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.DialogFragment)
        isCancelable = false
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = YouWinDialogFragmentBinding.inflate(inflater).let {
        binding = it
        it.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val win = match.count { it } >= 3
        binding?.image?.setImageResource(if (win) R.drawable.ic_first else R.drawable.ic_sad)
        binding?.message?.text = (if (win) getString(R.string.win) else getString(R.string.lose))
        binding?.recap?.text =
            getString(R.string.recap, match.count { it }.toString(), match.size.toString())
        binding?.playAgain?.setOnClickListener {
            playAgain.invoke()
            this.dismiss()
        }
    }


}