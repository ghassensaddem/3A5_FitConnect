package com.esprit.services;

import com.esprit.models.Admin;
import com.esprit.utils.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class AdminService implements IService<Admin> {

    private Connection connection = DataSource.getInstance().getConnection();

    @Override
    public void ajouter(Admin admin) {
        String req = "INSERT INTO admin (nom,prenom,sexe,mdp,dateNaissance,email) VALUES (?,?,?,?,?,?)";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setString(1, admin.getNom());
            pst.setString(2, admin.getPrenom());
            pst.setString(3, admin.getSexe());
            pst.setString(4, admin.getMdp());
            pst.setString(5, admin.getDateNaissance());
            pst.setString(6, admin.getEmail());
            pst.executeUpdate();
            System.out.println("Admin ajoutée");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void modifier(Admin admin) {
        String req = "UPDATE admin SET nom=? ,prenom=?,sexe=?,mdp=?,DateNaissance=?,email=? WHERE id=?";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setString(1, admin.getNom());
            pst.setString(2, admin.getPrenom());
            pst.setString(3, admin.getSexe());
            pst.setString(4, admin.getMdp());
            pst.setString(5, admin.getDateNaissance());
            pst.setString(6, admin.getEmail());
            pst.setInt(7, admin.getId());
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
    public List<Admin> rechercher() {
        List<Admin> clients = new ArrayList<>();

        String req = "SELECT * FROM admin";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            ResultSet rs = pst.executeQuery(req);
            while (rs.next()) {
                clients.add(new Admin(rs.getInt("id"), rs.getString("nom"), rs.getString("prenom"), rs.getString("sexe"),rs.getString("mdp"),rs.getString("dateNaissance"),rs.getString("email")));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return clients;
    }
}
