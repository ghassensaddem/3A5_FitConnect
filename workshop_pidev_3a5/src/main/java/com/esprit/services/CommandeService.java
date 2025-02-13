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
        String req = "INSERT INTO commande (clientId, equipementId, etat, dateLivraison, statutPaiement, quantite, dateCreation) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setInt(1, commande.getClientId());
            pst.setInt(2, commande.getEquipementId());
            pst.setString(3, commande.getEtat());
            if (commande.getDateLivraison() != null) {
                pst.setTimestamp(4, Timestamp.valueOf(commande.getDateLivraison()));
            } else {
                pst.setNull(4, Types.TIMESTAMP);
            }
            pst.setString(5, commande.getStatutPaiement());
            pst.setInt(6, commande.getQuantite());
            pst.setTimestamp(7, Timestamp.valueOf(LocalDateTime.now())); // Date de création automatique

            pst.executeUpdate();
            System.out.println("Commande ajoutée avec succès !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout de la commande : " + e.getMessage());
        }
    }

    // Modifier une commande
    public void modifier(Commande commande) {
        String req = "UPDATE commande SET clientId=?, equipementId=?, etat=?, dateLivraison=?, statutPaiement=?, quantite=? WHERE id=?";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setInt(1, commande.getClientId());
            pst.setInt(2, commande.getEquipementId());
            pst.setString(3, commande.getEtat());
            if (commande.getDateLivraison() != null) {
                pst.setTimestamp(4, Timestamp.valueOf(commande.getDateLivraison()));
            } else {
                pst.setNull(4, Types.TIMESTAMP);
            }
            pst.setString(5, commande.getStatutPaiement());
            pst.setInt(6, commande.getQuantite());
            pst.setInt(7, commande.getId());

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
                        rs.getInt("id"), // Récupère l'ID de la commande
                        rs.getInt("clientId"), // Récupère l'ID du client
                        rs.getInt("equipementId"), // Récupère l'ID de l'équipement
                        rs.getString("etat"), // Récupère l'état de la commande
                        rs.getTimestamp("dateLivraison") != null ? rs.getTimestamp("dateLivraison").toLocalDateTime() : null, // Récupère la date de livraison
                        rs.getString("statutPaiement"), // Récupère le statut du paiement
                        rs.getInt("quantite"), // Récupère la quantité de l'équipement
                        rs.getTimestamp("dateCreation") != null ? rs.getTimestamp("dateCreation").toLocalDateTime() : null // Récupère la date de création depuis la base de données
                );
                commandes.add(commande);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des commandes : " + e.getMessage());
        }

        return commandes;
    }

    // Récupérer une commande par son ID
    /*public Commande rechercherParId(int id) {
        String req = "SELECT * FROM commande WHERE id=?";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                return new Commande(
                        rs.getInt("id"),
                        rs.getInt("client_id"),
                        rs.getInt("equipement_id"),
                        rs.getString("etat"),
                        rs.getTimestamp("date_livraison") != null ? rs.getTimestamp("date_livraison").toLocalDateTime() : null,
                        rs.getString("statut_paiement"),
                        rs.getInt("quantite")
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
                        rs.getInt("clientId"),
                        rs.getInt("equipementId"),
                        rs.getString("etat"),
                        rs.getTimestamp("dateLivraison") != null ? rs.getTimestamp("dateLivraison").toLocalDateTime() : null,
                        rs.getString("statutPaiement"),
                        rs.getInt("quantite"),
                                rs.getTimestamp("dateCreation") != null ? rs.getTimestamp("dateCreation").toLocalDateTime() : null,

                );
                commandes.add(commande);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des commandes du client : " + e.getMessage());
        }

        return commandes;
    }

     */
}