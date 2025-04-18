package com.esprit.controllers;

import com.esprit.models.Post;
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
import javafx.util.Callback;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.chart.PieChart;
import java.util.Map;



public class AfficherPostB {

    @FXML
    private TableView<Post> postTable;
    @FXML
    private TableColumn<Post, Integer> colId;
    @FXML
    private TableColumn<Post, String> colDate;
    @FXML
    private TableColumn<Post, String> colAuthor;
    @FXML
    private TableColumn<Post, String> colContent;
    @FXML
    private TableColumn<Post, Integer> colUpvotes;
    @FXML
    private TableColumn<Post, Integer> colDownvotes;
    @FXML
    private TableColumn<Post, String> colImage;
    @FXML
    private TableColumn<Post, Void> colDelete;
    @FXML
    private TableColumn<Post, Void> colEdit;
    @FXML
    private TableColumn<Post, Void> colCommentaire;
    @FXML
    private Button btnExportCSV;

    @FXML
    private PieChart postsPieChart; // Déclaration correcte pour un PieChart

    private final ObservableList<Post> data = FXCollections.observableArrayList();
    private final PostService postService = new PostService();

    @FXML
    private Button btnAddPost;


    @FXML
    public void initialize() {
        postTable.getStylesheets().add(getClass().getResource("/styles/button.css").toExternalForm());
        loadData();
        setupCommentaireButton();
        // Ajouter un bouton Export CSV
        btnExportCSV = new Button("Exporter CSV");
        btnExportCSV.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold; -fx-border-radius: 5px; -fx-padding: 10px;");

        btnExportCSV.setOnMouseEntered(e -> btnExportCSV.setStyle("-fx-background-color: #45a049; -fx-text-fill: white;"));
        btnExportCSV.setOnMouseExited(e -> btnExportCSV.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;"));

        btnExportCSV.setOnAction(e -> exporterTousCSV());


    }


    private void loadPieChart() {
        // Récupérer les données des posts par auteur
        Map<String, Integer> postsParAuteur = postService.getPostsParAuteur();

        // Calculer le total des posts
        int totalPosts = postsParAuteur.values().stream().mapToInt(Integer::intValue).sum();

        // Ajouter les données au PieChart
        postsPieChart.getData().clear();
        for (Map.Entry<String, Integer> entry : postsParAuteur.entrySet()) {
            String author = entry.getKey();
            int count = entry.getValue();
            double percentage = (count * 100.0) / totalPosts; // Calculer le pourcentage
            PieChart.Data slice = new PieChart.Data(author + " (" + String.format("%.1f", percentage) + "%)", count);
            postsPieChart.getData().add(slice);
        }

        // Personnaliser le PieChart
        postsPieChart.setTitle("Répartition des posts par auteur");
        postsPieChart.setLegendVisible(true); // Afficher la légende
        postsPieChart.setLabelsVisible(true); // Afficher les étiquettes
    }

    @FXML
    public void loadData() {
        data.clear();
        data.addAll(postService.rechercher());

        colContent.setCellFactory(column -> {
            return new TableCell<Post, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                        setStyle("");
                    } else {
                        setText(item);
                        // Ajuster la hauteur de la cellule en fonction du contenu
                        setPrefHeight(Math.max(100, item.split("\n").length * 20));
                    }
                }
            };
        });

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colAuthor.setCellValueFactory(new PropertyValueFactory<>("author"));
        colContent.setCellValueFactory(new PropertyValueFactory<>("contenu"));
        colUpvotes.setCellValueFactory(new PropertyValueFactory<>("upvotes"));
        colDownvotes.setCellValueFactory(new PropertyValueFactory<>("downvotes"));
        colImage.setCellValueFactory(new PropertyValueFactory<>("image"));
        postTable.setItems(data);

        setupDeleteButton();
        //setupEditButton();
        setupImageColumn();
        loadPieChart(); // Charger les données dans le PieChart
    }





    @FXML
    private void exporterTousCSV() {
        String filePath = "posts_export.csv"; // Modifier le chemin si nécessaire

        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            // En-tête du fichier CSV
            writer.println("ID,Auteur,Date,Contenu,Upvotes,Downvotes,Image");

            // Récupérer les données du tableau
            List<Post> postList = postTable.getItems();

            for (Post post : postList) {
                // Gestion des valeurs nulles et formatage correct des champs texte
                String author = post.getAuthor() != null ? post.getAuthor() : "";
                String date = post.getDate() != null ? String.valueOf(post.getDate()) : "";
                String content = post.getContenu() != null ? post.getContenu().replace("\"", "\"\"") : "";
                String image = (post.getImage() != null && !post.getImage().isEmpty()) ? post.getImage() : "Aucune";

                // Écriture des données dans le CSV
                writer.println(post.getId() + ",\"" + author + "\",\"" + date + "\",\"" + content + "\","
                        + post.getUpvotes() + "," + post.getDownvotes() + ",\"" + image + "\"");
            }

            showAlert("Export CSV", "Fichier CSV enregistré avec succès : " + filePath);

        } catch (IOException e) {
            showAlert("Erreur d'exportation", "Impossible d'enregistrer le fichier CSV : " + e.getMessage());
            e.printStackTrace();
        }
    }



    private void setupCommentaireButton() {
        colCommentaire.setCellFactory(param -> new TableCell<>() {
            private final Button voirButton = new Button("Voir");

            {
                voirButton.setOnAction(event -> {
                    Post post = getTableView().getItems().get(getIndex());
                    ouvrirAfficherCommentaire(post);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : voirButton);
            }
        });
    }

    private void ouvrirAfficherCommentaire(Post post) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/affichercommentaireB.fxml"));
            Parent root = loader.load();

            // Passer l'ID du post ou tout autre information nécessaire au contrôleur
            AfficherCommentaireB controller = loader.getController();
            controller.setPostId(post.getId()); // Ajoute une méthode setPostId() dans AfficherCommentaire

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Commentaires du Post");

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    private void setupImageColumn() {
        colImage.setCellFactory(new Callback<TableColumn<Post, String>, TableCell<Post, String>>() {
            @Override
            public TableCell<Post, String> call(TableColumn<Post, String> param) {
                return new TableCell<Post, String>() {
                    private final ImageView imageView = new ImageView();

                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);

                        if (empty || item == null || item.isEmpty()) {
                            setGraphic(null);
                        } else {
                            // Charger l'image à partir du chemin de l'URL (ou d'un chemin local)
                            Image image = new Image("file:" + item); // "file:" si c'est un chemin local
                            imageView.setImage(image);
                            imageView.setFitHeight(100);  // Ajuster la taille
                            imageView.setFitWidth(100);
                            setGraphic(imageView);
                        }
                    }
                };
            }
        });
    }



    private void setupDeleteButton() {
        colDelete.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button("Supprimer");

            {
                deleteButton.setOnAction(event -> {
                    Post post = getTableView().getItems().get(getIndex());
                    postService.supprimer(post);
                    loadData();
                    showAlert("Suppression réussie", "Le post a été supprimé avec succès.");
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : deleteButton);
            }
        });
    }

    /*private void setupEditButton() {
        colEdit.setCellFactory(param -> new TableCell<>() {
            private final Button editButton = new Button("Modifier");

            {
                editButton.setStyle("-fx-background-color: #FFA500; -fx-text-fill: white; -fx-font-weight: bold;");
                editButton.setOnAction(event -> {
                    Post post = getTableView().getItems().get(getIndex());
                    ouvrirModifierPost(post);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : editButton);
            }
        });
    }*/

    /*private void ouvrirModifierPost(Post post) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/modifierposte.fxml"));
            Parent root = loader.load();
            ModifierPost controller = loader.getController();
            controller.initData(post);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Modifier un Post");

            // Ajouter un listener pour détecter la fermeture de la fenêtre et rafraîchir la table
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
