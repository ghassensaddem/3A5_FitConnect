package com.esprit.services;

import com.esprit.models.Avis;
import com.esprit.utils.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AvisService implements IService<Avis> {

    private Connection connection = DataSource.getInstance().getConnection();

    @Override
    public void ajouter(Avis avis) {
        String req = "INSERT INTO avis ( commentaire, note, seanceId) VALUES (?, ?, ?)";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setString(1, avis.getCommentaire());
            pst.setInt(2, avis.getNote());
            pst.setInt(3, avis.getSeanceId());
            pst.executeUpdate();
            System.out.println("Avis ajouté");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void modifier(Avis avis) {
        String req = "UPDATE avis SET seanceId=?, commentaire=?, note=? WHERE id=?";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setString(1, avis.getCommentaire());
            pst.setInt(2, avis.getNote());
            pst.setInt(3, avis.getSeanceId());
            pst.setInt(4, avis.getId());
            pst.executeUpdate();
            System.out.println("Avis modifié");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void supprimer(Avis avis) {
        String req = "DELETE FROM avis WHERE id=?";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setInt(1, avis.getId());
            pst.executeUpdate();
            System.out.println("Avis supprimé");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<Avis> rechercher() {
        List<Avis> avisList = new ArrayList<>();

        String req = "SELECT * FROM avis";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                avisList.add(new Avis(
                        rs.getInt("id"),
                        rs.getString("commentaire"),
                        rs.getInt("note"),
                        rs.getInt("seanceId")
                ));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return avisList;
    }
}
