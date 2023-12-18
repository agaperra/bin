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
import com.agaperra.bin.data.remote.dto.CarsAnswerResponse
import com.agaperra.bin.databinding.FragmentMainBinding
import com.agaperra.bin.domain.AppState
import com.agaperra.bin.domain.ErrorState
import com.agaperra.bin.domain.model.CardItem
import com.agaperra.bin.presentation.adapter.BinListAdapter
import com.agaperra.bin.utils.hideKeyboard
import com.agaperra.bin.utils.launchingWhenStarted
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onEach
import timber.log.Timber
import java.util.*

@AndroidEntryPoint
@ExperimentalCoroutinesApi
class MainFragment : Fragment(R.layout.fragment_main) {

    private val mainViewModel: MainViewModel by activityViewModels()

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private val binAdapter by lazy {
        BinListAdapter { item ->
            binding.enterBinET.setText(item.number)
        }
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
                mainViewModel.getContent(
                    requireContext(),
                    binding.enterBinET.text.toString(),
                    noInternet = {
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.internet),
                            Toast.LENGTH_SHORT
                        ).show()
                    },
                    onError = {
                        val error = when (it) {
                            ErrorState.BAD_REQUEST -> {
                                getString(R.string.bad_request)
                            }

                            ErrorState.NO_BIN_LOADED -> {
                                getString(R.string.no_data)
                            }

                            ErrorState.NO_INTERNET_CONNECTION -> {
                                getString(R.string.internet)
                            }

                            ErrorState.TOO_MANY_REQUESTS -> {
                                getString(R.string.too_many)
                            }
                        }
                        Toast.makeText(
                            requireContext(),
                            error,
                            Toast.LENGTH_SHORT
                        ).show()
                    },
                    onSuccess = {
                        startObserve(mainViewModel.mainContent)
                    })

            }
        }
        binding.enterBinET.setOnClickListener {
            binding.materialCardView.visibility = View.GONE
        }
    }

    private fun startObserve(contentSource: StateFlow<AppState<CarsAnswerResponse>>) {
        contentSource.onEach { result ->
            setResult(result)
        }.launchingWhenStarted(lifecycleScope)
    }

    private fun startObserveHistory(contentSource: StateFlow<List<CardItem>>) {
        contentSource.onEach { result ->
            setResultHistory(result)
        }.launchingWhenStarted(lifecycleScope)
    }

    @SuppressLint("SetTextI18n")
    private fun setResult(result: AppState<CarsAnswerResponse>) {
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
                mainViewModel.insertBin(binding.enterBinET.text.toString())
                binding.progressBar.isVisible = false
                binding.materialCardView.visibility = View.VISIBLE

                binding.bankCity.text = mainViewModel.mainContent.value.data?.bank?.city ?: getString(R.string.empty)
                binding.bankName.text = mainViewModel.mainContent.value.data?.bank?.name ?: getString(R.string.empty)
                binding.bankPhone.text = mainViewModel.mainContent.value.data?.bank?.phone ?: getString(R.string.empty)
                binding.bankURL.text = mainViewModel.mainContent.value.data?.bank?.url ?: getString(R.string.empty)

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
                } else binding.coordinates.text = getString(R.string.empty)


                binding.country.text =
                    mainViewModel.mainContent.value.data?.country?.name ?: getString(R.string.empty)

                binding.prepaid.text =
                    (mainViewModel.mainContent.value.data?.prepaid
                        ?: getString(R.string.empty)).toString()

                binding.type.text = (
                        mainViewModel.mainContent.value.data?.type ?: getString(R.string.empty)
                        ).toString()

                binding.scheme.text =
                    mainViewModel.mainContent.value.data?.scheme ?: getString(R.string.empty)

                binding.brand.text =
                    mainViewModel.mainContent.value.data?.brand ?: getString(R.string.empty)

                binding.len.text =
                    (mainViewModel.mainContent.value.data?.number?.length
                        ?: getString(R.string.empty)).toString()
                binding.lunh.text =
                    (mainViewModel.mainContent.value.data?.number?.luhn
                        ?: getString(R.string.empty)).toString()
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setResultHistory(result: List<CardItem>) {
        binAdapter.submitList(result)
        binAdapter.notifyDataSetChanged()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}