package com.esprit.services;

import com.esprit.models.Event;
import com.esprit.utils.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EventService implements iService<Event> {

    private Connection connection = DataSource.getInstance().getConnection();

    @Override
    public void ajouter(Event event) {
        String req = "INSERT INTO event (date, prixdupass, lieu, horaire , image) VALUES (?, ?, ?, ?, ?)";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setString(1, event.getDate());
            pst.setDouble(2, event.getPrixdupass());
            pst.setString(3, event.getLieu());
            pst.setString(4, event.getHoraire());
            pst.setString(5, event.getImage());
            pst.executeUpdate();
            System.out.println("Événement ajouté");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void modifier(Event event) {
        String req = "UPDATE event SET date=?, prixdupass=?, lieu=?, horaire=?, image=? WHERE id=?";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setString(1, event.getDate());
            pst.setDouble(2, event.getPrixdupass());
            pst.setString(3, event.getLieu());
            pst.setString(4, event.getHoraire());
            pst.setString(5, event.getImage());
            pst.setInt(6, event.getId());
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
                events.add(new Event(rs.getInt("id"), rs.getString("date"), rs.getFloat("prixdupass"), rs.getString("lieu"), rs.getString("horaire"), rs.getString("image")));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return events;
    }
}

