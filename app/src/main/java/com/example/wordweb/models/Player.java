package com.example.wordweb.models;

public class Player {
    private String name;
    private String word;
    private boolean isUndercover;
    private int votes;
    private boolean hasVoted;

    public Player(String name) {
        this.name = name;
        this.votes = 0;
        this.hasVoted = false;
    }

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getWord() { return word; }
    public void setWord(String word) { this.word = word; }

    public boolean isUndercover() { return isUndercover; }
    public void setUndercover(boolean undercover) { isUndercover = undercover; }

    public int getVotes() { return votes; }
    public void setVotes(int votes) { this.votes = votes; }
    public void addVote() { this.votes++; }

    public boolean hasVoted() { return hasVoted; }
    public void setHasVoted(boolean hasVoted) { this.hasVoted = hasVoted; }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Player player = (Player) obj;
        return name.equals(player.name);
    }
}
