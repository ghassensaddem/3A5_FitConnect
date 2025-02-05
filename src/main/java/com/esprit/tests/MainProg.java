package com.esprit.tests;

import com.esprit.models.Event;
import com.esprit.services.EventService;
import com.esprit.models.activiteevent;
import com.esprit.services.activiteEventService;
import com.esprit.models.typeactivite;
import com.esprit.services.typeactiviteService;
import com.esprit.utils.DataSource;
import com.esprit.models.Client;
import com.esprit.services.ClientService;
import com.esprit.models.Coach;
import com.esprit.services.CoachService;
import com.esprit.models.Admin;
import com.esprit.services.AdminService;

public class MainProg {
    public static void main(String[] args) {
        EventService ps = new EventService();
        typeactiviteService ts = new typeactiviteService();
        activiteEventService ac= new activiteEventService();

   // ac.ajouter(new activiteevent("11/2/2024",23,1));
      //  ac.modifier(new activiteevent("14:30",5,1));
       // ac.supprimer(new activiteevent("11/2/2024",23,1));
       // ts.ajouter(new typeactivite("compet","hi we are playing",1));
       // ts.modifier(new typeactivite("cop","gtt",3));
    //ps.ajouter(new Event("11/2/2024", 23,"MREZGA","14H30"));
        //ps.modifier(new Event(1,"hammamet", 25,"salah","14:30"));
        //ps.supprimer(new Event(2,"11/2/2024", 23, "MREZGA","14:30"));

        System.out.println(ps.rechercher());
        System.out.println(ac.rechercher());
        System.out.println(ts.rechercher());
        /////////////////////////////////////////////USER///////////////////////////////////////////////////////
          ClientService cs = new ClientService();
         cs.ajouter(new Client(6, "amen","wbch","null","32156sa","12/02/2003","amail@gmail.com",80f,1.50f,"i want to die",1));
          //cs.modifier(new Client(1, "ghassen","wbch","antha","32156sa","12/02/2003","amail@gmail.com",80f,1.50f,"i want to die",3));
        //cs.supprimer(new Client(1, "","","","","","",0f,0f,"",0));
        System.out.println(cs.rechercher());

        CoachService ccs = new CoachService();
        ccs.ajouter(new Coach(1, "amen","wbch","male","32156sa","12/02/2003","amail@gmail.com","soussa","dance classic"));
        //ccs.modifier(new Coach(1, "amen","aklb","femme","32156sa","12/02/2003","amail@gmail.com","soussa","dance classic"));
        // ccs.supprimer(new Coach(1, "","","","","","","",""));
          System.out.println(ccs.rechercher());

        AdminService as = new AdminService();
        as.ajouter(new Admin(1, "amdin","amdddin","male","32156sa","12/02/2003","amail@gmail.com"));
       // as.modifier(new Admin(1, "admin","addmmin","homme","32156sa","12/02/2003","amail@gmail.com"));
        //as.supprimer(new Admin(1, "","","","","",""));
        System.out.println(as.rechercher());
    }
}
