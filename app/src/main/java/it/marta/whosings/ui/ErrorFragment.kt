package it.marta.whosings.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import it.marta.whosings.databinding.ErrorFragmentBinding

class ErrorFragment:Fragment() {
    private var binding: ErrorFragmentBinding? = null
    private val args: ErrorFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = ErrorFragmentBinding.inflate(inflater).let {
        binding = it
        it.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.message?.text = args.message
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}