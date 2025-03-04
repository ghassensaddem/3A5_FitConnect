package com.esprit.services;

import com.esprit.models.Event;
import com.esprit.models.activiteevent;
import com.esprit.utils.DataSource;
import jakarta.mail.Message;
import jakarta.mail.PasswordAuthentication;

import com.mysql.cj.Session;
import jakarta.mail.MessagingException;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import jakarta.mail.Authenticator;
import jakarta.mail.PasswordAuthentication;


import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;



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
    public ArrayList<Event> rechercherParNom(String chaine) {
        ArrayList<Event> events = new ArrayList<>();

        String req = "SELECT * FROM event WHERE " +
                "date LIKE ? OR lieu LIKE ? OR horaire LIKE ?";

        try {
            PreparedStatement pst = connection.prepareStatement(req);

            // Appliquer le filtre sur les champs pertinents
            for (int i = 1; i <= 3; i++) {
                pst.setString(i, "%" + chaine + "%");
            }

            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                events.add(new Event(
                        rs.getInt("id"),
                        rs.getString("date"),
                        rs.getFloat("prixdupass"),
                        rs.getString("lieu"),
                        rs.getString("horaire"),
                        rs.getString("image")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Erreur SQL : " + e.getMessage());
        }

        return events;
    }
    public int calculnb(String mois) {
        int count = 0;  // Initialisation de la variable count

        try {
            Statement stmt = this.connection.createStatement();
            // Utilisation de la fonction EXTRACT pour obtenir le mois de la colonne date
            String query = "SELECT COUNT(*) FROM event WHERE EXTRACT(MONTH FROM date) = " + mois;
            ResultSet rs = stmt.executeQuery(query);

            if (rs.next()) {
                count = rs.getInt(1);  // Récupérer le nombre d'événements pour ce mois
            }
            return count;
        } catch (SQLException ex) {
            System.out.println(ex);
            return 0;
        }
    }

    public ArrayList<Event> affichageAvecTri(String critere) {
        ArrayList<Event> events = new ArrayList<>();

        // Liste des critères autorisés pour éviter l'injection SQL
        List<String> criteresAutorises = Arrays.asList("date", "prixdupass", "lieu", "horaire");

        // Vérification si le critère est valide, sinon utiliser un critère par défaut
        if (!criteresAutorises.contains(critere)) {
            System.out.println("Critère de tri invalide, utilisation du tri par défaut (date).");
            critere = "date"; // Tri par défaut
        }

        // Construction sécurisée de la requête SQL
        String req = "SELECT * FROM event ORDER BY " + critere;

        try {
            PreparedStatement pst = connection.prepareStatement(req);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                events.add(new Event(
                        rs.getInt("id"),
                        rs.getString("date"),
                        rs.getFloat("prixdupass"),
                        rs.getString("lieu"),
                        rs.getString("horaire"),
                        rs.getString("image")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Erreur SQL : " + e.getMessage());
        }

        return events;
    }

    public void envoyerEmail(String to, String subject, String body) {
        final String username = "ghassensaddem40@gmail.com";
        final String password = "ancw ufbh jjtc qtub";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        // Utilisation de jakarta.mail.Session pour l'authentification
        // Utilisation de jakarta.mail.Authenticator
        jakarta.mail.Session session = jakarta.mail.Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            // Créer le message avec la bonne classe javax.mail.Message
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);
            message.setText(body);

            // Envoi de l'email
            Transport.send(message);

            System.out.println("Email envoyé avec succès");
        } catch (MessagingException e) {
            System.out.println("Erreur lors de l'envoi de l'email : " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
    public double calculerRevenuParEvent(int eventId) {
        activiteEventService activiteService = new activiteEventService(); // Instance du service
        int totalParticipants = activiteService.getTotalParticipantsForEvent(eventId); // Appel de la fonction existante

        double prixPass = 0;
        String query = "SELECT prixdupass FROM event WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, eventId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                prixPass = rs.getDouble("prixdupass");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return prixPass * totalParticipants; // Calcul des revenus
    }
    public ArrayList<String> getRevenusParEvent() {
        ArrayList<String> revenusList = new ArrayList<>();
        String query = "SELECT id FROM event";

        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int eventId = rs.getInt("id");
                double revenu = calculerRevenuParEvent(eventId);
                revenusList.add("Événement ID: " + eventId + " | Revenu: " + revenu + " TND");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return revenusList;
    }
    public ArrayList<Event> getEventsAvecRevenu() {
        ArrayList<Event> events = new ArrayList<>();
        String query = "SELECT * FROM event";

        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int eventId = rs.getInt("id");
                String date = rs.getString("date");
                double prixPass = rs.getDouble("prixdupass");
                String lieu = rs.getString("lieu");
                String horaire = rs.getString("horaire");
                String image = rs.getString("image");

                // Calcul du revenu en appelant la fonction existante
                double revenu = calculerRevenuParEvent(eventId);

                // Création de l'objet Event avec son revenu
                Event event = new Event(eventId, date, (float) prixPass, lieu, horaire, image);
                event.setRevenu(revenu); // Ajouter le revenu à l'événement

                events.add(event);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return events;  // Retourner la liste des événements avec revenus
    }

    public activiteevent getActiviteEvent(int idEvent) throws SQLException {
        String query = "SELECT * FROM activiteevent WHERE idEvent = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, idEvent);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // Créer et retourner l'objet activiteevent à partir des résultats de la requête
                return new activiteevent(
                        rs.getInt("id"),
                        rs.getString("horaire"),
                        rs.getInt("nbrparticipant"),
                        rs.getInt("idEvent"),
                        rs.getInt("idTypeActivite")
                );
            } else {
                return null; // Aucun résultat trouvé
            }
        }
    }
}

