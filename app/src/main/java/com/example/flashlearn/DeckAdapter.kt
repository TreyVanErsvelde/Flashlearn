package com.example.flashlearn

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class DeckAdapter(
    private val decks: MutableList<Deck>,
    private val repository: FlashRepository,
    private val onDeckSelected: (Int) -> Unit
) : RecyclerView.Adapter<DeckAdapter.DeckViewHolder>() {

    class DeckViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tvDeckName)
        val tvDesc: TextView = itemView.findViewById(R.id.tvDeckDesc)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeckViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_deck, parent, false)
        return DeckViewHolder(view)
    }

    override fun onBindViewHolder(holder: DeckViewHolder, position: Int) {
        val deck = decks[position]

        holder.tvName.text = deck.name
        holder.tvDesc.text = deck.description

        // TAP → only SELECT deck
        holder.itemView.setOnClickListener {
            onDeckSelected(position)
        }

        // LONG TAP → menu
        holder.itemView.setOnLongClickListener {
            val options = arrayOf("Open Deck", "Delete Deck")

            AlertDialog.Builder(holder.itemView.context)
                .setItems(options) { _, which ->
                    when (which) {

                        // OPEN DECK
                        0 -> {
                            val intent = android.content.Intent(
                                holder.itemView.context,
                                DeckActivity::class.java
                            )
                            intent.putExtra("deckIndex", position)
                            holder.itemView.context.startActivity(intent)
                        }

                        // DELETE DECK
                        1 -> {
                            AlertDialog.Builder(holder.itemView.context)
                                .setTitle("Delete Deck?")
                                .setMessage("Are you sure?")
                                .setPositiveButton("Delete") { _, _ ->

                                    repository.deleteDeck(position)
                                    notifyItemRemoved(position)
                                    notifyItemRangeChanged(position, decks.size)

                                }
                                .setNegativeButton("Cancel", null)
                                .show()
                        }
                    }
                }
                .show()

            true
        }
    }

    override fun getItemCount(): Int = decks.size
}

