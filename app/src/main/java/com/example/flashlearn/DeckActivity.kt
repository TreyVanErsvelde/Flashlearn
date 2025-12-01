package com.example.flashlearn

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.content.Intent

class DeckActivity : AppCompatActivity() {

    private lateinit var tvDeckName: TextView
    private lateinit var tvDeckDesc: TextView
    private lateinit var rvCards: RecyclerView
    private lateinit var btnAddCard: Button
    private lateinit var btnStudy: Button
    private lateinit var btnTrivia: Button

    private lateinit var repository: FlashRepository
    private lateinit var adapter: CardAdapter

    private var deckIndex = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_deck)

        Log.d("DEBUG", "=== DECK ACTIVITY STARTED ===")

        repository = FlashRepository.getInstance(this)

        deckIndex = intent.getIntExtra("deckIndex", -1)

        if (deckIndex == -1 || deckIndex >= repository.decks.size) {
            Toast.makeText(this, "Invalid deck selected", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        val deck = repository.decks[deckIndex]

        initViews()

        tvDeckName.text = deck.name ?: "Unnamed Deck"
        tvDeckDesc.text = deck.description ?: "No description"

        val cards = deck.cards ?: mutableListOf()

        adapter = CardAdapter(cards, deckIndex, repository)
        rvCards.layoutManager = LinearLayoutManager(this)
        rvCards.adapter = adapter

        setupClickListeners()
    }

    private fun initViews() {
        tvDeckName = findViewById(R.id.tvDeckName)
        tvDeckDesc = findViewById(R.id.tvDeckDesc)
        rvCards = findViewById(R.id.rvCards)
        btnAddCard = findViewById(R.id.btnAddCard)
        btnStudy = findViewById(R.id.btnStudy)
        btnTrivia = findViewById(R.id.btnTrivia)
    }

    private fun setupClickListeners() {

        btnAddCard.setOnClickListener {
            showAddCardDialog()
        }

        btnStudy.setOnClickListener {
            val deck = repository.decks[deckIndex]
            if (deck.cards.isEmpty()) {
                Toast.makeText(this, "No cards in this deck to study!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val intent = Intent(this, StudyModeActivity::class.java)
            intent.putExtra("deckIndex", deckIndex)
            startActivity(intent)
        }

        btnTrivia.setOnClickListener {
            Toast.makeText(this, "Trivia button clicked", Toast.LENGTH_SHORT).show()

            val deck = repository.decks[deckIndex]
            if (deck.cards.isEmpty()) {
                Toast.makeText(this, "No cards in this deck for trivia!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val intent = Intent(this, TriviaModeActivity::class.java)
            intent.putExtra("deckIndex", deckIndex)   // 👈 MUST MATCH k
            startActivity(intent)
        }

    }

    // ✅ FIXED: now outside setupClickListeners
    private fun showAddCardDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_card, null)
        val etQuestion = dialogView.findViewById<EditText>(R.id.etQuestion)
        val etAnswer = dialogView.findViewById<EditText>(R.id.etAnswer)

        AlertDialog.Builder(this)
            .setTitle("Add Card")
            .setView(dialogView)
            .setPositiveButton("Add") { _, _ ->
                val q = etQuestion.text.toString()
                val a = etAnswer.text.toString()

                if (q.isNotEmpty() && a.isNotEmpty()) {
                    repository.addCard(deckIndex, Card(q, a))
                    adapter.notifyItemInserted(repository.decks[deckIndex].cards.size - 1)
                    Toast.makeText(this, "Card added!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Please enter both question and answer", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}
