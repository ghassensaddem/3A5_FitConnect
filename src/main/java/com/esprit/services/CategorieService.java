package com.esprit.services;

import com.esprit.models.CategorieEquipement;
import com.esprit.utils.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategorieService {


    private static Connection connection = DataSource.getInstance().getConnection();
    public List<CategorieEquipement> getAllCategories() {
        List<CategorieEquipement> categories = new ArrayList<>();
        String req = "SELECT * FROM categorie_equipement";

        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(req);

            while (rs.next()) {
                CategorieEquipement categorie = new CategorieEquipement(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("description")
                );
                categories.add(categorie);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des catégories : " + e.getMessage());
        }

        return categories;
    }

    public void ajouter(CategorieEquipement categorie) {
        String req = "INSERT INTO categorie_equipement (nom, description) VALUES (?,?)";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setString(1, categorie.getNom());
            pst.setString(2, categorie.getDescription());
            pst.executeUpdate();
            System.out.println("Catégorie ajoutée");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void modifier(CategorieEquipement categorie) {
        String req = "UPDATE categorie_equipement SET nom=?, description=? WHERE id=?";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setString(1, categorie.getNom());
            pst.setString(2, categorie.getDescription());
            pst.setInt(3, categorie.getId());
            pst.executeUpdate();
            System.out.println("Catégorie modifiée");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void supprimer(CategorieEquipement categorie) {
        String req = "DELETE FROM categorie_equipement WHERE id=?";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setInt(1, categorie.getId());
            pst.executeUpdate();
            System.out.println("Catégorie supprimée");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public List<CategorieEquipement> rechercher() {
        List<CategorieEquipement> categories = new ArrayList<>();

        String req = "SELECT * FROM categorie_equipement";
        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(req);
            while (rs.next()) {
                categories.add(new CategorieEquipement(rs.getInt("id"), rs.getString("nom"), rs.getString("description")));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return categories;
    }


    public static String getCategorieNomById(int idCategorie) {
        String categorieNom = "";
        String req = "SELECT nom FROM categorie_equipement WHERE id = ?";

        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setInt(1, idCategorie);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                categorieNom = rs.getString("nom");

            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return categorieNom;
    }


}
