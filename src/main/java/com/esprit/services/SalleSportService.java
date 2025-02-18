package com.esprit.services;
import com.esprit.models.SalleSportif;
import com.esprit.utils.DataSource;
import java.time.LocalTime;

import java.sql.Connection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SalleSportService implements iService<SalleSportif>{
    private Connection connection = DataSource.getInstance().getConnection();



    // Ajouter une salle de sport
    public void ajouter(SalleSportif salle) {
        try {

            String req = "INSERT INTO sallesportif (nomSalle, addresseSalle, HoraireOuverture, HoraireFermeture, Capacity) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setString(1, salle.getNomSalle());
            pst.setString(2, salle.getAddresseSalle());
            pst.setString(3, salle.getHoraireOuverture().toString());
            pst.setString(4, salle.getHoraireFermeture().toString());
            pst.setInt(5, salle.getCapacity());
            pst.executeUpdate();
            System.out.println("Salle de sport ajoutée");
        } catch (IllegalArgumentException e) {
            System.out.println("Erreur : " + e.getMessage());
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // Modifier une salle de sport
    public void modifier(SalleSportif salle) {

        try {
            String req = "UPDATE sallesportif SET nomSalle = ?, addresseSalle = ?, HoraireOuverture = ?, HoraireFermeture = ?, Capacity = ? WHERE idSalle = ?";
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setString(1, salle.getNomSalle());
            pst.setString(2, salle.getAddresseSalle());
            pst.setString(3, salle.getHoraireOuverture().toString());
            pst.setString(4, salle.getHoraireFermeture().toString());
            pst.setInt(5, salle.getCapacity());
            pst.setInt(6, salle.getIdSalle());
            pst.executeUpdate();
            System.out.println("Salle de sport modifiée");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // Supprimer une salle de sport
    public void supprimer(SalleSportif salle) {
        String req = "DELETE FROM sallesportif WHERE idSalle = ?";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setInt(1, salle.getIdSalle());
            pst.executeUpdate();
            System.out.println("Salle de sport supprimée");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    public ArrayList<SalleSportif> rechercher() {
        ArrayList<SalleSportif> salles = new ArrayList<>();
        String req = "SELECT * FROM sallesportif";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                salles.add(new SalleSportif(
                        rs.getInt("idSalle"),
                        rs.getString("nomSalle"),
                        rs.getString("addresseSalle"),
                        LocalTime.parse(rs.getString("HoraireOuverture")),
                        LocalTime.parse(rs.getString("HoraireFermeture")),
                        rs.getInt("capacity")
                ));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return salles;
    }
}
