package com.esprit.controllers;

import com.calendarfx.model.Calendar;
import com.calendarfx.model.CalendarSource;
import com.calendarfx.model.Entry;
import com.calendarfx.view.CalendarView;
import com.esprit.models.NavigationHelper;
import com.esprit.models.seance;
import com.esprit.services.HistoriqueService;
import com.esprit.services.SeanceService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class AfficherSeance implements Initializable {

    @FXML
    private CalendarView calendarView;

    private SeanceService seanceService = new SeanceService();
    private Calendar seancesCalendar;
    private Map<Entry<?>, seance> entryToSeanceMap = new HashMap<>();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Configuration du calendrier
        setupCalendar();

        // Chargement des données
        loadData();

        // Gestion du clic sur un événement pour ouvrir la fenêtre de modification
        configureEntryClickHandler();
    }

    private void setupCalendar() {
        // Créer un calendrier pour les séances
        seancesCalendar = new Calendar("Séances");
        seancesCalendar.setStyle(Calendar.Style.STYLE1); // Style du calendrier (couleur)

        // Créer une source de calendrier et y ajouter notre calendrier
        CalendarSource source = new CalendarSource("Source des séances");
        source.getCalendars().add(seancesCalendar);

        // Ajouter la source au CalendarView
        calendarView.getCalendarSources().add(source);

        // Configuration de l'affichage du calendrier
        calendarView.setRequestedTime(LocalTime.now());
        calendarView.setShowAddCalendarButton(false);
        calendarView.setShowPrintButton(false);
        calendarView.setShowSourceTrayButton(false);
        calendarView.setShowSearchField(false);
        calendarView.setEntryDetailsCallback(param -> {
            Entry<?> entry = param.getEntry();
            if (entry != null) {
                seance selectedSeance = entryToSeanceMap.get(entry);
                if (selectedSeance != null) {
                    ouvrirPageModification(selectedSeance);
                }
            }
            return null; // This prevents the default entry details view from showing
        });

        // Afficher la semaine par défaut
        calendarView.showWeekPage();
    }

    private void configureEntryClickHandler() {
        // Création d'un ContextMenu pour les entrées du calendrier
        calendarView.getDayPage().setEntryContextMenuCallback(param -> {
            Entry<?> entry = param.getEntry();
            seance selectedSeance = entryToSeanceMap.get(entry);
            if (selectedSeance != null) {
                // Créer un menu contextuel
                ContextMenu contextMenu = new ContextMenu();

                // Option pour modifier
                MenuItem modifierItem = new MenuItem("Modifier");
                modifierItem.setOnAction(e -> ouvrirPageModification(selectedSeance));

                // Option pour supprimer
                MenuItem supprimerItem = new MenuItem("Supprimer");
                supprimerItem.setOnAction(e -> supprimerSeance(selectedSeance));

                // Ajouter les options au menu
                contextMenu.getItems().addAll(modifierItem, supprimerItem);

                return contextMenu;
            }
            return null;
        });

        // Même configuration pour la vue semaine
        calendarView.getWeekPage().setEntryContextMenuCallback(param -> {
            Entry<?> entry = param.getEntry();
            seance selectedSeance = entryToSeanceMap.get(entry);
            if (selectedSeance != null) {
                ContextMenu contextMenu = new ContextMenu();

                MenuItem modifierItem = new MenuItem("Modifier");
                modifierItem.setOnAction(e -> ouvrirPageModification(selectedSeance));

                MenuItem supprimerItem = new MenuItem("Supprimer");
                supprimerItem.setOnAction(e -> supprimerSeance(selectedSeance));

                contextMenu.getItems().addAll(modifierItem, supprimerItem);

                return contextMenu;
            }
            return null;
        });

        // Même configuration pour la vue mois
        calendarView.getMonthPage().setEntryContextMenuCallback(param -> {
            Entry<?> entry = param.getEntry();
            seance selectedSeance = entryToSeanceMap.get(entry);
            if (selectedSeance != null) {
                ContextMenu contextMenu = new ContextMenu();

                MenuItem modifierItem = new MenuItem("Modifier");
                modifierItem.setOnAction(e -> ouvrirPageModification(selectedSeance));

                MenuItem supprimerItem = new MenuItem("Supprimer");
                supprimerItem.setOnAction(e -> supprimerSeance(selectedSeance));

                contextMenu.getItems().addAll(modifierItem, supprimerItem);

                return contextMenu;
            }
            return null;
        });
    }

    @FXML
    public void loadData() {
        // Nettoyer le calendrier avant de recharger les données
        seancesCalendar.clear();
        entryToSeanceMap.clear();

        // Charger les données depuis la base de données via SeanceService
        List<seance> seances = seanceService.rechercher();

        // Créer des entrées de calendrier pour chaque séance
        for (seance s : seances) {
            // Créer une entrée dans le calendrier
            Entry<seance> entry = new Entry<>("Séance " + s.getId());

            // Définir les heures de début et de fin (en utilisant la date et l'heure de la séance)
            LocalDate date = s.getDate().toLocalDate();
            LocalTime time = s.getHoraire();
            LocalDateTime start = LocalDateTime.of(date, time);
            LocalDateTime end = start.plusHours(1); // Supposons que chaque séance dure 1 heure

            entry.setInterval(start, end);

            // Ajouter des informations supplémentaires à l'entrée
            entry.setLocation("Programme: " + s.getProgramme_id() + ", Activité: " + s.getActivite_id());

            // Choisir une couleur en fonction de l'activité pour faciliter la visualisation
            int colorIndex = s.getActivite_id() % 5; // Utiliser le modulo pour limiter le nombre de couleurs
            switch (colorIndex) {
                case 0: entry.setFullDay(false); break;
                case 1: entry.setFullDay(false); break;
                case 2: entry.setFullDay(false); break;
                case 3: entry.setFullDay(false); break;
                case 4: entry.setFullDay(false); break;
            }

            // Stocker la référence à la séance pour pouvoir la récupérer lors d'un clic
            entryToSeanceMap.put(entry, s);

            // Ajouter l'entrée au calendrier
            seancesCalendar.addEntry(entry);
        }
    }

    @FXML
    private void refreshCalendar(ActionEvent event) {
        loadData();
        showAlert("Mise à jour", "Les données des séances ont été rafraîchies avec succès.");
    }

    private void ouvrirPageModification(seance seance) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/seance2.fxml"));
            Parent root = loader.load();

            // Récupérer le contrôleur de la page ModifierSeance
            ModifierSeance controller = loader.getController();

            controller.setSeanceData(seance.getId(), seance.getDate().toLocalDate(), LocalTime.parse(seance.getHoraire().toString()), seance.getProgramme_id(), seance.getActivite_id());

            // Afficher la fenêtre de modification
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Modifier une Séance");
            stage.showAndWait();

            // Recharger les données après la fermeture de la fenêtre de modification
            loadData();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erreur lors du chargement de la page de modification : " + e.getMessage());
        }
    }

    @FXML
    private void allerVersAjouterSeance(ActionEvent event) {
        NavigationHelper.changerPage(event, "/seance.fxml");
    }

    @FXML
    private void allerVersAjouterApplication(ActionEvent event) {
        NavigationHelper.changerPage(event, "/application.fxml");
    }

    @FXML
    private void allerVersAjouterAvis(ActionEvent event) {
        NavigationHelper.changerPage(event, "/avis3.fxml");
    }

    @FXML
    private void allerVersAjouterProgramme(ActionEvent event) {
        NavigationHelper.changerPage(event, "/programme.fxml");
    }

    private void supprimerSeance(seance seance) {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation de suppression");
        confirmation.setHeaderText("Supprimer la seance ?");
        confirmation.setContentText("Voulez-vous vraiment supprimer cette seance ?");

        confirmation.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                boolean isDeleted = seanceService.supprimer(seance.getId());
                if (isDeleted) {
                    showAlert("Succès", "La seance a été supprimée avec succès !");
                    loadData(); // Mise à jour du calendrier après suppression
                    HistoriqueService historiqueService = new HistoriqueService();
                    historiqueService.ajouterHistorique("Suppression", "Seance",
                            "Seance supprimé:  " + seance.getId());
                } else {
                    showAlert("Erreur", "Impossible de supprimer la seance.");
                }
            }
        });
    }

    @FXML
    private void afficherHistorique() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Historique.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Historique des actions");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}