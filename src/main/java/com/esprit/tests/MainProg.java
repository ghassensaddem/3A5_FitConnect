package com.esprit.tests;
import java.time.LocalDate;

import com.esprit.models.Commentaire;
import com.esprit.models.Post;
import com.esprit.services.CommentaireService;
import com.esprit.services.PostService;

public class MainProg {
    public static void main(String[] args) {
        CommentaireService com = new CommentaireService();
        PostService post = new PostService();

        //post.ajouter(new Post("aa", "new post", 200, 2, "image1.jpg", LocalDate.now(), 5));
        //post.modifier(new Post(13, "la", "ergergegr", 15, 3, "newImage.jpg"));
        //post.supprimer(new Post(14, "", "", 0, 0,""));

        // Afficher tous les postes
        System.out.println("Liste de tous les postes : ");
        System.out.println(post.rechercher());


        // ajouter commentaire
        //com.ajouter(new Commentaire("Auteur5com", "Contenu du commentaire", 5, 7, LocalDate.now(),9,5));


        //com.modifier(new Commentaire(10,"aakka", "Contenu du commentaire", 5, 77));

        // Supprimer un commentaire
        //com.supprimer(new Commentaire(9, "", "", 0, 0));


        // Afficher tous les commentaires
        /*System.out.println("Liste de tous les commentaires : ");
        System.out.println(com.rechercher());*/

        // Afficher les commentaires d'un post spécifique
        /*System.out.println("Commentaires pour le post : ");
        System.out.println(post.afficherCommentairesParPost(9));*/
    }
}
