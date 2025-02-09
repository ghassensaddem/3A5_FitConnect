package com.esprit.services;

import com.esprit.models.Commande;
import com.esprit.utils.DataSource;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CommandeService {

    private Connection connection = DataSource.getInstance().getConnection();

    // Ajouter une commande
    public void ajouter(Commande commande) {
        String req = "INSERT INTO commande (client_id, etat, date_livraison, statut_paiement) VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setInt(1, commande.getClientId());
            pst.setString(2, commande.getEtat());
            if (commande.getDateLivraison() != null) {
                pst.setTimestamp(3, Timestamp.valueOf(commande.getDateLivraison()));
            } else {
                pst.setNull(3, Types.TIMESTAMP);
            }
            pst.setString(4, commande.getStatutPaiement());

            pst.executeUpdate();
            System.out.println("Commande ajoutée avec succès !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout de la commande : " + e.getMessage());
        }
    }

    // Modifier une commande
    public void modifier(Commande commande) {
        String req = "UPDATE commande SET client_id=?, etat=?, date_livraison=?, statut_paiement=? WHERE id=?";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setInt(1, commande.getClientId());
            pst.setString(2, commande.getEtat());
            if (commande.getDateLivraison() != null) {
                pst.setTimestamp(3, Timestamp.valueOf(commande.getDateLivraison()));
            } else {
                pst.setNull(3, Types.TIMESTAMP);
            }
            pst.setString(4, commande.getStatutPaiement());
            pst.setInt(5, commande.getId());

            pst.executeUpdate();
            System.out.println("Commande modifiée avec succès !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de la modification de la commande : " + e.getMessage());
        }
    }

    // Supprimer une commande
    public void supprimer(Commande commande) {
        String req = "DELETE FROM commande WHERE id=?";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setInt(1, commande.getId());
            pst.executeUpdate();
            System.out.println("Commande supprimée avec succès !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression de la commande : " + e.getMessage());
        }
    }

    // Récupérer toutes les commandes
    public List<Commande> rechercher() {
        List<Commande> commandes = new ArrayList<>();
        String req = "SELECT * FROM commande";

        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(req);

            while (rs.next()) {
                Commande commande = new Commande(
                        rs.getInt("id"),
                        rs.getInt("client_id"),
                        rs.getString("etat"),
                        rs.getTimestamp("date_livraison") != null ? rs.getTimestamp("date_livraison").toLocalDateTime() : null,
                        rs.getString("statut_paiement")
                );
                commandes.add(commande);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des commandes : " + e.getMessage());
        }

        return commandes;
    }

    // Récupérer une commande par son ID
   /* public Commande rechercherParId(int id) {
        String req = "SELECT * FROM commande WHERE id=?";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                return new Commande(
                        rs.getInt("id"),
                        rs.getInt("client_id"),
                        rs.getString("etat"),
                        rs.getTimestamp("date_livraison") != null ? rs.getTimestamp("date_livraison").toLocalDateTime() : null,
                        rs.getString("statut_paiement")
                );
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération de la commande : " + e.getMessage());
        }

        return null;
    }

    // Récupérer les commandes d'un client spécifique
    public List<Commande> rechercherParClientId(int clientId) {
        List<Commande> commandes = new ArrayList<>();
        String req = "SELECT * FROM commande WHERE client_id=?";

        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setInt(1, clientId);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                Commande commande = new Commande(
                        rs.getInt("id"),
                        rs.getInt("client_id"),
                        rs.getString("etat"),
                        rs.getTimestamp("date_livraison") != null ? rs.getTimestamp("date_livraison").toLocalDateTime() : null,
                        rs.getString("statut_paiement")
                );
                commandes.add(commande);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des commandes du client : " + e.getMessage());
        }

        return commandes;
    }*/
}