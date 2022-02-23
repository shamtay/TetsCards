package com.example.testapplication.cards

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.testapplication.data.api.model.GithubRepositoryItem
import com.example.testapplication.databinding.ItemCardBinding

class CardsAdapter :RecyclerView.Adapter<CardViewHolder>() {

    var cardsRemoveListener: ((Int) -> Unit)? = null

    private val items: MutableList<GithubRepositoryItem> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val binding = ItemCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        holder.bind(items[position], position)
    }

    override fun getItemCount(): Int {
        return if (items.size >= 2) 2 else items.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(newItems: List<GithubRepositoryItem>) {
        items.clear()
        items.addAll(newItems)
        /**
         * it is fine to use the [notifyDataSetChanged] as there are no more then 2 elements in the
         * recycler view every time
         */
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun removeTop() {
        if (items.size >= 3) {
            items.removeAt(1)
            val middle = items[0]
            items[0] = items[1]
            items[1] = middle
            notifyDataSetChanged()
            cardsRemoveListener?.invoke(items.size)
        } else if (items.size >= 1) {
            items.removeAt(items.size - 1)
            notifyDataSetChanged()
            cardsRemoveListener?.invoke(items.size)
        }

    }
}