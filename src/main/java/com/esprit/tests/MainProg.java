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
        //EventService ps = new EventService();
        //typeactiviteService ts = new typeactiviteService();
       // activiteEventService ac= new activiteEventService();

     //   ts.ajouter(new typeactivite("compet","hi we are playing"));
      //ac.ajouter(new activiteevent("11/2/2024",23,1,15));
       //ac.modifier(new activiteevent(20,"14:50",5,1,16));
       // ac.supprimer(new activiteevent(21,"11/2/2024",23,1,15));

      // ts.modifier(new typeactivite(15,"coppp","gtt"));
     //ps.ajouter(new Event("11/2/2024", 23,"MREZGA","14H30"));
       // ps.modifier(new Event(1,"sidibousiid", 25,"salah","14:30"));
        //ps.supprimer(new Event(1,"11/2/2024", 23, "MREZGA","14:30"));
      //  ts.supprimer(new typeactivite(16,"compet","hi we are playing"));

        //System.out.println(ps.rechercher());
       // System.out.println(ac.rechercher());
        //System.out.println(ts.rechercher());
        /////////////////////////////////////////////USER///////////////////////////////////////////////////////
      //    ClientService cs = new ClientService();
      //  cs.ajouter(new Client(6, "amen","wbch","null","32156sa","12/02/2003","amail@gmail.com",80f,1.50f,"i want to die",3,1));
         // cs.modifier(new Client(64, "ghassen","behi","antha","32156sa","12/02/2003","amail@gmail.com",80f,1.50f,"i want to die",3,1));
        //cs.supprimer(new Client(64, "","","","","","",0f,0f,"",0,1));
       // System.out.println(cs.rechercher());

       // CoachService ccs = new CoachService();
      //  ccs.ajouter(new Coach(1, "amen","wbch","male","32156sa","12/02/2003","amail@gmail.com","soussa","dance classic"));
       // ccs.modifier(new Coach(9, "ghassen","behi","femme","32156sa","12/02/2003","amail@gmail.com","soussa","dance classic"));
       //  ccs.supprimer(new Coach(5, "","","","","","","",""));
       //   System.out.println(ccs.rechercher());

     //   AdminService as = new AdminService();
     //  as.ajouter(new Admin(1, "admin","amdddin","male","32156sa","12/02/2003","amail@gmail.com"));
       //as.modifier(new Admin(2, "admin","addmmin","HELICOPTER","32156sa","12/02/2003","amail@gmail.com"));
        //as.supprimer(new Admin(10, "","","","","",""));
       // System.out.println(as.rechercher());
        ///////////////////emir/////////////////////////////////////////////////
       //EquipementService es = new  EquipementService ();
        //  es.ajouter(new Equipement("mimou", "tapi khaybaaaa bara",18.5,5,1));
          //es.modifier(new Equipement(7,"tapi", "tapi ki wejhi",18.5,6,1));
       // es.supprimer(new Equipement(7,"tapi", "tapi ki nomi",18.5,5,1));
       // System.out.println(es.rechercher());
         // List<CategorieEquipement> tab=(new CategorieService()).getAllCategories();
       // EquipementService ps = new  EquipementService ();
      //  CategorieService ls = new  CategorieService();
       //  ls.ajouter(new CategorieEquipement("boxe", "boxe behi barcha"));
       // ls.modifier(new CategorieEquipement(1, "chante", "chanchouta"));
        // ls.supprimer(new CategorieEquipement(1, "chante", "chanchouta"));
        //System.out.println(ls.rechercher());


        // CommandeService pps = new CommandeService ();
        // Commande nouvelleCommande = new Commande(65, "en livraison", LocalDateTime.now().plusDays(3), "en attente");
        // pps.ajouter(nouvelleCommande);
         //pps.modifier(new Commande(16,65, "en commande", LocalDateTime.now().plusDays(3), "en attente"));
        // pps.supprimer(new Commande(16,8, "en commande", LocalDateTime.now().plusDays(3), "en attente"));
        // System.out.println(pps.rechercher());

        //Commande_EquipementService mps = new   Commande_EquipementService ();
       // mps.ajouter(new Commande_Equipement(16,8,3,18.50));
         //mps.modifier(new Commande_Equipement(12,16,8,10,18.60));
      //  mps.supprimer(new Commande_Equipement(12,5,7,2,18.50));
      //  System.out.println(mps.rechercher());
        //System.out.println(ps.getEquipementsParCommandeId(22));

        //////////////saiff////////
       // CommentaireService com = new CommentaireService();
       // PostService post = new PostService();

       // post.ajouter(new Post("Auteur1", "Contenu du post", 200, 2, "image1.jpg", "", 65));
      // post.modifier(new Post(8, "KAWAZAKI", "Nouveau contenu du post", 15, 3, "newImage.jpg"));
      //  post.supprimer(new Post(8, "", "", 0, 0,""));

        // Afficher tous les postes
      //  System.out.println("Liste de tous les postes : ");
       // System.out.println(post.rechercher());



        // ajouter commentaire
      // com.ajouter(new Commentaire("Auteur5com", "Contenu du commentaire", 5, 7, "",8,65));


       // com.modifier(new Commentaire(48,"beautiful KAWAZAKI", "Contenu du commentaire", 5, 77));

        // Supprimer un commentaire
       // com.supprimer(new Commentaire(48, "", "", 0, 0));


        // Afficher tous les commentaires
        //System.out.println("Liste de tous les commentaires : ");
       // System.out.println(com.rechercher());

        // Afficher les commentaires d'un post spécifique
        //System.out.println("Commentaires pour le post : ");
        //System.out.println(post.afficherCommentairesParPost(5));

       // ProgrammeService vs = new ProgrammeService();
       // SeanceService ss = new SeanceService();
     //   AvisService avis = new AvisService();

       // vs.ajouter(new Programme(2, LocalDate.of(2024, 3, 2), LocalDate.of(2024, 6, 4), 3.5, "qhjc",10));
       // vs.modifier(new Programme(1, LocalDate.of(2024, 3, 3), LocalDate.of(2024, 6, 4), 3.2, "mouchaloucha",10));
      //  vs.supprimer(new Programme(1, LocalDate.of(2024, 3, 3), LocalDate.of(2024, 3, 3), 0.0, "",1));

        //  ss.ajouter(new seance(0, LocalDate.of(2024, 3, 10), LocalTime.of(14, 30), "Salle B", 3,1));
       // ss.modifier(new seance(4, LocalDate.of(2024, 3, 12), LocalTime.of(16, 0), "Salle Z", 3,1));
      //  ss.supprimer(new seance(4, null, null, "", 3,1));

        //avis.ajouter(new Avis(0, "bien", 6, 4));
       //avis.modifier(new Avis(3, "mauvais", 7, 4));
        //avis.supprimer(new Avis(3, "mauvais", 8, 2));
        //System.out.println(vs.rechercher());
        ///////////nassim////////////////////
       // ActivityService activityService = new ActivityService();
      //  SalleSportService salleSportService = new SalleSportService();
       // PlanningService planning = new PlanningService();
       // RatingService rating = new RatingService();


        //activityService.ajouter(new activity(2, "wiou", "aaa", "aaa"));
        //activityService.modifier(new activity(1, "za", "aaa", "aab"));
      //  activityService.supprimer(new activity(3, "wiou", "aaa", "aaa"));
       // System.out.println(activityService.rechercher());

       // salleSportService.ajouter(new SalleSportif(0, "wiou", "aaa", LocalTime.parse("21:00"),LocalTime.parse("20:00"), 15));
        //salleSportService.modifier(new SalleSportif(7, "chrabanda9", "aaa", LocalTime.parse("21:00"),LocalTime.parse("20:00"), 15));
      //  salleSportService.supprimer(new SalleSportif(7, "3ala zebii", "aaa", LocalTime.parse("06:00"),LocalTime.parse("20:00"), 15));
      //  System.out.println(salleSportService.rechercher());

       // planning.ajouter(new PlanningActivity(1, 2, 1550, 20));
        //planning.supprimer(new PlanningActivity(1, 2, 50, 20));
        //planning.ajouter(new PlanningActivity(2, 2, 80, 80));
       // planning.modifier(new PlanningActivity(1, 2, 2500, 1));
       // System.out.println(planning.rechercher());

        //rating.ajouter(new RatingActivity(1, 2, 5, "rayen rojla baaaarchaaa"));
        //rating.supprimer(new RatingActivity(1, 2, 5, "rayen rojla"));
        //rating.ajouter(new RatingActivity(2, 2, 5, "nassim rojla "));
        //rating.modifier(new RatingActivity(1, 2, 5, "wiouuuuu"));
        //System.out.println(rating.rechercher());

    }
}
