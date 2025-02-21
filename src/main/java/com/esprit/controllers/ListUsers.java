package com.esprit.controllers;

import com.esprit.models.Admin;
import com.esprit.models.Client;
import com.esprit.models.Coach;
import com.esprit.models.User;
import com.esprit.services.AdminService;
import com.esprit.services.ClientService;
import com.esprit.services.CoachService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

public class ListUsers {

    public TextField txtSearch;

    @FXML
    private GridPane usersGrid;
    @FXML
    private ToggleButton btnClients;
    @FXML
    private ToggleButton btnCoachs;
    @FXML
    public ToggleButton btnAdmins;

    private final ToggleGroup toggleGroup = new ToggleGroup();
    private final ClientService clientService = new ClientService();
    private final CoachService coachService = new CoachService();
    private final AdminService adminService = new AdminService();

    public void setBtnCoachs() {
        btnCoachs.setSelected(true);
    }

    public void setBtnAdmins() {
        btnAdmins.setSelected(true);
    }

    public void initialize() {
        btnClients.setToggleGroup(toggleGroup);
        btnCoachs.setToggleGroup(toggleGroup);
        btnAdmins.setToggleGroup(toggleGroup);

            toggleGroup.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            usersGrid.getChildren().clear();
            ArrayList<User> users = new ArrayList<>();

            if (newToggle == btnClients) {
                users.addAll(clientService.rechercher());
            } else if (newToggle == btnCoachs) {
                users.addAll(coachService.rechercher());
            } else if (newToggle == btnAdmins) {
                users.addAll(adminService.rechercher());
            }

            txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
                rechercherParNom(newValue);
            });

            populateUsersGrid(users);
        });

        btnClients.setSelected(true);
        usersGrid.getChildren().clear();
        ArrayList<User> users = new ArrayList<>();
        users.addAll(clientService.rechercher());
        populateUsersGrid(users);
    }

    private void populateUsersGrid(ArrayList<User> users) {
        int columns = 5;
        int column = 0;
        int row = 0;

        for (User user : users) {
            VBox userCard = createUserCard(user);
            usersGrid.add(userCard, column, row);

            column++;
            if (column == columns) {
                column = 0;
                row++;
            }
        }
    }

    private VBox createUserCard(User user) {
        VBox card = new VBox(10);
        card.setAlignment(Pos.CENTER);
        card.getStyleClass().add("user-card");
        card.setUserData(user);

        ImageView imageView = new ImageView();
        imageView.setImage(new Image(user.getImage()));
        imageView.setFitWidth(80);
        imageView.setFitHeight(80);

        Label nameLabel = new Label(user.getNom() + " " + user.getPrenom());
        nameLabel.getStyleClass().add("user-name");

        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);

        Button btnModifier = new Button("Modifier");
        Button btnSupprimer = new Button("Supprimer");

        btnModifier.setOnAction(e -> {
            try {
                if (user instanceof Client) {
                    modifierClient((Client) user);
                } else if (user instanceof Coach) {
                    modifierCoach((Coach) user);
                } else if (user instanceof Admin) {
                    modifierAdmin((Admin) user);
                }
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        btnSupprimer.setOnAction(e -> supprimerUser(card));

        buttonBox.getChildren().addAll(btnModifier, btnSupprimer);

        card.getChildren().addAll(imageView, nameLabel, buttonBox);

        return card;
    }

    private void modifierClient(Client client) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierClient.fxml"));
        Parent root = loader.load();
        usersGrid.getScene().setRoot(root);

        ModifierClient ls = loader.getController();
        ls.setId(client.getId());
        ls.setNom(client.getNom());
        ls.setPrenom(client.getPrenom());
        ls.initialize();
        if (client.getSexe().equals("homme")) {
            ls.setCheckHomme();
        } else {
            ls.setCheckFemme();
        }

        ls.setMdp(client.getMdp());
        ls.setDateNaissance(client.getDateNaissance());
        ls.setPoids(client.getPoids());
        ls.setTaille(client.getTaille());
        ls.setEmail(client.getEmail());
        ls.setPdpIconLabelVisible(client.getImage() == null);
        ls.setPdpImageView(client.getImage());
        ls.setPdpPathField(client.getImage());
    }

    private void modifierCoach(Coach coach) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierCoach.fxml"));
        Parent root = loader.load();
        usersGrid.getScene().setRoot(root);

        ModifierCoach ls = loader.getController();
        ls.initialize();
        ls.setId(coach.getId());
        ls.setNom(coach.getNom());
        ls.setPrenom(coach.getPrenom());
        ls.setSexe(coach.getSexe());
        ls.setMdp(coach.getMdp());
        ls.setDateNaissance(coach.getDateNaissance());
        ls.setSpecialite(coach.getSpecialite());
        ls.setLieuEngagement(coach.getLieuEngagement());
        ls.setEmail(coach.getEmail());
        ls.setPdpIconLabelVisible(coach.getImage() == null);
        ls.setPdpImageView(coach.getImage());
        ls.setPdpPathField(coach.getImage());
    }

    private void modifierAdmin(Admin admin) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierAdmin.fxml"));
        Parent root = loader.load();
        usersGrid.getScene().setRoot(root);

        ModifierAdmin ls = loader.getController();
        ls.initialize();
        ls.setId(admin.getId());
        ls.setNom(admin.getNom());
        ls.setPrenom(admin.getPrenom());
        ls.setSexe(admin.getSexe());
        ls.setMdp(admin.getMdp());
        ls.setDateNaissance(admin.getDateNaissance());
        ls.setEmail(admin.getEmail());
        ls.setPdpIconLabelVisible(admin.getImage() == null);
        ls.setPdpImageView(admin.getImage());
        ls.setPdpPathField(admin.getImage());
    }

    private void supprimerUser(VBox card) {
        if (card == null) {
            System.out.println("Erreur : la carte est null.");
            return;
        }

        Object data = card.getUserData();
        if (!(data instanceof User)) {
            System.out.println("Erreur : la carte ne contient pas d'utilisateur valide.");
            return;
        }

        User user = (User) data;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText("Voulez-vous vraiment supprimer cet utilisateur ?");
        alert.setContentText("Cette action est irréversible.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            if (user instanceof Client) {
                clientService.supprimer((Client) user);
            } else if (user instanceof Coach) {
                coachService.supprimer((Coach) user);
            } else if (user instanceof Admin) {
                adminService.supprimer((Admin) user);
            }

            usersGrid.getChildren().remove(card);
            refreshGrid();
        }
    }

    private void refreshGrid() {
        usersGrid.getChildren().clear();
        ArrayList<User> users = new ArrayList<>();

        if (btnClients.isSelected()) {
            users.addAll(clientService.rechercher());
        } else if (btnCoachs.isSelected()) {
            users.addAll(coachService.rechercher());
        } else if (btnAdmins.isSelected()) {
            users.addAll(adminService.rechercher());
        }

        populateUsersGrid(users);
    }

    @FXML
    void Ajouter(ActionEvent event) throws IOException {
        if (btnClients.isSelected()) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterClient.fxml"));
            Parent root = loader.load();
            usersGrid.getScene().setRoot(root);
        } else if (btnCoachs.isSelected()) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterCoach.fxml"));
            Parent root = loader.load();
            usersGrid.getScene().setRoot(root);
        } else if (btnAdmins.isSelected()) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterAdmin.fxml"));
            Parent root = loader.load();
            usersGrid.getScene().setRoot(root);
        }
    }

    private void rechercherParNom(String nom) {
        usersGrid.getChildren().clear();
        ArrayList<User> users = new ArrayList<>();

        if (btnClients.isSelected()) {
            users.addAll(clientService.rechercherParNom(nom));
        } else if (btnCoachs.isSelected()) {
            users.addAll(coachService.rechercherParNom(nom));
        } else if (btnAdmins.isSelected()) {
            users.addAll(adminService.rechercherParNom(nom));
        }

        populateUsersGrid(users);
    }

    public void GoToEvennement(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/event3.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
    }

    public void GoToActivities(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ActivityAjouter.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
    }

    public void GoToProgramme(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/programme.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
    }

    public void GoToEquipement(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/afficher_equipement.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
    }

    public void GoToForum(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/afficherposteB.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
    }
}