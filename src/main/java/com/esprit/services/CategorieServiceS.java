package com.esprit.services;

import com.esprit.models.CategorieActivity;
import com.esprit.utils.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategorieServiceS {
    private Connection connection = DataSource.getInstance().getConnection();

    public void ajouter(CategorieActivity categorie) {
        String req = "INSERT INTO categorieactivity (nomCategorie, description) VALUES (?, ?)";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setString(1, categorie.getNomCategorie());
            pst.setString(2, categorie.getDescription());
            pst.executeUpdate();
            System.out.println("Catégorie ajoutée");
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout de la catégorie : " + e.getMessage());
        }
    }

    public void modifier(CategorieActivity categorie) {
        String req = "UPDATE categorieactivity SET nomCategorie = ?, description = ? WHERE idCategorie = ?";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setString(1, categorie.getNomCategorie());
            pst.setString(2, categorie.getDescription());
            pst.setInt(3, categorie.getIdCategorie());
            pst.executeUpdate();
            System.out.println("Catégorie modifiée");
        } catch (SQLException e) {
            System.out.println("Erreur lors de la modification de la catégorie : " + e.getMessage());
        }
    }

    public void supprimer(CategorieActivity categorie) {
        String req = "DELETE FROM categorieactivity WHERE idCategorie = ?";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setInt(1, categorie.getIdCategorie());
            pst.executeUpdate();
            System.out.println("Catégorie supprimée");
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression de la catégorie : " + e.getMessage());
        }
    }

    public List<CategorieActivity> rechercher() {
        List<CategorieActivity> categories = new ArrayList<>();
        String req = "SELECT * FROM categorieactivity";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                CategorieActivity categorie = new CategorieActivity(
                        rs.getInt("idCategorie"),
                        rs.getString("nomCategorie")
                );
                categorie.setDescription(rs.getString("description"));
                categories.add(categorie);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des catégories : " + e.getMessage());
        }
        return categories;
    }
    public CategorieActivity getCategorieByName(String nomCategorie) {
        String req = "SELECT * FROM categorieactivity WHERE nomCategorie = ?";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setString(1, nomCategorie);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return new CategorieActivity(rs.getInt("idCategorie"), rs.getString("nomCategorie"));
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération de la catégorie : " + e.getMessage());
        }
        return null;
    }

    public CategorieActivity getCategorieById(int idCategorie) {
        String req = "SELECT * FROM categorieactivity WHERE idCategorie = ?";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setInt(1, idCategorie);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                CategorieActivity categorie = new CategorieActivity(
                        rs.getInt("idCategorie"),
                        rs.getString("nomCategorie")
                );
                categorie.setDescription(rs.getString("description"));
                return categorie;
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération de la catégorie : " + e.getMessage());
        }
        return null; // Retourne null si la catégorie n'est pas trouvée
    }


}
