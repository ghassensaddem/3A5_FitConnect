package com.esprit.tests;

import com.esprit.models.Commentaire;
import com.esprit.models.Post;
import com.esprit.services.CommentaireService;
import com.esprit.services.PostService;

public class MainProg {
    public static void main(String[] args) {
        // Création des services
        CommentaireService cs = new CommentaireService();
        PostService ps = new PostService();

        //ps.ajouter(new Post("Auteur1", "Contenu du post", 200, 2, "image1.jpg"));
        //ps.modifier(new Post(12, "aaa", "Nouveau contenu du post", 15, 3, "newImage.jpg",""));
        //ps.supprimer(new Post(11, "", "", 0, 0, "", ""));

        // Afficher tous les postes
        /*System.out.println("Liste de tous les postes : ");
        System.out.println(ps.rechercher());*/



        // ajouter commentaire
        //cs.ajouter(new Commentaire("Auteur5com", "Contenu du commentaire", 5, 1, 10));


         //cs.modifier(new Commentaire(18,"Auteur5", "Contenu du commentaire", 5, 1, 12,""));

        // Supprimer un commentaire
        //cs.supprimer(new Commentaire(18, "", "", 0, 0, 0, ""));


        // Afficher tous les commentaires
        /*System.out.println("Liste de tous les commentaires : ");
        System.out.println(cs.rechercher());*/

        // Afficher les commentaires d'un post spécifique
        /*System.out.println("Commentaires pour le post : ");
        System.out.println(ps.afficherCommentairesParPost(10));*/
    }
}
