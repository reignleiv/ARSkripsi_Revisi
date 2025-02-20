package com.example.arskripsi_revisi.ui.detail

import android.content.Intent
import android.net.Uri
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
import com.example.arskripsi_revisi.ui.home.HomeViewModel
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL


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
                val thumbnailUrl = "https://arskripsi.irnhakim.com/public/storage/${it.thumbnail}"

                Glide.with(binding.productImageView.context)
                    .load(thumbnailUrl)
                    .placeholder(R.drawable.placeholder_200)
                    .into(binding.productImageView)

                val barangUrl = it.url
                if (barangUrl.isNullOrEmpty()) {
                    binding.goToMarketplace.isEnabled = false
                    binding.goToMarketplace.alpha = 0.5f  // Reduce opacity to show it's disabled
                } else {
                    binding.goToMarketplace.isEnabled = true
                    binding.goToMarketplace.alpha = 1f  // Reset opacity
                    binding.goToMarketplace.setOnClickListener {
                        openMarketPlaceUrl(barangUrl)
                    }
                }
            }
        }

        binding.btnLihatAr.setOnClickListener{
            homeViewModel.getSelectedBarang.value?.model?.let { modelName ->
                val url = "https://arskripsi.irnhakim.com/public/storage/$modelName"

                lifecycleScope.launch {
                    if (isUrlValid(url)) {
                        findNavController(view).navigate(R.id.action_detailFragment_to_ARFragment)
                    } else {
                        Toast.makeText(requireContext(), "Invalid model link", Toast.LENGTH_SHORT).show()
                    }
                }
            } ?: run {
                Toast.makeText(requireContext(), "Model not found", Toast.LENGTH_SHORT).show()
            }
        }

    }

    suspend fun isUrlValid(url: String): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val connection = URL(url).openConnection() as HttpURLConnection
                connection.requestMethod = "HEAD"
                connection.connectTimeout = 5000
                connection.responseCode == HttpURLConnection.HTTP_OK
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }
    }

    fun openMarketPlaceUrl(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }
}