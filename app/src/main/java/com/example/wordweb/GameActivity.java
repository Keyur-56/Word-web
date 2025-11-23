package com.example.wordweb;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class GameActivity extends AppCompatActivity {

    private TextView tvWord, tvGameState, tvCurrentPlayer;
    private Button btnStartRound, btnReveal, btnNextRound, btnNextPlayer, btnNextVoter, btnReady;
    private LinearLayout playersLayout;

    private List<Player> players;
    private List<String[]> wordPairs;
    private String[] currentWordPair;
    private int undercoverPlayerIndex = -1;
    private boolean isVotingPhase = false;
    private int currentPlayerTurn = 0;
    private int currentVoterTurn = 0;
    private int votesCount = 0;
    private boolean isReadyPhase = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        initializeViews();
        setupWordPairs();
        setupGame();
        createPlayerCards();
    }

    private void initializeViews() {
        tvWord = findViewById(R.id.tvWord);
        tvGameState = findViewById(R.id.tvGameState);
        tvCurrentPlayer = findViewById(R.id.tvCurrentPlayer);
        btnStartRound = findViewById(R.id.btnStartRound);
        btnReveal = findViewById(R.id.btnReveal);
        btnNextRound = findViewById(R.id.btnNextRound);
        btnNextPlayer = findViewById(R.id.btnNextPlayer);
        btnNextVoter = findViewById(R.id.btnNextVoter);
        btnReady = findViewById(R.id.btnReady);
        playersLayout = findViewById(R.id.playersLayout);

        // Hide elements initially
        tvWord.setVisibility(View.GONE);
        btnReveal.setVisibility(View.GONE);
        btnNextRound.setVisibility(View.GONE);
        btnNextPlayer.setVisibility(View.GONE);
        btnNextVoter.setVisibility(View.GONE);
        btnReady.setVisibility(View.GONE);
        playersLayout.setVisibility(View.GONE);
        tvCurrentPlayer.setVisibility(View.GONE);
    }

    private void setupWordPairs() {
        wordPairs = Arrays.asList(
                new String[]{"Restaurant", "Cafeteria"},
                new String[]{"Beach", "Desert"},
                new String[]{"Mountain", "Hill"},
                new String[]{"River", "Stream"},
                new String[]{"Car", "Bicycle"},
                new String[]{"Phone", "Telephone"},
                new String[]{"Computer", "Laptop"},
                new String[]{"Book", "Novel"},
                new String[]{"Movie", "Film"},
                new String[]{"Music", "Song"},
                new String[]{"Coffee", "Tea"},
                new String[]{"Pizza", "Burger"},
                new String[]{"Football", "Soccer"},
                new String[]{"School", "College"},
                new String[]{"House", "Apartment"}
        );
    }

    private void setupGame() {
        // Create 4 players
        players = new ArrayList<>();
        players.add(new Player("Player 1"));
        players.add(new Player("Player 2"));
        players.add(new Player("Player 3"));
        players.add(new Player("Player 4"));

        btnStartRound.setOnClickListener(v -> startGameRound());
        btnReveal.setOnClickListener(v -> startVotingPhase());
        btnNextRound.setOnClickListener(v -> nextRound());
        btnNextPlayer.setOnClickListener(v -> showNextPlayerWord());
        btnNextVoter.setOnClickListener(v -> showNextVoter());
        btnReady.setOnClickListener(v -> showPlayerRole());
    }

    private void createPlayerCards() {
        playersLayout.removeAllViews();

        for (int i = 0; i < players.size(); i++) {
            CardView playerCard = new CardView(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(16, 8, 16, 8);
            playerCard.setLayoutParams(params);
            playerCard.setCardBackgroundColor(Color.parseColor("#4A5568"));
            playerCard.setCardElevation(8);
            playerCard.setRadius(16);
            playerCard.setContentPadding(32, 32, 32, 32);

            TextView playerName = new TextView(this);
            playerName.setText(players.get(i).getName());
            playerName.setTextColor(Color.WHITE);
            playerName.setTextSize(18);
            playerName.setTypeface(null, Typeface.BOLD);

            playerCard.addView(playerName);
            final int playerIndex = i;
            playerCard.setOnClickListener(v -> onPlayerVoted(playerIndex));

            playersLayout.addView(playerCard);
        }
    }

    private void startGameRound() {
        // Reset game state
        resetGame();

        // Randomly select word pair
        Random random = new Random();
        currentWordPair = wordPairs.get(random.nextInt(wordPairs.size()));

        // Randomly select undercover player
        undercoverPlayerIndex = random.nextInt(4);

        // Assign words
        for (int i = 0; i < players.size(); i++) {
            if (i == undercoverPlayerIndex) {
                players.get(i).setWord(currentWordPair[1]); // Undercover word
                players.get(i).setUndercover(true);
            } else {
                players.get(i).setWord(currentWordPair[0]); // Common word
                players.get(i).setUndercover(false);
            }
        }

        // Start showing ready screen to players one by one
        currentPlayerTurn = 0;
        showReadyScreen();

        // Update UI
        playersLayout.setVisibility(View.VISIBLE);
        btnStartRound.setVisibility(View.GONE);
        tvCurrentPlayer.setVisibility(View.VISIBLE);

        tvGameState.setText("Pass the device to each player");
    }

    private void showReadyScreen() {
        Player currentPlayer = players.get(currentPlayerTurn);

        tvCurrentPlayer.setText("Current: " + currentPlayer.getName());
        tvWord.setText("Pass the device to " + currentPlayer.getName() + "\n\nWhen ready, click the button below to see your role!");

        tvWord.setVisibility(View.VISIBLE);
        btnReady.setVisibility(View.VISIBLE);
        btnNextPlayer.setVisibility(View.GONE);

        // Highlight current player
        highlightCurrentPlayer(currentPlayerTurn);

        isReadyPhase = true;
    }

    private void showPlayerRole() {
        if (!isReadyPhase) return;

        Player currentPlayer = players.get(currentPlayerTurn);

        tvWord.setText(currentPlayer.getName() + "'s Role:\n\nWord: " + currentPlayer.getWord() +
                "\n\nYou are: " + (currentPlayer.isUndercover() ? "👤 UNDERCOVER!" : "👥 WORKER") +
                "\n\nRemember your word and role!");

        btnReady.setVisibility(View.GONE);
        btnNextPlayer.setVisibility(View.VISIBLE);

        if (currentPlayerTurn == players.size() - 1) {
            btnNextPlayer.setText("Finish Viewing");
        }

        isReadyPhase = false;
    }

    private void showNextPlayerWord() {
        currentPlayerTurn++;

        if (currentPlayerTurn < players.size()) {
            showReadyScreen();
        } else {
            // All players have seen their words
            finishWordReveal();
        }
    }

    private void finishWordReveal() {
        tvWord.setVisibility(View.GONE);
        tvCurrentPlayer.setVisibility(View.GONE);
        btnNextPlayer.setVisibility(View.GONE);
        btnReveal.setVisibility(View.VISIBLE);

        tvGameState.setText("All players have seen their roles!\nDiscuss and find the undercover!");
        Toast.makeText(this, "Discuss with other players! Find the undercover!", Toast.LENGTH_LONG).show();

        // Reset player highlights
        resetPlayerHighlights();
    }

    private void startVotingPhase() {
        isVotingPhase = true;
        currentVoterTurn = 0;
        votesCount = 0;

        btnReveal.setVisibility(View.GONE);
        btnNextVoter.setVisibility(View.VISIBLE);
        tvCurrentPlayer.setVisibility(View.VISIBLE);

        tvGameState.setText("Voting Phase");
        showCurrentVoter();

        Toast.makeText(this, "Voting started! Pass device to each voter.", Toast.LENGTH_LONG).show();
    }

    private void showCurrentVoter() {
        Player currentVoter = players.get(currentVoterTurn);

        tvCurrentPlayer.setText("Voting: " + currentVoter.getName());
        tvGameState.setText(currentVoter.getName() + ", tap to vote for suspected undercover!");

        // Enable voting for current voter only
        enableVotingForCurrentVoter();

        if (currentVoterTurn == players.size() - 1) {
            btnNextVoter.setText("Finish Voting");
        }
    }

    private void showNextVoter() {
        // Disable voting for previous voter
        disableVotingForAll();

        currentVoterTurn++;

        if (currentVoterTurn < players.size()) {
            showCurrentVoter();
        } else {
            // All players have voted
            finishVoting();
        }
    }

    private void finishVoting() {
        tvCurrentPlayer.setVisibility(View.GONE);
        btnNextVoter.setVisibility(View.GONE);
        revealResults();
    }

    private void onPlayerVoted(int playerIndex) {
        if (!isVotingPhase || currentVoterTurn >= players.size()) return;

        Player votedPlayer = players.get(playerIndex);
        Player currentVoter = players.get(currentVoterTurn);

        // Check if player is voting for themselves
        if (playerIndex == currentVoterTurn) {
            Toast.makeText(this, "You cannot vote for yourself!", Toast.LENGTH_SHORT).show();
            return;
        }

        votedPlayer.addVote();
        votesCount++;

        // Visual feedback
        updatePlayerCard(playerIndex);

        Toast.makeText(this, currentVoter.getName() + " voted for " + votedPlayer.getName(), Toast.LENGTH_SHORT).show();

        // Auto-advance to next voter after short delay
        new android.os.Handler().postDelayed(() -> {
            showNextVoter();
        }, 1500);
    }

    private void updatePlayerCard(int playerIndex) {
        CardView playerCard = (CardView) playersLayout.getChildAt(playerIndex);
        TextView textView = (TextView) playerCard.getChildAt(0);

        Player player = players.get(playerIndex);
        String displayText = player.getName() + "\nVotes: " + player.getVotes();
        textView.setText(displayText);

        // Highlight voted player
        playerCard.setCardBackgroundColor(Color.parseColor("#2D3748"));
    }

    private void revealResults() {
        // Find most voted player
        Player mostVoted = getMostVotedPlayer();
        Player undercover = players.get(undercoverPlayerIndex);

        boolean undercoverCaught = mostVoted.equals(undercover);

        // Show results
        String resultText;
        if (undercoverCaught) {
            resultText = "🎉 UNDERCOVER CAUGHT!\n" + undercover.getName() + " was the undercover!\nWorkers Win!";
            tvGameState.setTextColor(Color.GREEN);
        } else {
            resultText = "😎 UNDERCOVER ESCAPED!\n" + undercover.getName() + " was the undercover!\nUndercover Wins!";
            tvGameState.setTextColor(Color.RED);
        }

        // Reveal all roles
        StringBuilder wordsInfo = new StringBuilder("Game Results:\n\n");
        wordsInfo.append("Common Word: ").append(currentWordPair[0]).append("\n");
        wordsInfo.append("Undercover Word: ").append(currentWordPair[1]).append("\n\n");

        for (Player player : players) {
            String role = player.isUndercover() ? "👤 UNDERCOVER" : "👥 WORKER";
            wordsInfo.append(player.getName()).append(": ")
                    .append(player.getWord()).append("\n(").append(role).append(") - Votes: ").append(player.getVotes()).append("\n\n");
        }

        tvWord.setText(wordsInfo.toString());
        tvWord.setVisibility(View.VISIBLE);
        tvGameState.setText(resultText);
        btnNextRound.setVisibility(View.VISIBLE);

        // Highlight the actual undercover
        highlightUndercoverPlayer();
    }

    private void highlightCurrentPlayer(int playerIndex) {
        resetPlayerHighlights();
        CardView currentCard = (CardView) playersLayout.getChildAt(playerIndex);
        currentCard.setCardBackgroundColor(Color.parseColor("#667eea"));
    }

    private void highlightUndercoverPlayer() {
        CardView undercoverCard = (CardView) playersLayout.getChildAt(undercoverPlayerIndex);
        undercoverCard.setCardBackgroundColor(Color.parseColor("#FF6B6B"));

        TextView textView = (TextView) undercoverCard.getChildAt(0);
        textView.setText(players.get(undercoverPlayerIndex).getName() + "\n👤 UNDERCOVER");
    }

    private void resetPlayerHighlights() {
        for (int i = 0; i < playersLayout.getChildCount(); i++) {
            CardView playerCard = (CardView) playersLayout.getChildAt(i);
            playerCard.setCardBackgroundColor(Color.parseColor("#4A5568"));

            TextView textView = (TextView) playerCard.getChildAt(0);
            textView.setText(players.get(i).getName());
        }
    }

    private void enableVotingForCurrentVoter() {
        for (int i = 0; i < playersLayout.getChildCount(); i++) {
            CardView playerCard = (CardView) playersLayout.getChildAt(i);
            playerCard.setClickable(i != currentVoterTurn); // Can't vote for yourself
            playerCard.setCardBackgroundColor(Color.parseColor("#4A5568"));
        }

        // Highlight current voter
        CardView voterCard = (CardView) playersLayout.getChildAt(currentVoterTurn);
        voterCard.setCardBackgroundColor(Color.parseColor("#667eea"));
    }

    private void disableVotingForAll() {
        for (int i = 0; i < playersLayout.getChildCount(); i++) {
            CardView playerCard = (CardView) playersLayout.getChildAt(i);
            playerCard.setClickable(false);
        }
    }

    private Player getMostVotedPlayer() {
        Player mostVoted = players.get(0);
        for (Player player : players) {
            if (player.getVotes() > mostVoted.getVotes()) {
                mostVoted = player;
            }
        }
        return mostVoted;
    }

    private void resetGame() {
        isVotingPhase = false;
        isReadyPhase = false;
        currentPlayerTurn = 0;
        currentVoterTurn = 0;
        votesCount = 0;
        undercoverPlayerIndex = -1;

        for (Player player : players) {
            player.setVotes(0);
        }

        createPlayerCards();
    }

    private void nextRound() {
        resetGame();

        // Reset UI
        tvWord.setVisibility(View.GONE);
        btnNextRound.setVisibility(View.GONE);
        btnStartRound.setVisibility(View.VISIBLE);
        playersLayout.setVisibility(View.GONE);
        tvCurrentPlayer.setVisibility(View.GONE);
        tvGameState.setText("Ready for next round!");
        tvGameState.setTextColor(Color.WHITE);

        Toast.makeText(this, "Starting new round!", Toast.LENGTH_SHORT).show();
    }

    // Player class inside GameActivity
    class Player {
        private String name;
        private String word;
        private boolean isUndercover;
        private int votes;

        public Player(String name) {
            this.name = name;
            this.votes = 0;
        }

        public String getName() { return name; }
        public String getWord() { return word; }
        public void setWord(String word) { this.word = word; }
        public boolean isUndercover() { return isUndercover; }
        public void setUndercover(boolean undercover) { isUndercover = undercover; }
        public int getVotes() { return votes; }
        public void setVotes(int votes) { this.votes = votes; }
        public void addVote() { this.votes++; }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            Player player = (Player) obj;
            return name.equals(player.name);
        }
    }
}