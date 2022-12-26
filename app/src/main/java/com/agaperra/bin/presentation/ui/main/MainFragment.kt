package com.agaperra.bin.presentation.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.agaperra.bin.R
import com.agaperra.bin.databinding.FragmentMainBinding
import com.agaperra.bin.domain.AppState
import com.agaperra.bin.domain.model.BinResponse
import com.agaperra.bin.utils.launchingWhenStarted
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
@ExperimentalCoroutinesApi
class MainFragment : Fragment(R.layout.fragment_main) {

    private val mainViewModel: MainViewModel by activityViewModels()

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        doInitialization()
    }

    private fun doInitialization() {
        mainViewModel.liveData.observe(viewLifecycleOwner) { binding.enterTextTV.text = it }
        binding.enterBinButton.setOnClickListener {
            if (binding.enterBinET.text.toString().trim().isEmpty()){
                Toast.makeText(requireContext(), getString(R.string.toast_enter), Toast.LENGTH_SHORT).show()
            }
            else {
                mainViewModel.getContent(binding.enterBinET.text.toString())
                startObserve(mainViewModel.mainContent)
            }
        }
    }

    private fun startObserve(contentSource: StateFlow<AppState<BinResponse>>) {
        contentSource.onEach { result ->
            setResult(result)
        }.launchingWhenStarted(lifecycleScope)
    }

    private fun setResult(result: AppState<BinResponse>) {
        when (result) {
            is AppState.Error -> {
                binding.progressBar.isVisible = false
            }
            is AppState.Loading -> {
                println("it is ok")
                binding.progressBar.isVisible = true
            }
            is AppState.Success -> {
                binding.progressBar.isVisible = false
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}