package com.esprit.services;

import com.esprit.models.Admin;
import com.esprit.models.Client;
import com.esprit.utils.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class AdminService implements iService<Admin> {

    private Connection connection = DataSource.getInstance().getConnection();

    @Override
    public void ajouter(Admin admin) {
        String req = "INSERT INTO admin (nom,prenom,sexe,mdp,dateNaissance,email,image,role) VALUES (?,?,?,?,?,?,?,?)";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setString(1, admin.getNom());
            pst.setString(2, admin.getPrenom());
            pst.setString(3, admin.getSexe());
            pst.setString(4, admin.getMdp());
            pst.setString(5, admin.getDateNaissance());
            pst.setString(6, admin.getEmail());
            pst.setString(7, admin.getImage());
            pst.setString(8, admin.getRole());
            pst.executeUpdate();
            System.out.println("Admin ajoutée");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void modifier(Admin admin) {
        String req = "UPDATE admin SET nom=? ,prenom=?,sexe=?,mdp=?,DateNaissance=?,email=?,image=?,role=? WHERE id=?";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setString(1, admin.getNom());
            pst.setString(2, admin.getPrenom());
            pst.setString(3, admin.getSexe());
            pst.setString(4, admin.getMdp());
            pst.setString(5, admin.getDateNaissance());
            pst.setString(6, admin.getEmail());
            pst.setString(7, admin.getImage());
            pst.setString(8, admin.getRole());
            pst.setInt(9, admin.getId());
            pst.executeUpdate();
            System.out.println("Admin modifiée");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void supprimer(Admin admin) {
        String req = "DELETE from admin WHERE id=?";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setInt(1, admin.getId());
            pst.executeUpdate();
            System.out.println("admin supprimée");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public ArrayList<Admin> rechercher() {
        ArrayList<Admin> clients = new ArrayList<>();

        String req = "SELECT * FROM admin";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            ResultSet rs = pst.executeQuery(req);
            while (rs.next()) {
                clients.add(new Admin(rs.getInt("id"), rs.getString("nom"), rs.getString("prenom"), rs.getString("sexe"),rs.getString("mdp"),rs.getString("dateNaissance"),rs.getString("email"),rs.getString("image"),rs.getString("role")));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return clients;
    }


    public boolean existe(String email) {
        String req = "SELECT * FROM admin WHERE email = ? ";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setString(1, email);
            ResultSet rs = pst.executeQuery();

            // Vérifier si un résultat existe
            boolean exists = rs.next();

            // Fermer les ressources
            rs.close();
            pst.close();

            return exists;
        } catch (SQLException e) {
            System.out.println("Erreur SQL : " + e.getMessage());
            return false;
        }
    }

    public ArrayList<Admin> rechercherParNom(String nom) {
        ArrayList<Admin> admins = new ArrayList<>();
        String req = "SELECT * FROM admin WHERE nom LIKE ?";

        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setString(1, "%" + nom + "%"); // Recherche partielle avec LIKE
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                admins.add(new Admin(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("sexe"),
                        rs.getString("mdp"),
                        rs.getString("dateNaissance"),
                        rs.getString("email"),
                        rs.getString("image"),
                        rs.getString("role")
                ));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return admins;
    }


    public boolean AUTH(String email,String mdp) {
        String req = "SELECT * FROM admin WHERE email = ? AND mdp=?";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setString(1, email);
            pst.setString(2, mdp);
            ResultSet rs = pst.executeQuery();

            // Vérifier si un résultat existe
            boolean exists = rs.next();

            // Fermer les ressources
            rs.close();
            pst.close();

            return exists;
        } catch (SQLException e) {
            System.out.println("Erreur SQL : " + e.getMessage());
            return false;
        }
    }

    public Admin GetAdmin(String email) {
        String req = "SELECT * FROM admin WHERE email = ?";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setString(1, email);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return new Admin(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("sexe"),
                        rs.getString("mdp"),
                        rs.getString("dateNaissance"),
                        rs.getString("email"),
                        rs.getString("image"),
                        rs.getString("role")
                );
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

}
