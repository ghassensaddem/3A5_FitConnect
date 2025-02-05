package com.esprit.test;

import com.esprit.models.Client;
import com.esprit.services.ClientService;
import com.esprit.models.Coach;
import com.esprit.services.CoachService;
import com.esprit.models.Admin;
import com.esprit.services.AdminService;

public class main {
    public static void main(String[] args) {

        //  ClientService ps = new ClientService();
         // cs.ajouter(new Client(6, "amen","wbch","null","32156sa","12/02/2003","amail@gmail.com",80f,1.50f,"i want to die"));
        //    cs.modifier(new Client(1, "ghassen","wbch","antha","32156sa","12/02/2003","amail@gmail.com",80f,1.50f,"i want to die"));
        //  cs.supprimer(new Client(5, "","","","","","",0f,0f,""));
       // System.out.println(cs.rechercher());

       //CoachService cs = new CoachService();
        //cs.ajouter(new Coach(1, "amen","wbch","male","32156sa","12/02/2003","amail@gmail.com","soussa","dance classic"));
        //cs.modifier(new Coach(1, "amen","aklb","female","32156sa","12/02/2003","amail@gmail.com","soussa","dance classic"));
      //  cs.supprimer(new Coach(1, "","","","","","","",""));
       //  System.out.println(cs.rechercher());

        AdminService as = new AdminService();
       // as.ajouter(new Admin(1, "amdin","amdddin","male","32156sa","12/02/2003","amail@gmail.com"));
       // as.modifier(new Admin(1, "admin","addmmin","female","32156sa","12/02/2003","amail@gmail.com"));
         as.supprimer(new Admin(1, "","","","","",""));
         System.out.println(as.rechercher());

    }
}
