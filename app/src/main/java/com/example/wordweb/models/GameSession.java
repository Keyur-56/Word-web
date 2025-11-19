package com.example.wordweb.models;

import java.util.ArrayList;
import java.util.List;

public class GameSession {
    private List<Player> players;
    private int currentRound;
    private boolean gameActive;

    public GameSession() {
        this.players = new ArrayList<>();
        this.currentRound = 1;
        this.gameActive = true;
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    public List<Player> getPlayers() {
        return players;
    }

    public int getCurrentRound() {
        return currentRound;
    }

    public void nextRound() {
        currentRound++;
    }

    public boolean isGameActive() {
        return gameActive;
    }

    public void setGameActive(boolean gameActive) {
        this.gameActive = gameActive;
    }

    public void resetVotes() {
        for (Player player : players) {
            player.setVotes(0);
            player.setHasVoted(false);
        }
    }
}
