package com.esprit.services;

import com.esprit.models.Coach;
import com.esprit.utils.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class CoachService implements iService<Coach> {

    private Connection connection = DataSource.getInstance().getConnection();

    @Override
    public void ajouter(Coach coach) {
        String req = "INSERT INTO coach (nom,prenom,sexe,mdp,dateNaissance,email,lieuEngagement,specialite,image) VALUES (?,?,?,?,?,?,?,?,?)";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setString(1, coach.getNom());
            pst.setString(2, coach.getPrenom());
            pst.setString(3, coach.getSexe());
            pst.setString(4, coach.getMdp());
            pst.setString(5, coach.getDateNaissance());
            pst.setString(6, coach.getEmail());
            pst.setString(7, coach.getLieuEngagement());
            pst.setString(8, coach.getSpecialite());
            pst.setString(9, coach.getImage());

            pst.executeUpdate();
            System.out.println("coach ajoutée");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void modifier(Coach coach) {
        String req = "UPDATE coach SET nom=? ,prenom=?,sexe=?,mdp=?,DateNaissance=?,email=?,lieuEngagement=?,specialite=?,image=? WHERE id=?";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setString(1, coach.getNom());
            pst.setString(2, coach.getPrenom());
            pst.setString(3, coach.getSexe());
            pst.setString(4, coach.getMdp());
            pst.setString(5, coach.getDateNaissance());
            pst.setString(6, coach.getEmail());
            pst.setString(7, coach.getLieuEngagement());
            pst.setString(8,coach.getSpecialite());
            pst.setString(9,coach.getImage());
            pst.setInt(10, coach.getId());
            pst.executeUpdate();
            System.out.println("coach modifiée");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void supprimer(Coach coach) {
        String req = "DELETE from coach WHERE id=?";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setInt(1, coach.getId());
            pst.executeUpdate();
            System.out.println("coach supprimée");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public ArrayList<Coach> rechercher() {
        ArrayList<Coach> coachs = new ArrayList<>();

        String req = "SELECT * FROM coach";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            ResultSet rs = pst.executeQuery(req);
            while (rs.next()) {
                coachs.add(new Coach(rs.getInt("id"), rs.getString("nom"), rs.getString("prenom"), rs.getString("sexe"),rs.getString("mdp"),rs.getString("dateNaissance"),rs.getString("email"),rs.getString("image"),rs.getString("lieuEngagement"),rs.getString("specialite")));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return coachs;
    }

    public ArrayList<Coach> rechercherParNom(String nom) {
        ArrayList<Coach> coachs = new ArrayList<>();
        String req = "SELECT * FROM coach WHERE nom LIKE ?";

        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setString(1, "%" + nom + "%"); // Recherche partielle avec LIKE
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                coachs.add(new Coach(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("sexe"),
                        rs.getString("mdp"),
                        rs.getString("dateNaissance"),
                        rs.getString("email"),
                        rs.getString("image"),
                        rs.getString("lieuEngagement"),
                        rs.getString("specialite")
                ));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return coachs;
    }



    public ArrayList<String> getAdressesSalles() {
        ArrayList<String> adresses = new ArrayList<>();
        String req = "SELECT addresseSalle FROM sallesportif";

        try {
            PreparedStatement pst = connection.prepareStatement(req);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                adresses.add(rs.getString("addresseSalle"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return adresses;
    }


    public ArrayList<String> getSpecialites() {
        ArrayList<String> specialites = new ArrayList<>();
        String req = "SELECT nomActivity FROM activity";

        try {
            PreparedStatement pst = connection.prepareStatement(req);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                specialites.add(rs.getString("nomActivity"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return specialites;
    }


}
