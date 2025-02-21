package com.esprit.controllers;

import com.esprit.models.SalleSportif;
import com.esprit.services.SalleSportService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ModifierSalle {
    @FXML
    private TextField nomSalleField;
    @FXML
    private TextField addresseSalleField;
    @FXML
    private TextField capaciteField;

    private SalleSportif salleActuelle;
    private final SalleSportService salleService = new SalleSportService();
    private Runnable callback;

    public void setOnModificationComplete(Runnable callback) {
        this.callback = callback;
    }

    public void initData(SalleSportif salle) {
        this.salleActuelle = salle;
        if (salle != null) {
            nomSalleField.setText(salle.getNomSalle());
            addresseSalleField.setText(salle.getAddresseSalle());
            capaciteField.setText(String.valueOf(salle.getCapacity()));
        }
    }

    @FXML
    private void enregistrerModification() {
        if (salleActuelle == null) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Aucune salle sélectionnée.");
            return;
        }

        // Récupérer les valeurs modifiées et les nettoyer
        String nouveauNom = nomSalleField.getText().trim();
        String nouvelleAdresse = addresseSalleField.getText().trim();
        String capaciteTexte = capaciteField.getText().trim();

        // Validation stricte de la saisie
        int nouvelleCapacite = validerSaisie(nouveauNom, nouvelleAdresse, capaciteTexte);
        if (nouvelleCapacite == -1) return;

        // Mise à jour des données de la salle
        salleActuelle.setNomSalle(nouveauNom);
        salleActuelle.setAddresseSalle(nouvelleAdresse);
        salleActuelle.setCapacity(nouvelleCapacite);

        // Mise à jour dans la base de données
        salleService.modifier(salleActuelle);
        afficherAlerte(Alert.AlertType.INFORMATION, "Succès", "La salle a été modifiée avec succès.");

        // Rafraîchir la table après modification
        if (callback != null) {
            callback.run();
        }

        // Fermer la fenêtre après modification
        Stage stage = (Stage) nomSalleField.getScene().getWindow();
        stage.close();
    }

    private int validerSaisie(String nom, String adresse, String capaciteTexte) {
        if (nom.isEmpty() || adresse.isEmpty() || capaciteTexte.isEmpty()) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Tous les champs doivent être remplis !");
            return -1;
        }

        if (nom.length() < 3 || !nom.matches("[a-zA-Z\\s]+")) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Le nom de la salle doit contenir au moins 3 lettres sans chiffres ni caractères spéciaux !");
            return -1;
        }

        if (adresse.length() < 3) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "L'adresse doit contenir au moins 3 caractères !");
            return -1;
        }

        try {
            int capacite = Integer.parseInt(capaciteTexte);
            if (capacite <= 0 || capacite > 500) {
                afficherAlerte(Alert.AlertType.ERROR, "Erreur", "La capacité doit être un nombre entre 1 et 500 !");
                return -1;
            }
            return capacite;
        } catch (NumberFormatException e) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Veuillez entrer un nombre valide pour la capacité !");
            return -1;
        }
    }

    private void afficherAlerte(Alert.AlertType type, String titre, String contenu) {
        Alert alert = new Alert(type);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(contenu);
        alert.showAndWait();
    }
}
