package com.esprit.services;

import com.esprit.utils.DataSource;

import java.sql.*;

public class Vote_ComService1 {
    private Connection connection = DataSource.getInstance().getConnection();

    public void voter(int commentId, int clientId, int voteType) {
        try {
            // Vérifier si l'utilisateur a déjà voté pour ce commentaire
            String checkVoteQuery = "SELECT id, vote_type FROM vote_com WHERE comment_id = ? AND client_id = ?";
            PreparedStatement checkStmt = connection.prepareStatement(checkVoteQuery);
            checkStmt.setInt(1, commentId);
            checkStmt.setInt(2, clientId);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                int existingVoteType = rs.getInt("vote_type");
                int voteId = rs.getInt("id");

                if (existingVoteType == voteType) {
                    // Annuler le vote
                    String deleteVoteQuery = "DELETE FROM vote_com WHERE id = ?";
                    PreparedStatement deleteStmt = connection.prepareStatement(deleteVoteQuery);
                    deleteStmt.setInt(1, voteId);
                    deleteStmt.executeUpdate();
                    System.out.println("Vote annulé !");
                } else {
                    // Changer le vote
                    String deleteVoteQuery = "DELETE FROM vote_com WHERE id = ?";
                    PreparedStatement deleteStmt = connection.prepareStatement(deleteVoteQuery);
                    deleteStmt.setInt(1, voteId);
                    deleteStmt.executeUpdate();
                    System.out.println("Ancien vote supprimé");

                    // Ajouter le nouveau vote
                    String insertVoteQuery = "INSERT INTO vote_com (comment_id, client_id, vote_type) VALUES (?, ?, ?)";
                    PreparedStatement insertStmt = connection.prepareStatement(insertVoteQuery);
                    insertStmt.setInt(1, commentId);
                    insertStmt.setInt(2, clientId);
                    insertStmt.setInt(3, voteType);
                    insertStmt.executeUpdate();
                    System.out.println("Nouveau vote ajouté !");
                }
            } else {
                // Ajouter un nouveau vote
                String insertVoteQuery = "INSERT INTO vote_com (comment_id, client_id, vote_type) VALUES (?, ?, ?)";
                PreparedStatement insertStmt = connection.prepareStatement(insertVoteQuery);
                insertStmt.setInt(1, commentId);
                insertStmt.setInt(2, clientId);
                insertStmt.setInt(3, voteType);
                insertStmt.executeUpdate();
                System.out.println("Vote ajouté !");
            }

            // Mise à jour des upvotes et downvotes dans la table `comment`
            updateCommentVotes(commentId);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public int countUpvotes(int commentId) {
        return countVotes(commentId, 1);
    }

    public int countDownvotes(int commentId) {
        return countVotes(commentId, -1);
    }

    private int countVotes(int commentId, int voteType) {
        int count = 0;
        try {
            String query = "SELECT COUNT(*) FROM vote_com WHERE comment_id = ? AND vote_type = ?";
            PreparedStatement pst = connection.prepareStatement(query);
            pst.setInt(1, commentId);
            pst.setInt(2, voteType);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return count;
    }

    public int getVoteType(int commentId, int clientId) {
        int voteType = 0; // 0 signifie aucun vote
        try {
            String query = "SELECT vote_type FROM vote_com WHERE comment_id = ? AND client_id = ?";
            PreparedStatement pst = connection.prepareStatement(query);
            pst.setInt(1, commentId);
            pst.setInt(2, clientId);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                voteType = rs.getInt("vote_type");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return voteType;
    }

    private void updateCommentVotes(int commentId) {
        try {
            // Calculer le nombre d'upvotes et de downvotes
            String countVotesQuery = "SELECT SUM(CASE WHEN vote_type = 1 THEN 1 ELSE 0 END) AS upvotes, " +
                    "SUM(CASE WHEN vote_type = -1 THEN 1 ELSE 0 END) AS downvotes " +
                    "FROM vote_com WHERE comment_id = ?";
            PreparedStatement countStmt = connection.prepareStatement(countVotesQuery);
            countStmt.setInt(1, commentId);
            ResultSet rs = countStmt.executeQuery();

            if (rs.next()) {
                int upvotes = rs.getInt("upvotes");
                int downvotes = rs.getInt("downvotes");

                // Mettre à jour les champs upvotes et downvotes du commentaire
                String updateCommentVotesQuery = "UPDATE commentaire SET upvotes = ?, downvotes = ? WHERE id = ?";
                PreparedStatement updateStmt = connection.prepareStatement(updateCommentVotesQuery);
                updateStmt.setInt(1, upvotes);
                updateStmt.setInt(2, downvotes);
                updateStmt.setInt(3, commentId);
                updateStmt.executeUpdate();
                System.out.println("Votes du commentaire mis à jour !");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
