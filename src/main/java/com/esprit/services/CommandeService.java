package com.esprit.services;

import com.esprit.models.Commande;
import com.esprit.models.Equipement;
import com.esprit.utils.DataSource;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CommandeService {

    private Connection connection = DataSource.getInstance().getConnection();

    // Ajouter une commande
    public void ajouter(Commande commande) {
        String req = "INSERT INTO commande (clientId, equipementId, etat, statutPaiement, quantite, dateCreation) VALUES (?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setInt(1, commande.getClientId());
            pst.setInt(2, commande.getEquipementId());
            pst.setString(3, commande.getEtat());

            pst.setString(4, commande.getStatutPaiement());
            pst.setInt(5, commande.getQuantite());
            pst.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now())); // Date de création automatique

            pst.executeUpdate();
            System.out.println("Commande ajoutée avec succès !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout de la commande : " + e.getMessage());
        }
    }

    // Modifier une commande
    public void modifier(Commande commande) {
        String req = "UPDATE commande SET clientId=?, equipementId=?, etat=?, statutPaiement=?, quantite=? WHERE id=?";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setInt(1, commande.getClientId());
            pst.setInt(2, commande.getEquipementId());
            pst.setString(3, commande.getEtat());

            pst.setString(4, commande.getStatutPaiement());
            pst.setInt(5, commande.getQuantite());
            pst.setInt(6, commande.getId());

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


    public List<Commande> rechercherParClientId(int clientId) {
        List<Commande> commandes = new ArrayList<>();
        String req = "SELECT * FROM commande WHERE clientId = ? AND etat = 'En attente'";

        try (PreparedStatement pst = connection.prepareStatement(req)) {
            pst.setInt(1, clientId);
            ResultSet rs = pst.executeQuery();

            // Debug : Afficher la requête SQL exécutée
            System.out.println("Requête SQL exécutée : " + pst.toString());

            while (rs.next()) {
                // Récupérer l'ID de l'équipement
                int equipementId = rs.getInt("equipementId");

                // Récupérer les détails de l'équipement en utilisant EquipementService
                EquipementService equipementService = new EquipementService();
                Equipement equipement = equipementService.getEquipementById(equipementId);

                // Debug : Afficher les détails de l'équipement
                if (equipement != null) {
                    System.out.println("Équipement trouvé : " + equipement.getNom());
                } else {
                    System.out.println("Aucun équipement trouvé pour l'ID : " + equipementId);
                }

                // Créer un objet Commande avec l'équipement associé
                Commande commande = new Commande(
                        rs.getInt("id"), // ID de la commande
                        rs.getInt("clientId"), // ID du client
                        rs.getInt("equipementId"), // ID de l'équipement
                        rs.getString("etat"), // État de la commande
                        rs.getString("statutPaiement"), // Statut du paiement
                        rs.getInt("quantite"), // Quantité
                        rs.getTimestamp("dateCreation").toLocalDateTime() // Date de création
                );

                // Associer l'équipement à la commande
                commande.setEquipement(equipement);

                // Ajouter la commande à la liste
                commandes.add(commande);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des commandes du client : " + e.getMessage());
        }

        // Debug : Afficher le nombre de commandes récupérées
        System.out.println("Nombre de commandes récupérées : " + commandes.size());

        return commandes;
    }
    public void supprimerToutesLesCommandes(int clientId) {
        String req = "DELETE FROM commande WHERE clientId = ?";
        try (PreparedStatement pst = connection.prepareStatement(req)) {
            pst.setInt(1, clientId);
            pst.executeUpdate();
            System.out.println("Toutes les commandes du client " + clientId + " ont été supprimées.");
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression des commandes : " + e.getMessage());
        }
    }

    public void modifier_quantite(Commande commande) {
        String query = "UPDATE commande SET quantite = ? WHERE id = ?";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setInt(1, commande.getQuantite());
            pst.setInt(2, commande.getId());
            pst.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour de la commande : " + e.getMessage());
        }

    }

    public int getTotalQuantitePanier(int clientId) {
        int totalQuantite = 0;
        String query = "SELECT SUM(quantite) FROM commande WHERE clientId = ? AND etat = 'En attente'";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, clientId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                totalQuantite = rs.getInt(1);
                System.out.println("Quantité totale récupérée : " + totalQuantite);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return totalQuantite;
    }
    public void passerCommande(int clientId) {
        String query = "UPDATE commande SET etat = ?, dateCreation = ? WHERE clientId = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, "En commande");
            preparedStatement.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            preparedStatement.setInt(3, clientId);

            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Commande mise à jour avec succès.");
            } else {
                System.out.println("Aucune commande trouvée pour ce client.");
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour de la commande : " + e.getMessage());
        }
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
    public List<Commande> getAllCommandes(){
        List<Commande> commandes = new ArrayList<>();
        String req = "SELECT * FROM commande WHERE statutPaiement = ? ";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setString(1,"payé");
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                Commande commande = new Commande(
                        rs.getInt("id"),
                        rs.getInt("clientId"),
                        rs.getInt("equipementId"),
                        rs.getString("etat"),
                        rs.getString("statutPaiement"),
                        rs.getInt("quantite"),
                        rs.getTimestamp("dateCreation").toLocalDateTime()
                );
                Equipement equipement = getEquipementById(commande.getEquipementId());
                commande.setEquipement(equipement);

                commandes.add(commande);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des commandes : " + e.getMessage());
        }
        return commandes;
    }

































    public List<Commande> getCommandesByClientIdAndEtat(int clientId, String etat) {
        List<Commande> commandes = new ArrayList<>();
        String req = "SELECT * FROM commande WHERE clientId = ? AND etat = ?";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setInt(1, clientId);
            pst.setString(2, etat);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                Commande commande = new Commande(
                        rs.getInt("id"),
                        rs.getInt("clientId"),
                        rs.getInt("equipementId"),
                        rs.getString("etat"),
                        rs.getString("statutPaiement"),
                        rs.getInt("quantite"),
                        rs.getTimestamp("dateCreation").toLocalDateTime()
                );

                // Récupérer l'équipement associé à la commande
                Equipement equipement = getEquipementById(commande.getEquipementId());
                commande.setEquipement(equipement);

                commandes.add(commande);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des commandes : " + e.getMessage());
        }
        return commandes;
    }
    // Récupérer l'email du client par son ID
    public String getClientEmailById(int clientId) {
        String req = "SELECT email FROM client WHERE id = ?";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setInt(1, clientId);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                return rs.getString("email");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération de l'email du client : " + e.getMessage());
        }
        return null;
    }

    public String getClientphoneById(int clientId) {
        String req = "SELECT telephone FROM client WHERE id = ?";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setInt(1, clientId);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                String phoneNumber = rs.getString("telephone");
                // Formater le numéro au format international si nécessaire
                if (phoneNumber != null && !phoneNumber.startsWith("+")) {
                    phoneNumber = "+216" + phoneNumber; // Ajoute le code pays pour la Tunisie
                }
                return phoneNumber;
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération du téléphone du client : " + e.getMessage());
        }
        return null; // Retourne null si aucun résultat n'est trouvé
    }

















    // Récupérer l'équipement par son ID
    public Equipement getEquipementById(int equipementId) {
        String req = "SELECT * FROM equipement WHERE id = ?";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setInt(1, equipementId);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                return new Equipement(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("description"),
                        rs.getDouble("prix"),
                        rs.getInt("quantite_stock"),
                        rs.getInt("categorie_id"),
                        rs.getString("image")
                );
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération de l'équipement : " + e.getMessage());
        }
        return null;
    }


}