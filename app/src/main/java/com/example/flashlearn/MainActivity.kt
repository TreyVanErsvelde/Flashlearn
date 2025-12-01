package com.example.flashlearn

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private var selectedDeckIndex: Int = -1

    private lateinit var rvDecks: RecyclerView
    private lateinit var btnAddDeck: Button
    private lateinit var btnStudyMode: Button
    private lateinit var btnTriviaMode: Button

    private lateinit var adapter: DeckAdapter
    private lateinit var repository: FlashRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // ⭐ Setup toolbar
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false) // disable default title

        // ⭐ Set custom centered title
        val titleText = findViewById<TextView>(R.id.toolbarTitle)
        titleText.text = "FlashLearn"

        repository = FlashRepository.getInstance(this)

        rvDecks = findViewById(R.id.rvDecks)
        btnAddDeck = findViewById(R.id.btnAddDeck)
        btnStudyMode = findViewById(R.id.btnStudyMode)
        btnTriviaMode = findViewById(R.id.btnTriviaMode)

        // When a deck is tapped → select deck only
        adapter = DeckAdapter(repository.decks, repository) { index ->
            selectedDeckIndex = index

            Toast.makeText(
                this,
                "Selected deck: ${repository.decks[index].name}",
                Toast.LENGTH_SHORT
            ).show()
        }

        rvDecks.layoutManager = LinearLayoutManager(this)
        rvDecks.adapter = adapter

        btnAddDeck.setOnClickListener {
            showAddDeckDialog()
        }

        btnStudyMode.setOnClickListener {
            if (selectedDeckIndex == -1) {
                Toast.makeText(this, "Select a deck first!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val intent = Intent(this, StudyModeActivity::class.java)
            intent.putExtra("deckIndex", selectedDeckIndex)
            startActivity(intent)
        }

        btnTriviaMode.setOnClickListener {
            if (selectedDeckIndex == -1) {
                Toast.makeText(this, "Select a deck first!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val intent = Intent(this, TriviaModeActivity::class.java)
            intent.putExtra("deckIndex", selectedDeckIndex)
            startActivity(intent)
        }
    }

    private fun showAddDeckDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_deck, null)

        val etName = dialogView.findViewById<EditText>(R.id.etDeckName)
        val etDesc = dialogView.findViewById<EditText>(R.id.etDeckDesc)

        AlertDialog.Builder(this)
            .setTitle("Create New Deck")
            .setView(dialogView)
            .setPositiveButton("Create") { _, _ ->
                val name = etName.text.toString()
                val desc = etDesc.text.toString()

                if (name.isEmpty()) {
                    Toast.makeText(this, "Name required", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                repository.addDeck(name, desc)
                adapter.notifyItemInserted(repository.decks.size - 1)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {

            R.id.action_about -> {
                Toast.makeText(this, "FlashLearn v1.0 — Created by Team Chimichanga", Toast.LENGTH_LONG).show()
                true
            }

            R.id.action_help -> {
                Toast.makeText(this, "Need help? Add decks → add cards → study or play trivia!", Toast.LENGTH_LONG).show()
                true
            }

            R.id.action_settings -> {
                Toast.makeText(this, "Settings — coming soon!", Toast.LENGTH_SHORT).show()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}
