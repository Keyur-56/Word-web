package com.example.wordweb.managers;

import com.example.wordweb.models.GameSession;
import com.example.wordweb.models.Player;

import java.util.List;
import java.util.Random;

public class GameManager {
    private GameSession gameSession;
    private WordManager wordManager;
    private Random random;

    public GameManager() {
        this.gameSession = new GameSession();
        this.wordManager = new WordManager();
        this.random = new Random();
    }

    public void initializeGame() {
        // Create 4 players
        String[] playerNames = {"Player 1", "Player 2", "Player 3", "Player 4"};
        for (String name : playerNames) {
            gameSession.addPlayer(new Player(name));
        }

        startNewRound();
    }

    public void startNewRound() {
        gameSession.resetVotes();

        // Get word pair
        String[] wordPair = wordManager.getRandomWordPair();
        String commonWord = wordPair[0];
        String undercoverWord = wordPair[1];

        // Select random undercover
        int undercoverIndex = random.nextInt(4);

        // Assign words
        List<Player> players = gameSession.getPlayers();
        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);
            if (i == undercoverIndex) {
                player.setWord(undercoverWord);
                player.setUndercover(true);
            } else {
                player.setWord(commonWord);
                player.setUndercover(false);
            }
        }
    }

    public void addVote(Player votedPlayer) {
        votedPlayer.addVote();

        // Mark current player as voted (simplified - in real app track who voted)
        for (Player player : gameSession.getPlayers()) {
            if (!player.hasVoted()) {
                player.setHasVoted(true);
                break;
            }
        }
    }

    public boolean allPlayersVoted() {
        for (Player player : gameSession.getPlayers()) {
            if (!player.hasVoted()) {
                return false;
            }
        }
        return true;
    }

    public Player getUndercoverPlayer() {
        for (Player player : gameSession.getPlayers()) {
            if (player.isUndercover()) {
                return player;
            }
        }
        return null;
    }

    public Player getMostVotedPlayer() {
        Player mostVoted = null;
        int maxVotes = -1;

        for (Player player : gameSession.getPlayers()) {
            if (player.getVotes() > maxVotes) {
                maxVotes = player.getVotes();
                mostVoted = player;
            }
        }

        return mostVoted;
    }

    public void prepareNextRound() {
        gameSession.nextRound();
    }

    // Getters
    public List<Player> getPlayers() {
        return gameSession.getPlayers();
    }

    public int getCurrentRound() {
        return gameSession.getCurrentRound();
    }

    public Player getCurrentPlayer() {
        // Simplified - in real app you'd track current player
        return gameSession.getPlayers().get(0);
    }
}