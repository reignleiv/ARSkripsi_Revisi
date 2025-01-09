package com.example.arskripsi_revisi.data.adapter

import android.graphics.ColorSpace
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.arskripsi_revisi.R
import com.example.arskripsi_revisi.data.model.Barang

class ModelAdapter(
    private val modelList: List<Barang>,
    private val listener: OnItemClickListener) : RecyclerView.Adapter<ModelAdapter.ModelViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(barang: Barang)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ModelViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_model, parent, false)

        return ModelViewHolder(view)
    }

    override fun getItemCount(): Int = modelList.size

    override fun onBindViewHolder(holder: ModelViewHolder, position: Int) {
        val ItemsViewModel = modelList[position]
        val barang = modelList[position]
        holder.nameTextView.text = barang.name
        holder.priceTextView.text = "Harga: ${barang.price}"
        holder.stockTextView.text = "Stok: ${barang.stock}"

        holder.bind(barang, listener)
    }

    class ModelViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        val priceTextView: TextView = itemView.findViewById(R.id.priceTextView)
        val stockTextView: TextView = itemView.findViewById(R.id.stockTextView)
        val container: View = itemView.findViewById(R.id.container)

        fun bind(barang: Barang, listener: OnItemClickListener) {
            container.setOnClickListener {
                listener.onItemClick(barang)
            }
        }
    }
}