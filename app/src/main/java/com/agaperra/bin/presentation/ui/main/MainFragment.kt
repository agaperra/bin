package com.agaperra.bin.presentation.ui.main

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
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
import com.agaperra.bin.data.remote.dto.BinResponse
import com.agaperra.bin.databinding.FragmentMainBinding
import com.agaperra.bin.domain.AppState
import com.agaperra.bin.domain.model.BinItem
import com.agaperra.bin.presentation.adapter.BinListAdapter
import com.agaperra.bin.presentation.adapter.listener.BinNumberClickListener
import com.agaperra.bin.utils.hideKeyboard
import com.agaperra.bin.utils.launchingWhenStarted
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onEach
import java.util.*

@AndroidEntryPoint
@ExperimentalCoroutinesApi
class MainFragment : Fragment(R.layout.fragment_main) {

    private val mainViewModel: MainViewModel by activityViewModels()

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private val binAdapter by lazy {
        BinListAdapter(object : BinNumberClickListener {
            override fun onItemClick(item: BinItem) {
                binding.enterBinET.setText(item.number)
            }
        })
    }


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

        mainViewModel.liveDataEnter.observe(viewLifecycleOwner) { binding.enterTextTV.text = it }
        mainViewModel.liveDataHistory.observe(viewLifecycleOwner) { binding.historyTitle.text = it }

        binding.historyRecycler.adapter = binAdapter
        startObserveHistory(mainViewModel.readAllBin)

        binding.enterBinButton.setOnClickListener {
            if (binding.enterBinET.text.toString().trim().isEmpty()) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.toast_enter),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                this.hideKeyboard()
                mainViewModel.getContent(binding.enterBinET.text.toString())
                startObserve(mainViewModel.mainContent)
            }
            mainViewModel.insertBin(binding.enterBinET.text.toString())
        }
        binding.enterBinET.setOnClickListener {
            binding.materialCardView.visibility = View.GONE
        }
    }

    private fun startObserve(contentSource: StateFlow<AppState<BinResponse>>) {
        contentSource.onEach { result ->
            setResult(result)
        }.launchingWhenStarted(lifecycleScope)
    }

    private fun startObserveHistory(contentSource: StateFlow<List<BinItem>>) {
        contentSource.onEach { result ->
            setResultHistory(result)
        }.launchingWhenStarted(lifecycleScope)
    }

    @SuppressLint("SetTextI18n")
    private fun setResult(result: AppState<BinResponse>) {
        when (result) {
            is AppState.Error -> {
                binding.progressBar.isVisible = false
                binding.materialCardView.visibility = View.GONE
            }
            is AppState.Loading -> {
                binding.progressBar.isVisible = true
                binding.materialCardView.visibility = View.GONE
            }
            is AppState.Success -> {
                binding.progressBar.isVisible = false
                binding.materialCardView.visibility = View.VISIBLE

                binding.bankCity.text = mainViewModel.mainContent.value.data?.bank?.city ?: " ? "
                binding.bankName.text = mainViewModel.mainContent.value.data?.bank?.name ?: " ? "
                binding.bankPhone.text = mainViewModel.mainContent.value.data?.bank?.phone ?: " ? "
                binding.bankURL.text = mainViewModel.mainContent.value.data?.bank?.url ?: " ? "

                if (mainViewModel.mainContent.value.data?.country?.latitude != null && mainViewModel.mainContent.value.data?.country?.longitude != null) {
                    binding.coordinates.text =
                        mainViewModel.mainContent.value.data!!.country!!.latitude.toString() + ", " + mainViewModel.mainContent.value.data?.country?.longitude
                            .toString()
                    binding.coordinates.setOnClickListener {
                        val uri: String = java.lang.String.format(
                            Locale.getDefault(),
                            "geo:%d,%d",
                            mainViewModel.mainContent.value.data?.country?.latitude,
                            mainViewModel.mainContent.value.data?.country?.longitude
                        )
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
                        requireContext().startActivity(intent)
                    }
                } else binding.coordinates.text = " ? "


                binding.country.text = mainViewModel.mainContent.value.data?.country?.name ?: " ? "

                binding.prepaid.text =
                    (mainViewModel.mainContent.value.data?.prepaid ?: " ? ").toString()

                binding.type.text = (
                        mainViewModel.mainContent.value.data?.type ?: " ? "
                        ).toString()

                binding.scheme.text = mainViewModel.mainContent.value.data?.scheme ?: "?"

                binding.brand.text = mainViewModel.mainContent.value.data?.brand ?: " ? "

                binding.len.text =
                    (mainViewModel.mainContent.value.data?.number?.length ?: " ? ").toString()
                binding.lunh.text =
                    (mainViewModel.mainContent.value.data?.number?.luhn ?: " ? ").toString()
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setResultHistory(result: List<BinItem>) {
        binAdapter.submitList(result)
        binAdapter.notifyDataSetChanged()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}