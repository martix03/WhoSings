package it.marta.whosings.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import it.marta.whosings.R
import it.marta.whosings.databinding.LoginFragmentBinding

class Login : Fragment() {
    private var binding: LoginFragmentBinding? = null
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = LoginFragmentBinding.inflate(inflater).let {
        binding = it
        it.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.login?.setOnClickListener {
            val username = binding?.username?.text?.toString()
            val name = binding?.name?.text?.toString()

            when {
                username?.isBlank() == true -> Toast.makeText(
                    context,
                    getString(R.string.username_hint),
                    Toast.LENGTH_LONG
                ).show()
                name?.isBlank() == true -> Toast.makeText(
                    context,
                    getString(R.string.name_hint),
                    Toast.LENGTH_LONG
                ).show()
                else -> viewModel.checkUsername(username!!.trim())
            }
        }

        observe()

    }

    private fun observe() {
        viewModel.usernameFound.observe(viewLifecycleOwner) {
            if (!it) {
                viewModel.addUser(
                    binding?.username?.text?.toString()?.trim()!!,
                    binding?.name?.text?.toString()?.trim()!!
                )
                Toast.makeText(
                    context,
                    getString(R.string.user_added),
                    Toast.LENGTH_LONG
                ).show()
            }
            findNavController().navigate(LoginDirections.actionLoginToMainFragment())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}