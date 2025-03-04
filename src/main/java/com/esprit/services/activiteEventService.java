package com.esprit.services;

import com.esprit.models.Event;
import com.esprit.models.activiteevent;
import com.esprit.utils.DataSource;
import com.esprit.services.typeactiviteService;
import  com.esprit.models.typeactivite;
import java.sql.*;
import java.util.ArrayList;

public class activiteEventService implements iService<activiteevent> {

    private Connection connection = DataSource.getInstance().getConnection();

    @Override
    public void ajouter(activiteevent activiteEvent) {
        String req = "INSERT INTO activiteevent (horaire, nbrparticipant, idEvent,idTypeActivite) VALUES (?,?, ?, ?)";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setString(1, activiteEvent.getHoraire());
            pst.setInt(2, activiteEvent.getNbrparticipant());
            pst.setInt(3, activiteEvent.getIdEvent());
            pst.setInt(4, activiteEvent.getIdTypeActivite());
            pst.executeUpdate();
            System.out.println("Activité ajoutée");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void modifier(activiteevent activiteEvent) {
        String req = "UPDATE activiteevent SET horaire=?, nbrparticipant=?, idEvent=?,idTypeActivite=? WHERE id=?";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setString(1, activiteEvent.getHoraire());
            pst.setInt(2, activiteEvent.getNbrparticipant());
            pst.setInt(3, activiteEvent.getIdEvent());
            pst.setInt(4, activiteEvent.getIdTypeActivite());
            pst.setInt(5, activiteEvent.getId());
            pst.executeUpdate();
            System.out.println("Activité modifiée");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void supprimer(activiteevent activiteEvent) {
        String req = "DELETE FROM activiteevent WHERE id=?";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setInt(1, activiteEvent.getId());
            pst.executeUpdate();
            System.out.println("Activité supprimée");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public ArrayList<activiteevent> rechercher() {
        ArrayList<activiteevent> activites = new ArrayList<>();
        String req = "SELECT * FROM activiteevent";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                activites.add(new activiteevent(rs.getInt("id"), rs.getString("horaire"), rs.getInt("nbrparticipant"), rs.getInt("idEvent"),rs.getInt("idTypeActivite")));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return activites;
    }


    // Récupérer les IDs depuis la base de données
    public ArrayList<Integer> getAvailableIds(String table, String column) {
        ArrayList<Integer> ids = new ArrayList<>();

        // Construire dynamiquement la requête SQL
        String query = "SELECT " + column + " FROM " + table;

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                ids.add(rs.getInt(column)); // Récupérer les IDs
            }

        } catch (SQLException e) {
            System.out.println("Erreur SQL : " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("Autre erreur : " + e.getMessage());
            e.printStackTrace();
        }

        return ids;
    }
    public ArrayList<activiteevent> getActivitiesByEventId(int eventId) {
        ArrayList<activiteevent> activities = new ArrayList<>();

        String query = "SELECT * FROM activiteevent WHERE idEvent = ?"; // Assure-toi que 'event_id' est bien la clé étrangère dans ta table

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, eventId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                activiteevent activity = new activiteevent();
                activity.setHoraire(rs.getString("horaire"));
                activity.setNbrparticipant(rs.getInt("nbrparticipant"));
                //activity.setEventId(rs.getInt("event_id")); // Si tu as un champ clé étrangère dans ton modèle activiteevent

                activities.add(activity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return activities;
    }
    public String getImagePathFromDatabase(int eventId) {
        String imagePath = null;
        String query = "SELECT image FROM event WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, eventId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                imagePath = rs.getString("image");
                System.out.println("Image Path from DB: " + imagePath); // Debug: afficher le chemin récupéré
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return imagePath;
    }
    public int getidtipe(int eventId) {
        int imagePath = 0;
        String query = "SELECT idTypeActivite FROM activiteevent WHERE idEvent = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, eventId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                imagePath = rs.getInt("idTypeActivite");
                System.out.println("Image Path from DB: " + imagePath); // Debug: afficher le chemin récupéré
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return imagePath;
    }
    public String[] getTypeActiviteDetails(int typeId) {
        String query = "SELECT title, description FROM typeactivite WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, typeId);
            ResultSet rs = stmt.executeQuery();

            stmt.setInt(1, typeId);
            ResultSet resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                String titre = resultSet.getString("title");
                String description = resultSet.getString("description");
                return new String[]{titre, description};
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new String[]{"Titre inconnu", "Description indisponible"};
    }

    public ArrayList<typeactivite> rechercherParNom(String chaine) {

        ArrayList<typeactivite> events = new ArrayList<>();

        String req = "SELECT * FROM tipeactivite WHERE " +
                "title LIKE ?";

        try {
            PreparedStatement pst = connection.prepareStatement(req);

            // Appliquer le filtre sur les champs pertinents
            for (int i = 1; i <= 3; i++) {
                pst.setString(i, "%" + chaine + "%");
            }

            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                events.add(new typeactivite(
                        rs.getString("title"),
                        rs.getString("description")
                        ));
            }
        } catch (SQLException e) {
            System.out.println("Erreur SQL : " + e.getMessage());
        }

        return events;
    }
   /* public ArrayList<activiteevent> rechercherParidd(int chaine) {

        ArrayList<activiteevent> events = new ArrayList<>();

        String req = "SELECT * FROM activiteevent WHERE " +
                "id LIKE ?";

        try {
            PreparedStatement pst = connection.prepareStatement(req);

            // Appliquer le filtre sur les champs pertinents
            for (int i = 1; i <= 3; i++) {
                pst.setString(i, "%" + chaine + "%");
            }

            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                events.add(new typeactivite(
                        rs.getString("title"),
                        rs.getString("description")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Erreur SQL : " + e.getMessage());
        }

        return events;
    }*/
   public int getTotalParticipantsForEvent(int eventId) {
       int totalParticipants = 0;
       String query = "SELECT SUM(nbrparticipant) AS total FROM activiteevent WHERE idEvent = ?";

       try (PreparedStatement stmt = connection.prepareStatement(query)) {
           stmt.setInt(1, eventId);
           ResultSet rs = stmt.executeQuery();

           if (rs.next()) {
               totalParticipants = rs.getInt("total"); // Récupérer la somme
           }
       } catch (SQLException e) {
           e.printStackTrace();
       }

       return totalParticipants;
   }

}


