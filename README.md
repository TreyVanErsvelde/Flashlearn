FlashLearn – Smart Flashcard & Trivia Learning App

A modern Android learning app with smart AI-like distractors, multiple study modes, beautiful UI, and swipe-friendly flashcards.

 Overview

FlashLearn is an Android study application designed to help users learn efficiently through:

Flashcard Mode – flipping between questions and answers

Trivia Mode – a multiple-choice quiz engine

Smart AI-like Distractor Generation (C1 Engine)

Shake animation for incorrect answers

Option highlighting (green = correct, red = incorrect)

Swipe navigation for cards

This project demonstrates Android UI/UX development, Kotlin logic, offline AI-style generation, and clean app architecture.

Features
 C1 Smart Distractor Engine (Offline AI)

FlashLearn includes a custom-built distractor generator that creates realistic multiple-choice options without using any external APIs:

Analyzes question semantics

Detects topics (chemistry, geography, founders, etc.)

Chooses options similar to the correct answer

Eliminates unrelated or silly options

Ensures variety and avoids duplicates

Fully offline (no API keys needed)

This achieves AI-like behavior while keeping the app lightweight.

 2. Trivia Mode

A polished interactive quiz mode:

Four-option multiple choice
Wrong answer shakes (shake animation)
Wrong answer = “Incorrect — Try Again” (no auto-advance)
Correct answer = reveal + auto-advance
Clean UI switching between question → answer → next question
Randomized options every round

 3. Flashcard Mode

Swipe-based flashcards:

Swipe left/right to move between cards

Tap to flip card

Smooth transitions

Tracks progress (1 / N)

 4. Modern UI

Material-style layout

Rounded option buttons

Theme matching

Smooth flip + tap interactions

Clean constraint layout

 5. Works Fully Offline

No internet required

No external ML models

No APIs

Everything runs on-device

 Tech Stack
Technology	Usage
Kotlin	Main programming language
Android Studio	Development environment
ConstraintLayout	UI structure
Animations (XML / Kotlin)	Shake feedback
Custom logic engine	Smart distractor generator
Git/GitHub	Version control
Project Structure
Flashlearn/
├── app/
│   ├── src/main/java/com/example/flashlearn/
│   │   ├── MainActivity.kt
│   │   ├── TriviaModeActivity.kt
│   │   ├── Deck.kt / Card.kt
│   │   └── FlashRepository.kt
│   ├── res/
│   │   ├── layout/activity_trivia_mode.xml
│   │   ├── anim/shake.xml
│   │   ├── drawable/option_button.xml
│   │   └── font/
│   └── AndroidManifest.xml
└── README.md

 How the Smart Distractor Engine Works

FlashLearn analyzes the question text using linguistic patterns:

“Who…?” → People distractors

“Capital of…?” → City distractors

“Gas humans need…” → Chemistry distractors

“How many…?” → Number distractors

“Ocean, Sea, River…” → Geography

Otherwise → fallback similarity matching

This keeps the app smart without using machine learning models.

 Installation

Clone the repository:

git clone https://github.com/hanamichi10-super/Flashlearn.git


Open the project in Android Studio

Sync Gradle

Run on emulator or physical device

No API keys needed.

 Contributing

Pull requests are welcome!
Ideas for improvement include:

Adding spaced repetition

Adding dark mode

Adding text-to-speech

Cloud sync

Export/import decks
