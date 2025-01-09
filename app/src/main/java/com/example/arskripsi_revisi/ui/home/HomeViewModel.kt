package com.example.arskripsi_revisi.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.arskripsi_revisi.data.model.Barang
import com.example.arskripsi_revisi.data.repository.ModelRepository

class HomeViewModel : ViewModel() {
    init {
        val repository = ModelRepository()
    }

    private val _selectedBarang = MutableLiveData<Barang>()

    val getSelectedBarang: LiveData<Barang> = _selectedBarang

    fun setSelectedBarang(barang: Barang) {
        _selectedBarang.value = barang
    }
}