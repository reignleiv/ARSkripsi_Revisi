package com.example.arskripsi_revisi.ui.ar
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.fragment.app.activityViewModels
import com.example.arskripsi_revisi.databinding.FragmentARBinding
import com.example.arskripsi_revisi.helpers.StringHelper
import com.example.arskripsi_revisi.ui.home.HomeViewModel
import io.github.sceneview.ar.ArSceneView

import io.github.sceneview.ar.node.ArModelNode
import io.github.sceneview.ar.node.PlacementMode
import io.github.sceneview.math.Position

class ARFragment : Fragment() {
    private var _binding: FragmentARBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel: HomeViewModel by activityViewModels()

    private lateinit var sceneView: ArSceneView
    private lateinit var modelNode: ArModelNode

    private val BASE_URL = "https://arskripsi.irnhakim.com/public/models/"
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentARBinding.inflate(inflater, container, false)
        sceneView = binding.sceneView
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        homeViewModel.getSelectedBarang.observe(viewLifecycleOwner) { barang ->
            val modelPath = barang.model

            loadModel(StringHelper.getUrlSafeName("$modelPath"))
        }
    }

    private fun loadModel(fileName: String){
        Log.d(TAG,"Start")
        try {
//            binding.sceneView.apply {
//                this.lightEstimationMode = LightEstimationMode.ENVIRONMENTAL_HDR_NO_REFLECTIONS
//            }

            binding.place.setOnClickListener {
                placeModel()
            }

            modelNode = ArModelNode(sceneView.engine).apply {
                val glbPath = BASE_URL + fileName
                Log.d(TAG, "Model URL: $glbPath")

                loadModelGlbAsync(
                    glbFileLocation = glbPath,
                    scaleToUnits = 1f,
                    centerOrigin = Position(-0.5f)
                ) {
                    sceneView.planeRenderer.isVisible = true
                    Log.d(TAG, "Model loaded successfully")
                }
                onAnchorChanged = {
                    binding.place.isGone
                }
                isScaleEditable = false
                isRotationEditable = false
                isSelectable = false
            }
            Log.d(TAG, modelNode.toString())
            sceneView.addChild(modelNode)
        } catch (e: Exception) {
            Log.e(TAG, "Error initializing AR", e)
        }
    }

    private fun placeModel() {
        try {
            modelNode.anchor()
            sceneView.planeRenderer.isVisible = false
        } catch (e: Exception) {
            Log.e(TAG, "Error placing model", e)
        }
    }

    companion object {
        private const val TAG = "ARFragment"
    }
}