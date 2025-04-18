package com.esprit.models;

public class Vote {
    private int id;
    private int postId;
    private int clientId;
    private int voteType; // 1 = Upvote, -1 = Downvote, 0 = Annulé

    public Vote(int postId, int clientId, int voteType) {
        this.postId = postId;
        this.clientId = clientId;
        this.voteType = voteType;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getPostId() { return postId; }
    public void setPostId(int postId) { this.postId = postId; }

    public int getClientId() { return clientId; }
    public void setClientId(int clientId) { this.clientId = clientId; }

    public int getVoteType() { return voteType; }
    public void setVoteType(int voteType) { this.voteType = voteType; }
}
