package com.esprit.controllers;

import com.esprit.models.Commentaire;
import com.esprit.services.CommentaireService;
import com.esprit.services.PostService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.List;

public class AfficherCommentaireB {

    @FXML
    private TableView<Commentaire> comTable;
    @FXML
    private TableColumn<Commentaire, Integer> colcomId;
    @FXML
    private TableColumn<Commentaire, String> colcomDate;
    @FXML
    private TableColumn<Commentaire, String> colcomAuthor;
    @FXML
    private TableColumn<Commentaire, String> colcomContent;
    @FXML
    private TableColumn<Commentaire, Integer> colcomUpvotes;
    @FXML
    private TableColumn<Commentaire, Integer> colcomDownvotes;
    @FXML
    private TableColumn<Commentaire, String> colcomImage;
    @FXML
    private TableColumn<Commentaire, Void> colcomDelete;
    @FXML
    //private TableColumn<Commentaire, Void> colcomEdit;

    private TableColumn<Commentaire, Integer> colcomPostId;

    private final ObservableList<Commentaire> data = FXCollections.observableArrayList();
    private final CommentaireService commentaireService = new CommentaireService();
    private final PostService postService = new PostService();

    @FXML
    private Button btnAddCommentaire;

    private int postId;

    @FXML
    public void initialize() {
        loadData();
    }

    public void setPostId(int postId) {
        this.postId = postId;
        loadData();  // Recharger les données avec le bon filtre
    }


    @FXML
    public void loadData() {
        data.clear();
        // Appel de la méthode du service pour récupérer les commentaires pour ce post
        List<Commentaire> commentaires = postService.afficherCommentairesParPost(postId);

        // Ajout des commentaires à la liste observable
        data.addAll(commentaires);

        // Liens des colonnes avec les propriétés des Commentaires
        colcomId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colcomDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colcomAuthor.setCellValueFactory(new PropertyValueFactory<>("author"));
        colcomContent.setCellValueFactory(new PropertyValueFactory<>("contenu"));
        colcomUpvotes.setCellValueFactory(new PropertyValueFactory<>("upvotes"));
        colcomDownvotes.setCellValueFactory(new PropertyValueFactory<>("downvotes"));

        comTable.setItems(data);

        setupDeleteButton();
        //setupEditButton();
        //setupAddButton();

    }




    private void setupDeleteButton() {
        colcomDelete.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button("Supprimer");

            {
                deleteButton.setOnAction(event -> {
                    Commentaire commentaire = getTableView().getItems().get(getIndex());
                    commentaireService.supprimer(commentaire);
                    loadData();
                    showAlert("Suppression réussie", "Le commentaire a été supprimé avec succès.");
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                // Met à jour la cellule pour afficher un bouton de suppression,
                // uniquement si la cellule n'est pas vide.
                super.updateItem(item, empty);
                setGraphic(empty ? null : deleteButton);
            }

        });
    }

    /*private void setupEditButton() {
        colcomEdit.setCellFactory(param -> new TableCell<>() {
            private final Button editButton = new Button("Modifier");

            {
                editButton.setStyle("-fx-background-color: #FFA500; -fx-text-fill: white; -fx-font-weight: bold;");
                editButton.setOnAction(event -> {
                    Commentaire commentaire = getTableView().getItems().get(getIndex());
                    ouvrirModifierCommentaire(commentaire);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : editButton);
            }
        });
    }

    private void ouvrirModifierCommentaire(Commentaire commentaire) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/modifiercommentaire.fxml"));
            Parent root = loader.load();
            ModifierCommentaire controller = loader.getController();
            controller.initData(commentaire);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Modifier un Commentaire");

            // Ajouter un listener pour détecter la fermeture de la fenêtre et rafraîchir la table
            stage.setOnHiding(event -> loadData());

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

    /*private void setupAddButton() {
        btnAddCommentaire.setOnAction(event -> ouvrirAjout());
    }

    private void ouvrirAjout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ajoutercommentaire.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Ajouter un Commentaire");

            // Rafraîchir la table après la fermeture de la fenêtre d'ajout
            stage.setOnHiding(event -> loadData());

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

    @FXML
    private void refreshTable() {
        loadData();
        showAlert("Mise à jour", "Les données ont été rafraîchies avec succès.");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
