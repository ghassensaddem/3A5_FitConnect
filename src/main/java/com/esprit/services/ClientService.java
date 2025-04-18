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
        String req = "INSERT INTO client (nom,prenom,sexe,mdp,dateNaissance,email,poids,taille,image,idEvent,id_prog) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
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
            pst.setString(9, client.getImage());
            pst.setInt(10, client.getIdEvent());
            pst.setInt(11,client.getId_prog());
            pst.executeUpdate();
            System.out.println("Client ajoutée");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void modifier(Client client) {
        String req = "UPDATE client SET nom=? ,prenom=?,sexe=?,mdp=?,DateNaissance=?,email=?,poids=?,taille=?,image=?,idEvent=?,id_prog=? WHERE id=?";
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
            pst.setString(9, client.getImage());
            pst.setInt(10, client.getIdEvent());
            pst.setInt(11, client.getId_prog());
            pst.setInt(12, client.getId());
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
                clients.add(new Client(rs.getInt("id"), rs.getString("nom"), rs.getString("prenom"), rs.getString("sexe"),rs.getString("mdp"),rs.getString("dateNaissance"),rs.getString("email"),rs.getString("image"),rs.getFloat("poids"),rs.getFloat("taille"),rs.getInt("idEvent"),rs.getInt("id_prog")));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return clients;
    }

    public boolean existe(String email) {
        String req = "SELECT * FROM client WHERE email = ? ";
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

    public boolean AUTH(String email,String mdp) {
        String req = "SELECT * FROM client WHERE email = ? AND mdp=?";
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

    public void ADD(Client client) {
        String req = "INSERT INTO client (nom,prenom,sexe,mdp,dateNaissance,email,poids,taille,image) VALUES (?,?,?,?,?,?,?,?,?)";
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
            pst.setString(9, client.getImage());
            pst.executeUpdate();
            System.out.println("Client ajoutée");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void update(Client client) {
        String req = "UPDATE client SET nom=? ,prenom=?,sexe=?,mdp=?,DateNaissance=?,email=?,poids=?,taille=?,image=? WHERE id=?";
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
            pst.setString(9, client.getImage());
            pst.setInt(10, client.getId());
            pst.executeUpdate();
            System.out.println("Client modifiée");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    public ArrayList<Client> rechercherParNom(String nom) {
        ArrayList<Client> clients = new ArrayList<>();
        String req = "SELECT * FROM client WHERE nom LIKE ?";

        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setString(1, "%" + nom + "%"); // Recherche partielle avec LIKE
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                clients.add(new Client(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("sexe"),
                        rs.getString("mdp"),
                        rs.getString("dateNaissance"),
                        rs.getString("email"),
                        rs.getString("image"),
                        rs.getFloat("poids"),
                        rs.getFloat("taille"),
                        rs.getInt("idEvent"),
                        rs.getInt("id_prog")
                ));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return clients;
    }

    public Client GetClient(String email) {
        String req = "SELECT * FROM client WHERE email = ?";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setString(1, email);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return new Client(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("sexe"),
                        rs.getString("mdp"),
                        rs.getString("dateNaissance"),
                        rs.getString("email"),
                        rs.getString("image"),
                        rs.getFloat("poids"),
                        rs.getFloat("taille"),
                        rs.getInt("idEvent"),
                        rs.getInt("id_prog")
                );
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public Client rechercherParId(int id) {
        String req = "SELECT * FROM client WHERE id = ?";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return new Client(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("sexe"),
                        rs.getString("mdp"),
                        rs.getString("dateNaissance"),
                        rs.getString("email"),
                        rs.getString("image"),
                        rs.getFloat("poids"),
                        rs.getFloat("taille"),
                        rs.getInt("idEvent"),
                        rs.getInt("id_prog")
                );
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public void mettreAJourProfil(int id, String nom, String prenom, String email, String mdp,String date ,String image) {
        String req = "UPDATE client SET nom = ?, prenom = ?, email = ?, mdp = ?,dateNaissance=?, image = ? WHERE id = ?";

        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setString(1, nom);
            pst.setString(2, prenom);
            pst.setString(3, email);
            pst.setString(4, mdp);
            pst.setString(5, date);
            pst.setString(6, image);
            pst.setInt(7, id);

            pst.executeUpdate();
            System.out.println("Profil mis à jour avec succès !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de la mise à jour du profil : " + e.getMessage());
        }
    }


    public void supprimerID(int id) {
        String req = "DELETE from client WHERE id=?";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setInt(1, id);
            pst.executeUpdate();
            System.out.println("client supprimée");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    public int getTotalCommandes() {
        String req = "SELECT COUNT(*) FROM commande";
        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(req);
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println("Erreur récupération total commandes : " + e.getMessage());
        }
        return 0;
    }

    public int getCommandesByClient(int clientId) {
        String req = "SELECT COUNT(*) FROM commande WHERE clientId = ?";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setInt(1, clientId);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println("Erreur récupération commandes client : " + e.getMessage());
        }
        return 0;
    }

    public int getTotalApplications() {
        String req = "SELECT COUNT(*) FROM application";
        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(req);
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println("Erreur récupération total applications : " + e.getMessage());
        }
        return 0;
    }

    public int getApplicationsByClient(int clientId) {
        String req = "SELECT COUNT(*) FROM application WHERE idClient = ?";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setInt(1, clientId);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println("Erreur récupération applications client : " + e.getMessage());
        }
        return 0;
    }


    public int getCodeVerif(int userId) {
        String req = "SELECT id_prog FROM client WHERE id = ?";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setInt(1, userId);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getInt("id_prog");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération du code de vérification : " + e.getMessage());
        }
        return -1;
    }

    public void karwi(Client client) {
        String req = "UPDATE client SET nom=? ,prenom=?,sexe=?,mdp=?,DateNaissance=?,email=?,poids=?,taille=?,image=?,id_prog=? WHERE id=?";
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
            pst.setString(9, client.getImage());
            pst.setInt(10, client.getId_prog());
            pst.setInt(11, client.getId());
            pst.executeUpdate();
            System.out.println("Client modifiée");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }





}
