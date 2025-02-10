package com.esprit.services;

import com.esprit.models.Programme;
import com.esprit.utils.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProgrammeService implements iService<Programme> {

    private Connection connection = DataSource.getInstance().getConnection();

    @Override
    public void ajouter(Programme programme) {
        String req = "INSERT INTO programme (datedebut,datefin,prix,description,coach_id) VALUES (?,?,?,?,?)";
        try {
            PreparedStatement pst = connection.prepareStatement(req);

            pst.setDate(1, programme.getdatedebut());
            pst.setDate(2, programme.getdatefin());
            pst.setDouble(3, programme.getprix());
            pst.setString( 4, programme.getdescription());
            pst.setInt(5,programme.getCoach_id());
            pst.executeUpdate();
            System.out.println("Programme ajoutée");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void modifier(Programme programme) {
        String req = "UPDATE programme SET datedebut=? ,datefin=? ,prix=? ,description=? ,coach_id=?  WHERE id=?";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setDate(1, programme.getdatedebut());
            pst.setDate(2, programme.getdatefin());
            pst.setDouble(3, programme.getprix());
            pst.setString(4, programme.getdescription());
            pst.setInt(5, programme.getCoach_id());
            pst.setInt(6, programme.getId());
            pst.executeUpdate();
            System.out.println("Programme modifiée");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void supprimer(Programme programme) {
        String req = "DELETE from programme WHERE id=?";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setInt(1, programme.getId());
            pst.executeUpdate();
            System.out.println("Programme supprimée");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public ArrayList<Programme> rechercher() {
        ArrayList<Programme> programmes = new ArrayList<>();

        String req = "SELECT * FROM programme";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            ResultSet rs = pst.executeQuery(req);
            while (rs.next()) {
                programmes.add(new Programme(rs.getInt("id"), rs.getDate("datedebut").toLocalDate(), rs.getDate("datefin").toLocalDate(), rs.getDouble("prix"), rs.getString("description"), rs.getInt("coach_id")));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return programmes;
    }
}
