package com.example.arskripsi_revisi.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.arskripsi_revisi.R
import com.example.arskripsi_revisi.data.adapter.ModelAdapter
import com.example.arskripsi_revisi.data.model.Barang
import com.example.arskripsi_revisi.databinding.FragmentHomeBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class HomeFragment : Fragment(), ModelAdapter.OnItemClickListener {

    private val binding: FragmentHomeBinding by lazy { FragmentHomeBinding.inflate(layoutInflater) }
    private lateinit var adapter: ModelAdapter
    private val homeViewModel: HomeViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
//        val homeViewModel =
//            ViewModelProvider(this).get(HomeViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerview.layoutManager = GridLayoutManager(context, 2)

        val database = Firebase.database
        val myRef = database.getReference("barang")

        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                val barangList = mutableListOf<Barang>()
                for (barangSnapshot in snapshot.children) {
                    val barang = barangSnapshot.getValue(Barang::class.java)
                    barang?.let {
                        Log.d(TAG, "onDataChange: $it")
                        barangList.add(it)
                    }
                }
                adapter = ModelAdapter(barangList, this@HomeFragment)
                binding.recyclerview.adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "Error fetching data: ${error.message}")
            }

        })

    }

    companion object {
        private const val TAG = "HomeFragment"
    }

    override fun onItemClick(barang: Barang) {
        homeViewModel.setSelectedBarang(barang)
        findNavController().navigate(R.id.action_navigation_home_to_detailFragment)

    }
}