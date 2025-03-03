package com.esprit.services;

import com.esprit.models.Equipement;
import com.esprit.models.CategorieEquipement;
import com.esprit.utils.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EquipementService {

    private Connection connection = DataSource.getInstance().getConnection();

    // Ajouter un équipement
    public void ajouter(Equipement equipement) {
        String req = "INSERT INTO equipement (nom, description, prix, quantite_stock, categorie_id,image) VALUES (?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setString(1, equipement.getNom());
            pst.setString(2, equipement.getDescription());
            pst.setDouble(3, equipement.getPrix());
            pst.setInt(4, equipement.getQuantiteStock());
            pst.setInt(5, equipement.getIdCategorie());
            pst.setString(6, equipement.getImage());
            pst.executeUpdate();
            System.out.println("Équipement ajouté avec succès !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout de l'équipement : " + e.getMessage());
        }
    }

    // Modifier un équipement
    public void modifier(Equipement equipement) {
        String req = "UPDATE equipement SET nom=?, description=?, prix=?, quantite_stock=?, categorie_id=?, image=? WHERE id=?";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setString(1, equipement.getNom());
            pst.setString(2, equipement.getDescription());
            pst.setDouble(3, equipement.getPrix());
            pst.setInt(4, equipement.getQuantiteStock());
            pst.setInt(5, equipement.getIdCategorie());
            pst.setString(6, equipement.getImage());
            pst.setInt(7, equipement.getId());
            pst.executeUpdate();
            System.out.println("Équipement modifié avec succès !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de la modification de l'équipement : " + e.getMessage());
        }
    }

    // Supprimer un équipement
    public void supprimer(Equipement e1) {
        String req = "DELETE FROM equipement WHERE id=?";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setInt(1, e1.getId());
            pst.executeUpdate();
            System.out.println("Équipement supprimé avec succès !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression de l'équipement : " + e.getMessage());
        }
    }

    public List<Equipement> rechercher() {
        List<Equipement> equipements = new ArrayList<>();
        String req = "SELECT * FROM equipement";

        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(req);

            while (rs.next()) {
                Equipement equipement = new Equipement(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("description"),
                        rs.getDouble("prix"),
                        rs.getInt("quantite_stock"),
                        rs.getInt("categorie_id"),
                        rs.getString("image")
                );
                equipements.add(equipement);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des équipements : " + e.getMessage());
        }

        return equipements;
    }
    public List<Equipement> rechercherParId(int id) {
        List<Equipement> equipements = new ArrayList<>();
        String req = "SELECT * FROM equipement WHERE categorie_id = ?";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                Equipement equipement = new Equipement(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("description"),
                        rs.getDouble("prix"),
                        rs.getInt("quantite_stock"),
                        rs.getInt("categorie_id"),
                        rs.getString("image")
                );
                equipements.add(equipement);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des équipements : " + e.getMessage());
        }

        return equipements;
    }
    public Equipement getEquipementById(int id) {
        String req = "SELECT * FROM equipement WHERE id = ?";
        try (PreparedStatement pst = connection.prepareStatement(req)) {
            pst.setInt(1, id);
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
            System.err.println("Erreur lors de la récupération de l'équipement : " + e.getMessage());
        }
        return null; // Retourne null si aucun équipement n'est trouvé
    }
    // Méthode pour rechercher par nom
    public List<Equipement> searchByName(String searchText) {
        List<Equipement> equipements = rechercher(); // Récupérer tous les équipements
        return equipements.stream()
                .filter(equipement -> equipement.getNom().toLowerCase().contains(searchText.toLowerCase()))
                .collect(Collectors.toList());
    }

    // Méthode pour trier par quantité
    public List<Equipement> sortByQuantity() {
        List<Equipement> equipements = rechercher(); // Récupérer tous les équipements
        equipements.sort((e1, e2) -> Integer.compare(e2.getQuantiteStock(), e1.getQuantiteStock())); // Tri décroissant
        return equipements;
    }
    public void mettreAJourQuantiteEquipement(int equipementId, int quantiteCommandee) {
        // Récupérer l'équipement actuel
        Equipement equipement = getEquipementById(equipementId);
        if (equipement == null) {
            System.err.println("Équipement non trouvé avec l'ID : " + equipementId);
            return;
        }

        // Calculer la nouvelle quantité
        int nouvelleQuantite = equipement.getQuantiteStock() - quantiteCommandee;

        if (nouvelleQuantite <= 0) {
            // Si la quantité est nulle ou négative, supprimer l'équipement
            supprimer(equipement);
            System.out.println("L'équipement " + equipement.getNom() + " a été supprimé car la quantité est épuisée.");
        } else {
            // Sinon, mettre à jour la quantité
            String req = "UPDATE equipement SET quantite_stock = ? WHERE id = ?";
            try (PreparedStatement pst = connection.prepareStatement(req)) {
                pst.setInt(1, nouvelleQuantite);
                pst.setInt(2, equipementId);
                pst.executeUpdate();
                System.out.println("Quantité de l'équipement mise à jour avec succès.");
            } catch (SQLException e) {
                System.err.println("Erreur lors de la mise à jour de la quantité de l'équipement : " + e.getMessage());
            }
        }
    }




}
