package com.example.yemektariflerisqlite

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.yemektariflerisqlite.databinding.RecyclerRowBinding
import kotlinx.android.synthetic.main.recycler_row.view.*

class ListeRecyclerAdapter(val yemekListesi: ArrayList<String>, val idListesi : ArrayList<Int>) : RecyclerView.Adapter<ListeRecyclerAdapter.YemekHolder>() {

    class YemekHolder(private val binding: RecyclerRowBinding) : RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): YemekHolder {
        val binding = RecyclerRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return YemekHolder(binding)

    }

    override fun onBindViewHolder(holder: YemekHolder, position: Int) {
        holder.itemView.recycler_row_text.text = yemekListesi[position]
        holder.itemView.setOnClickListener {
            val action = ListeFragmentDirections.actionListeFragmentToTarifFragment("recyclerdengeldim",idListesi[position])
            Navigation.findNavController(it).navigate(action)
        }

        
    }

    override fun getItemCount(): Int {
        return yemekListesi.size
    }
}