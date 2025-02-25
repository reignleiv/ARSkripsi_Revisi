package com.example.arskripsi_revisi.data.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.arskripsi_revisi.R
import com.example.arskripsi_revisi.data.model.Barang

class ModelAdapter(
    private val modelList: List<Barang>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<ModelAdapter.ModelViewHolder>() {

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
        holder.priceTextView.text = "${barang.price}"
        holder.panjangTextView.text = "${barang.long}"
        holder.tinggiTextView.text = "${barang.height}"
        holder.lebarTextView.text = "${barang.width}"
        val thumbnailUrl = "https://arskripsi.irnhakim.com/public/storage/${barang.thumbnail}"

        Glide.with(holder.thumbnailImageView.context)
            .load(thumbnailUrl)
            .placeholder(R.drawable.placeholder_200)
            .into(holder.thumbnailImageView)

        holder.bind(barang, listener)
    }

    class ModelViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val thumbnailImageView: ImageView = itemView.findViewById(R.id.thumbnailView)
        val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        val priceTextView: TextView = itemView.findViewById(R.id.priceTextView)
        val panjangTextView: TextView = itemView.findViewById(R.id.panjangTextView)
        val tinggiTextView: TextView = itemView.findViewById(R.id.tinggiTextView)
        val lebarTextView: TextView = itemView.findViewById(R.id.lebarTextView)
        val container: View = itemView.findViewById(R.id.container)

        fun bind(barang: Barang, listener: OnItemClickListener) {
            container.setOnClickListener {
                listener.onItemClick(barang)
            }
        }
    }
}