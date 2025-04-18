package com.esprit.models;



public class VoteCom {
    private int id;
    private int commentId;
    private int clientId;
    private int voteType; // 1 = Upvote, -1 = Downvote, 0 = Annulé

    public VoteCom(int commentId, int clientId, int voteType) {
        this.commentId = commentId;
        this.clientId = clientId;
        this.voteType = voteType;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getCommentId() { return commentId; }
    public void setCommentId(int commentId) { this.commentId = commentId; }

    public int getClientId() { return clientId; }
    public void setClientId(int clientId) { this.clientId = clientId; }

    public int getVoteType() { return voteType; }
    public void setVoteType(int voteType) { this.voteType = voteType; }

    @Override
    public String toString() {
        return "VoteCom{" +
                "id=" + id +
                ", commentId=" + commentId +
                ", clientId=" + clientId +
                ", voteType=" + voteType +
                '}';
    }
}
