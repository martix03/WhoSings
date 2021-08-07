package it.marta.whosings.ui.profile

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import it.marta.whosings.R
import it.marta.whosings.databinding.ProfileFragmentBinding
import it.marta.whosings.ui.main.MainFragmentDirections

class ProfileFragment : Fragment() {
    private var binding: ProfileFragmentBinding? = null
    private val viewModel: ProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = ProfileFragmentBinding.inflate(inflater).let {
        binding = it
        it.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_logout, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.logout)
            findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToLogin())

        return super.onOptionsItemSelected(item)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.checkUsername()
        setHasOptionsMenu(true)

        binding?.bestPlayers?.setOnClickListener {
            findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToChartFragment())
        }
        observe()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    @SuppressLint("SetTextI18n")
    private fun observe() {
        viewModel.user.observe(viewLifecycleOwner) {
            binding?.name?.text = it.name
            binding?.username?.text = it.username
            binding?.matches?.text = "${it.matchWon}/${it.totalMatch}"
        }
    }

}