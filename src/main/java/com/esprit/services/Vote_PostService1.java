package com.esprit.services;

import com.esprit.utils.DataSource;

import java.sql.*;

public class Vote_PostService1 {
    private Connection connection = DataSource.getInstance().getConnection();

    public void voter(int postId, int clientId, int voteType) {
        try {
            // Vérifier si l'utilisateur a déjà voté pour ce post
            String checkVoteQuery = "SELECT id, vote_type FROM vote WHERE post_id = ? AND client_id = ?";
            PreparedStatement checkStmt = connection.prepareStatement(checkVoteQuery);
            checkStmt.setInt(1, postId);
            checkStmt.setInt(2, clientId);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                int existingVoteType = rs.getInt("vote_type");
                int voteId = rs.getInt("id");

                if (existingVoteType == voteType) {
                    // L'utilisateur a déjà voté du même type, donc on annule son vote
                    String deleteVoteQuery = "DELETE FROM vote WHERE id = ?";
                    PreparedStatement deleteStmt = connection.prepareStatement(deleteVoteQuery);
                    deleteStmt.setInt(1, voteId);
                    deleteStmt.executeUpdate();
                    System.out.println("Vote annulé !");
                } else {
                    // L'utilisateur change de vote (de upvote -> downvote ou inversement)
                    // Supprimer l'ancien vote
                    String deleteVoteQuery = "DELETE FROM vote WHERE id = ?";
                    PreparedStatement deleteStmt = connection.prepareStatement(deleteVoteQuery);
                    deleteStmt.setInt(1, voteId);
                    deleteStmt.executeUpdate();
                    System.out.println("Ancien vote supprimé");

                    // Ajouter le nouveau vote
                    String insertVoteQuery = "INSERT INTO vote (post_id, client_id, vote_type) VALUES (?, ?, ?)";
                    PreparedStatement insertStmt = connection.prepareStatement(insertVoteQuery);
                    insertStmt.setInt(1, postId);
                    insertStmt.setInt(2, clientId);
                    insertStmt.setInt(3, voteType);
                    insertStmt.executeUpdate();
                    System.out.println("Nouveau vote ajouté !");
                }
            } else {
                // L'utilisateur n'a jamais voté pour ce post, ajouter son vote
                String insertVoteQuery = "INSERT INTO vote (post_id, client_id, vote_type) VALUES (?, ?, ?)";
                PreparedStatement insertStmt = connection.prepareStatement(insertVoteQuery);
                insertStmt.setInt(1, postId);
                insertStmt.setInt(2, clientId);
                insertStmt.setInt(3, voteType);
                insertStmt.executeUpdate();
                System.out.println("Vote ajouté !");
            }

            // Mise à jour des upvotes et downvotes dans la table `post`
            updatePostVotes(postId);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public int countUpvotes(int postId) {
        return countVotes(postId, 1);
    }

    public int countDownvotes(int postId) {
        return countVotes(postId, -1);
    }

    private int countVotes(int postId, int voteType) {
        int count = 0;
        try {
            String query = "SELECT COUNT(*) FROM vote WHERE post_id = ? AND vote_type = ?";
            PreparedStatement pst = connection.prepareStatement(query);
            pst.setInt(1, postId);
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


    public int getVoteType(int postId, int clientId) {
        int voteType = 0; // 0 signifie aucun vote
        try {
            String query = "SELECT vote_type FROM vote WHERE post_id = ? AND client_id = ?";
            PreparedStatement pst = connection.prepareStatement(query);
            pst.setInt(1, postId);
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


    private void updatePostVotes(int postId) {
        try {
            // Calculer le nombre d'upvotes et de downvotes
            String countVotesQuery = "SELECT SUM(CASE WHEN vote_type = 1 THEN 1 ELSE 0 END) AS upvotes, " +
                    "SUM(CASE WHEN vote_type = -1 THEN 1 ELSE 0 END) AS downvotes " +
                    "FROM vote WHERE post_id = ?";
            PreparedStatement countStmt = connection.prepareStatement(countVotesQuery);
            countStmt.setInt(1, postId);
            ResultSet rs = countStmt.executeQuery();

            if (rs.next()) {
                int upvotes = rs.getInt("upvotes");
                int downvotes = rs.getInt("downvotes");

                // Mettre à jour les champs upvotes et downvotes du post
                String updatePostVotesQuery = "UPDATE post SET upvotes = ?, downvotes = ? WHERE id = ?";
                PreparedStatement updateStmt = connection.prepareStatement(updatePostVotesQuery);
                updateStmt.setInt(1, upvotes);
                updateStmt.setInt(2, downvotes);
                updateStmt.setInt(3, postId);
                updateStmt.executeUpdate();
                System.out.println("Votes du post mis à jour !");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
