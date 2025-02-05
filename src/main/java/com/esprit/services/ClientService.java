package com.esprit.services;

import com.esprit.models.Client;
import com.esprit.utils.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClientService implements iService<Client> {

    private Connection connection = DataSource.getInstance().getConnection();

    @Override
    public void ajouter(Client client) {
        String req = "INSERT INTO client (nom,prenom,sexe,mdp,dateNaissance,email,poids,taille,objectif,idEvent) VALUES (?,?,?,?,?,?,?,?,?,?)";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setString(1, client.getNom());
            pst.setString(2, client.getPrenom());
            pst.setString(3, client.getSexe());
            pst.setString(4, client.getMdp());
            pst.setString(5, client.getDateNaissance());
            pst.setString(6, client.getEmail());
            pst.setFloat(7, client.getPoids());
            pst.setFloat(8, client.getTaille());
            pst.setString(9, client.getObjectif());
            pst.setInt(10, client.getIdEvent());
            pst.executeUpdate();
            System.out.println("Client ajoutée");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void modifier(Client client) {
        String req = "UPDATE client SET nom=? ,prenom=?,sexe=?,mdp=?,DateNaissance=?,email=?,poids=?,taille=?,objectif=?,idEvent=? WHERE id=?";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setString(1, client.getNom());
            pst.setString(2, client.getPrenom());
            pst.setString(3, client.getSexe());
            pst.setString(4, client.getMdp());
            pst.setString(5, client.getDateNaissance());
            pst.setString(6, client.getEmail());
            pst.setFloat(7, client.getPoids());
            pst.setFloat(8, client.getTaille());
            pst.setString(9, client.getObjectif());
            pst.setInt(10, client.getIdEvent());
            pst.setInt(11, client.getId());
            pst.executeUpdate();
            System.out.println("Client modifiée");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void supprimer(Client client) {
        String req = "DELETE from client WHERE id=?";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setInt(1, client.getId());
            pst.executeUpdate();
            System.out.println("client supprimée");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public ArrayList<Client> rechercher() {
        ArrayList<Client> clients = new ArrayList<>();

        String req = "SELECT * FROM client";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            ResultSet rs = pst.executeQuery(req);
            while (rs.next()) {
                clients.add(new Client(rs.getInt("id"), rs.getString("nom"), rs.getString("prenom"), rs.getString("sexe"),rs.getString("mdp"),rs.getString("dateNaissance"),rs.getString("email"),rs.getFloat("poids"),rs.getFloat("taille"),rs.getString("objectif"),rs.getInt("idEvent")));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return clients;
    }
}
