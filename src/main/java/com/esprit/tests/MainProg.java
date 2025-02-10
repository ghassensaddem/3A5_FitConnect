package com.esprit.tests;

import com.esprit.models.*;
import com.esprit.services.*;
import com.esprit.utils.DataSource;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class MainProg {
    public static void main(String[] args) {
        EventService ps = new EventService();
        typeactiviteService ts = new typeactiviteService();
        activiteEventService ac= new activiteEventService();

   // ac.ajouter(new activiteevent("11/2/2024",23,1));
      //  ac.modifier(new activiteevent("14:30",5,1));
       // ac.supprimer(new activiteevent("11/2/2024",23,1));
       // ts.ajouter(new typeactivite("compet","hi we are playing",1));
      // ts.modifier(new typeactivite(1,"coppp","gtt",1));
    //ps.ajouter(new Event("11/2/2024", 23,"MREZGA","14H30"));
        //ps.modifier(new Event(1,"hammamet", 25,"salah","14:30"));
        //ps.supprimer(new Event(2,"11/2/2024", 23, "MREZGA","14:30"));

        System.out.println(ps.rechercher());
        System.out.println(ac.rechercher());
        System.out.println(ts.rechercher());
        /////////////////////////////////////////////USER///////////////////////////////////////////////////////
          ClientService cs = new ClientService();
         cs.ajouter(new Client(6, "amen","wbch","null","32156sa","12/02/2003","amail@gmail.com",80f,1.50f,"i want to die",1,1));
          //cs.modifier(new Client(1, "ghassen","wbch","antha","32156sa","12/02/2003","amail@gmail.com",80f,1.50f,"i want to die",3,1));
        //cs.supprimer(new Client(1, "","","","","","",0f,0f,"",0,1));
        System.out.println(cs.rechercher());

        CoachService ccs = new CoachService();
       // ccs.ajouter(new Coach(1, "amen","wbch","male","32156sa","12/02/2003","amail@gmail.com","soussa","dance classic"));
        //ccs.modifier(new Coach(1, "amen","aklb","femme","32156sa","12/02/2003","amail@gmail.com","soussa","dance classic"));
        // ccs.supprimer(new Coach(1, "","","","","","","",""));
          System.out.println(ccs.rechercher());

        AdminService as = new AdminService();
       // as.ajouter(new Admin(1, "amdin","amdddin","male","32156sa","12/02/2003","amail@gmail.com"));
       // as.modifier(new Admin(1, "admin","addmmin","homme","32156sa","12/02/2003","amail@gmail.com"));
        //as.supprimer(new Admin(1, "","","","","",""));
        System.out.println(as.rechercher());
        ///////////////////emir/////////////////////////////////////////////////
        EquipementService es = new  EquipementService ();
        //es.ajouter(new Equipement("mimou", "tapi behiya bara",18.5,5,1));
        //es.modifier(new Equipement(5,"tapi", "tapi ki nomi",18.5,5,1));
       // es.supprimer(new Equipement(5,"tapi", "tapi ki nomi",18.5,5,1));
        //System.out.println(es.rechercher());
         // List<CategorieEquipement> tab=(new CategorieService()).getAllCategories();
       // EquipementService ps = new  EquipementService ();
        CategorieService ls = new  CategorieService();
        // ls.ajouter(new CategorieEquipement("dance", "dance behi barcha"));
        //ls.modifier(new CategorieEquipement(2, "chante", "chanchouta"));
         //ls.supprimer(new CategorieEquipement(2, "chante", "chanchouta"));
        //System.out.println(ls.rechercher());


        CommandeService pps = new CommandeService ();
         Commande nouvelleCommande = new Commande(8, "en livraison", LocalDateTime.now().plusDays(3), "en attente");
         pps.ajouter(nouvelleCommande);
//pps.modifier(new Commande(4,8, "en commande", LocalDateTime.now().plusDays(3), "en attente"));
//pps.supprimer(new Commande(4,8, "en commande", LocalDateTime.now().plusDays(3), "en attente"));
        //System.out.println(pps.rechercher());

        Commande_EquipementService mps = new   Commande_EquipementService ();
        mps.ajouter(new Commande_Equipement(5,7,3,18.50));
         //mps.modifier(new Commande_Equipement(1,5,7,2,18.50));
        mps.supprimer(new Commande_Equipement(1,5,7,2,18.50));
        System.out.println(mps.rechercher());
        //System.out.println(ps.getEquipementsParCommandeId(22));

        //////////////saiff////////
        CommentaireService com = new CommentaireService();
        PostService post = new PostService();

       // post.ajouter(new Post("Auteur1", "Contenu du post", 200, 2, "image1.jpg", "", 8));
       // post.modifier(new Post(4, "y", "Nouveau contenu du post", 15, 3, "newImage.jpg"));
        //post.supprimer(new Post(4, "", "", 0, 0,""));

        // Afficher tous les postes
        /*System.out.println("Liste de tous les postes : ");*/
        //System.out.println(post.rechercher());



        // ajouter commentaire
        com.ajouter(new Commentaire("Auteur5com", "Contenu du commentaire", 5, 7, "",5,8));


        //com.modifier(new Commentaire(7,"aaa", "Contenu du commentaire", 5, 77));

        // Supprimer un commentaire
        //com.supprimer(new Commentaire(7, "", "", 0, 0));


        // Afficher tous les commentaires
        //System.out.println("Liste de tous les commentaires : ");
        System.out.println(com.rechercher());

        // Afficher les commentaires d'un post spécifique
        /*System.out.println("Commentaires pour le post : ");*/
        //System.out.println(post.afficherCommentairesParPost(5));

        ProgrammeService vs = new ProgrammeService();
        SeanceService ss = new SeanceService();
        AvisService avis = new AvisService();

        //vs.ajouter(new Programme(2, LocalDate.of(2024, 3, 2), LocalDate.of(2024, 6, 4), 3.5, "qhjc"));
        //ps.ajouter(new Programme(3, LocalDate.of(2024, 3, 2), LocalDate.of(2024, 6, 4), 9.5, "abvzf"));
        //ps.modifier(new Programme(1, LocalDate.of(2024, 3, 3), LocalDate.of(2024, 6, 4), 3.2, "qhyedgy"));
        //ps.supprimer(new Programme(1, null, null, 0.0, ""));  // Suppression : les valeurs nulles suffisent
          //ss.ajouter(new seance(0, LocalDate.of(2024, 3, 10), LocalTime.of(14, 30), "Salle B", 2));
        // Modifier une séance (exemple: changer la date et le lieu de la séance avec id=5)
        //ss.modifier(new seance(2, LocalDate.of(2024, 3, 12), LocalTime.of(16, 0), "Salle C", 2));
        // Supprimer une séance avec id=6
        //ss.supprimer(new seance(1, null, null, "", 0));
        //avis.ajouter(new Avis(0, "bien", 6, 2));
        //avis.modifier(new Avis(2, "mauvais", 7, 2));
        //avis.supprimer(new Avis(1, "mauvais", 8, 2));
        //System.out.println(vs.rechercher());
        ///////////nassim////////////////////
        ActivityService activityService = new ActivityService();
        SalleSportService salleSportService = new SalleSportService();
        PlanningService planning = new PlanningService();
        RatingService rating = new RatingService();


       // activityService.ajouter(new activity(2, "wiou", "aaa", "aaa"));
        //activityService.modifier(new activity(1, "za", "aaa", "aab"));
        //activityService.supprimer(new activity(1, "wiou", "aaa", "aaa"));
        //System.out.println(activityService.rechercher());

        //salleSportService.ajouter(new SalleSportif(0, "wiou", "aaa", LocalTime.parse("21:00"),LocalTime.parse("20:00"), 15));
        //salleSportService.modifier(new SalleSportif(1, "chrabanda9", "aaa", LocalTime.parse("21:00"),LocalTime.parse("20:00"), 15));
        //salleSportService.supprimer(new SalleSportif(1, "3ala zebii", "aaa", LocalTime.parse("06:00"),LocalTime.parse("20:00"), 15));
        //System.out.println(salleSportService.rechercher());

        //planning.ajouter(new PlanningActivity(2, 2, 50, 20));
        //planning.supprimer(new PlanningActivity(1, 2, 50, 20));
        //planning.ajouter(new PlanningActivity(2, 2, 80, 80));
        //planning.modifier(new PlanningActivity(1, 1, 500, 1));
        //System.out.println(planning.rechercher());

        //rating.ajouter(new RatingActivity(2, 2, 5, "rayen rojla"));
        //rating.supprimer(new RatingActivity(1, 2, 5, "rayen rojla"));
        //rating.ajouter(new RatingActivity(2, 2, 5, "nassim rojla "));
        //rating.modifier(new RatingActivity(1, 1, 5, "wiouuuuu"));
        //System.out.println(rating.rechercher());

    }
}
