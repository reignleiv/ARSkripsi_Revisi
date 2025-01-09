package com.example.arskripsi_revisi.ui.ar

import android.content.Context
import android.os.Bundle
import android.util.Base64
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.fragment.app.activityViewModels
import com.example.arskripsi_revisi.databinding.FragmentARBinding
import com.example.arskripsi_revisi.ui.home.HomeViewModel
import com.google.ar.core.Config
import io.github.sceneview.ar.ArSceneView
import io.github.sceneview.ar.node.ArModelNode
import io.github.sceneview.ar.node.PlacementMode
import io.github.sceneview.math.Position
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import kotlin.concurrent.write

class ARFragment : Fragment() {
    private val binding : FragmentARBinding by lazy { FragmentARBinding.inflate(layoutInflater) }
    private val homeViewModel: HomeViewModel by activityViewModels()

    private lateinit var sceneView: ArSceneView
    private lateinit var modelNode: ArModelNode

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        homeViewModel.getSelectedBarang.observe(viewLifecycleOwner) { barang ->
            barang?.model.let {
//                binding.selectedBarang.text = it.fileName ?: "Error"

                val model = decodeBase64ToFile(requireContext(), it?.content!!,it?.filename!!)

                if (model != null) {
                    Log.d(TAG, "onViewCreated: ${model.absolutePath}")
                    loadModel(model.absolutePath)
                }
            }
        }
    }

    private fun loadModel(glbLocation: String){
        try {
            binding.sceneView.apply {
                this.lightEstimationMode = Config.LightEstimationMode.DISABLED
            }

            binding.place.setOnClickListener {
                placeModel()
            }

//            val modelLocation = intent.getStringExtra("MODEL_LOCATION") ?: "assets/models/meja.glb"
//            Log.d("LihatARbang", "Model location: $modelLocation")

            modelNode = ArModelNode(sceneView.engine, PlacementMode.INSTANT).apply {
                loadModelGlbAsync(
                    glbFileLocation = glbLocation,
                    scaleToUnits = 1f,
                    centerOrigin = Position(-0.5f)
                ) {
                    sceneView.planeRenderer.isVisible = false
                    Log.d(TAG, "Model loaded successfully")
                }
                onAnchorChanged = {
                    binding.place.isGone = it != null
                }
                // Disable scaling and zooming
                isScaleEditable = false
                isRotationEditable = false
                isSelectable=false
            }
            sceneView.addChild(modelNode)
        } catch (e: Exception) {
            Log.e("LihatAR", "Error initializing AR", e)
        }
    }
    private fun placeModel() {
        try {
            modelNode.anchor()
            sceneView.planeRenderer.isVisible = false
        } catch (e: Exception) {
            Log.e("LihatAR", "Error placing model", e)
        }
    }

    fun decodeBase64ToFile(context: Context, base64String: String, fileName: String): File? {
        val decodedBytes: ByteArray?
        try {
            decodedBytes = Base64.decode(base64String, android.util.Base64.DEFAULT)
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
            return null
        }

        val glbFile = File(context.cacheDir, fileName)
        try {
            val fos = FileOutputStream(glbFile)
            fos.write(decodedBytes)
            fos.close()
            return glbFile
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }
    }
    companion object {
        private const val TAG = "ARFragment"
    }
}