package com.example.flashlearn

import android.graphics.Color
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.flashlearn.databinding.ActivityTriviaModeBinding
import kotlin.math.abs
import android.view.animation.AnimationUtils

class TriviaModeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTriviaModeBinding

    private lateinit var deck: Deck
    private var cards = mutableListOf<Card>()

    private var currentIndex = 0
    private var isBackVisible = false

    private var touchX = 0f
    private var touchY = 0f
    private val SWIPE_THRESHOLD = 120f
    private val TAP_THRESHOLD = 10f

    // Answer pools (categories)
    private val gasPool = listOf("Nitrogen", "Carbon Dioxide", "Hydrogen", "Helium", "Methane", "Ozone")

    private val oceanPool = listOf("Pacific Ocean", "Atlantic Ocean", "Indian Ocean", "Arctic Ocean", "Southern Ocean")

    private val foundersPool = listOf("Steve Jobs", "Jeff Bezos", "Larry Page", "Sergey Brin", "Elon Musk")

    private val scientistsPool = listOf("Nikola Tesla", "Thomas Edison", "Alexander Graham Bell", "Marie Curie", "Albert Einstein")

    private val colorPool = listOf("Red", "Blue", "Yellow", "Green", "Purple", "Orange")

    private val numberPool = listOf("Zero", "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten")

    private val elementPool = listOf("Hydrogen", "Helium", "Carbon", "Nitrogen", "Sodium", "Iron", "Neon", "Copper")

    // 🔥 FIX: Expanded fallback city list (ensures ALWAYS enough city distractors)
    private val fallbackCityPool = listOf(
        "Paris", "Tokyo", "London", "Rome", "Berlin", "Madrid",
        "Lisbon", "Vienna", "Prague", "Athens", "Dublin",
        "Amsterdam", "Budapest", "Warsaw", "Stockholm",
        "Oslo", "Helsinki", "Copenhagen", "Zurich"
    )

    // Country pool
    private val countryPool = listOf(
        "United States", "Canada", "Mexico", "Brazil",
        "United Kingdom", "France", "Germany", "Spain",
        "Italy", "China", "Japan", "Australia", "India"
    )

    private lateinit var correctAnswer: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTriviaModeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val deckIndex = intent.getIntExtra("deckIndex", -1)
        if (deckIndex == -1) {
            Toast.makeText(this, "Invalid deck!", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        deck = FlashRepository.getInstance(this).decks[deckIndex]

        // Demo-friendly predictable order
        cards = deck.cards.toMutableList()

        if (cards.isEmpty()) {
            Toast.makeText(this, "This deck has no cards!", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        showCard()

        binding.flashcardContainer.setOnTouchListener { _, event ->
            handleTouch(event)
            true
        }

        // Option buttons
        listOf(binding.option1, binding.option2, binding.option3, binding.option4).forEach { btn ->
            btn.setOnClickListener { checkAnswer(btn) }
        }
    }

    private fun showCard() {
        val card = cards[currentIndex]

        binding.tvFront.text = card.question
        binding.tvBack.text = card.answer
        correctAnswer = card.answer

        binding.tvFront.visibility = View.VISIBLE
        binding.tvBack.visibility = View.GONE
        isBackVisible = false

        binding.optionsContainer.visibility = View.VISIBLE

        resetOptionStyles()

        binding.tvProgress.text = "${currentIndex + 1}/${cards.size}"

        generateOptions(correctAnswer)
    }

    private fun generateOptions(correct: String) {

        val q = binding.tvFront.text.toString().lowercase()
        val wrong = mutableListOf<String>()
        val options = mutableListOf<String>()

        // 🔥 FIX: STRONG capital detection
        val category = when {
            "capital" in q -> "city"        // <-- high priority
            "city" in q -> "city"
            "country" in q || "nation" in q -> "country"
            "ocean" in q || "sea" in q || "water" in q -> "ocean"
            listOf("gas", "breathe", "survive").any { q.contains(it) } -> "gas"
            listOf("who", "found", "invent", "discover").any { q.contains(it) } -> "person"
            listOf("color", "shade").any { q.contains(it) } -> "color"
            listOf("how many", "number").any { q.contains(it) } -> "number"
            listOf("element", "chemical", "formula").any { q.contains(it) } -> "element"
            else -> "general"
        }

        // Pick distractor pool
        val distractorPool = when (category) {

            "gas" -> gasPool

            "city" -> fallbackCityPool

            "country" -> countryPool

            "ocean" -> oceanPool

            "person" -> foundersPool + scientistsPool

            "color" -> colorPool

            "number" -> numberPool

            "element" -> elementPool

            else -> {
                // better general fallback
                cards.map { it.answer }.distinct()
            }
        }

        // Remove correct answer
        val filtered = distractorPool.filter { it != correct }

        wrong.addAll(filtered.shuffled().take(3))

        while (wrong.size < 3) wrong.add(correct + "s")

        options.addAll(wrong)
        options.add(correct)
        options.shuffle()

        binding.option1.text = options[0]
        binding.option2.text = options[1]
        binding.option3.text = options[2]
        binding.option4.text = options[3]
    }

    private fun resetOptionStyles() {
        listOf(binding.option1, binding.option2, binding.option3, binding.option4).forEach {
            it.isEnabled = true
            it.setBackgroundColor(Color.parseColor("#DDE6E8EC"))
            it.setTextColor(Color.BLACK)
        }
    }

    private fun checkAnswer(button: Button) {
        val selected = button.text.toString()

        if (selected != correctAnswer) {
            val shake = AnimationUtils.loadAnimation(this, R.anim.shake)
            button.startAnimation(shake)

            button.setBackgroundColor(Color.RED)
            Toast.makeText(this, "Incorrect — Try Again", Toast.LENGTH_SHORT).show()
            button.isEnabled = false
            return
        }

        button.setBackgroundColor(Color.GREEN)

        val allButtons = listOf(binding.option1, binding.option2, binding.option3, binding.option4)
        allButtons.forEach { it.isEnabled = false }

        binding.tvFront.visibility = View.GONE
        binding.tvBack.visibility = View.VISIBLE
        binding.optionsContainer.visibility = View.GONE

        binding.flashcardContainer.postDelayed({
            nextCard()
        }, 900)
    }

    private fun flipCard() {
        if (!isBackVisible) {
            binding.tvFront.visibility = View.GONE
            binding.tvBack.visibility = View.VISIBLE
            binding.optionsContainer.visibility = View.GONE
        } else {
            binding.tvFront.visibility = View.VISIBLE
            binding.tvBack.visibility = View.GONE
            binding.optionsContainer.visibility = View.VISIBLE
        }
        isBackVisible = !isBackVisible
    }

    private fun handleTouch(event: MotionEvent) {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                touchX = event.x
                touchY = event.y
            }
            MotionEvent.ACTION_UP -> {
                val deltaX = event.x - touchX
                val deltaY = event.y - touchY

                if (abs(deltaX) < TAP_THRESHOLD && abs(deltaY) < TAP_THRESHOLD) {
                    flipCard()
                    return
                }

                if (abs(deltaX) > SWIPE_THRESHOLD) {
                    if (deltaX < 0) nextCard() else prevCard()
                }
            }
        }
    }

    private fun nextCard() {
        if (currentIndex < cards.size - 1) {
            currentIndex++
            showCard()
        } else {
            Toast.makeText(this, "End of deck!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun prevCard() {
        if (currentIndex > 0) {
            currentIndex--
            showCard()
        } else {
            Toast.makeText(this, "Start of deck!", Toast.LENGTH_SHORT).show()
        }
    }
}
