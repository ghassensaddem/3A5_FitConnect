package com.esprit.services;

import com.esprit.models.Event;
import com.esprit.utils.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class EventService implements IService<Event> {

    private Connection connection = DataSource.getInstance().getConnection();

    @Override
    public void ajouter(Event event) {
        String req = "INSERT INTO event (date, prixdupass, lieu, horaire) VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setString(1, event.getDate());
            pst.setDouble(2, event.getPrixdupass());
            pst.setString(3, event.getLieu());
            pst.setString(4, event.getHoraire());
            pst.executeUpdate();
            System.out.println("Événement ajouté");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void modifier(Event event) {
        String req = "UPDATE event SET date=?, prixdupass=?, lieu=?, horaire=? WHERE id=?";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setString(1, event.getDate());
            pst.setDouble(2, event.getPrixdupass());
            pst.setString(3, event.getLieu());
            pst.setString(4, event.getHoraire());
            pst.setInt(5, event.getId());
            pst.executeUpdate();
            System.out.println("Événement modifié");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void supprimer(Event event) {
        String req = "DELETE FROM event WHERE id=?";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setInt(1, event.getId());
            pst.executeUpdate();
            System.out.println("Événement supprimé");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public ArrayList<Event> rechercher() {
        ArrayList<Event> events = new ArrayList<>();
        String req = "SELECT * FROM event";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                events.add(new Event(rs.getInt("id"), rs.getString("date"), rs.getFloat("prixdupass"), rs.getString("lieu"), rs.getString("horaire")));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return events;
    }
}

