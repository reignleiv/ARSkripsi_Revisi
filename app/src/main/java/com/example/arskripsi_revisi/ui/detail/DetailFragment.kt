package com.example.arskripsi_revisi.ui.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation.findNavController
import com.example.arskripsi_revisi.R
import com.example.arskripsi_revisi.databinding.FragmentDetailBinding
import com.example.arskripsi_revisi.helpers.Model
import com.example.arskripsi_revisi.ui.home.HomeViewModel
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch


class DetailFragment : Fragment() {

    private val binding : FragmentDetailBinding by lazy { FragmentDetailBinding.inflate(layoutInflater) }
    private val homeViewModel: HomeViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homeViewModel.getSelectedBarang.observe(viewLifecycleOwner) { barang ->
            barang?.let {
                binding.selectedBarang.text = "Nama: ${it.name ?: "Error"}"
                binding.selectedBarangDeskripsi.text = "Deskripsi: ${it.deskripsi ?: "Error"}"
                binding.selectedBarangPrice.text = "Price: Rp.${it.price.toString() ?: "Error"}"
                binding.selectedBarangStock.text = "Stock: ${it.stock.toString() ?: "Error"}"
            }
        }

        binding.btnLihatAr.setOnClickListener{
            homeViewModel.getSelectedBarang.value?.model?.let { modelName ->
                val model = Model(modelName)

                lifecycleScope.launch {
                    val downloadedFile = model.download(requireContext())
                    if (downloadedFile != null) {
                        println("Downloaded file path: ${downloadedFile.absolutePath}")
                        findNavController(view).navigate(R.id.action_detailFragment_to_ARFragment)
                    } else {
                        Toast.makeText(requireContext(), "Failed to download the model", Toast.LENGTH_SHORT).show()
                    }
                }
            } ?: run {
                Toast.makeText(requireContext(), "Model not found", Toast.LENGTH_SHORT).show()
            }
        }
    }
}