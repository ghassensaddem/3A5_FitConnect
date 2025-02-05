package com.esprit.tests;

import com.esprit.models.Event;
import com.esprit.services.EventService;
import com.esprit.models.activiteevent;
import com.esprit.services.activiteEventService;
import com.esprit.models.typeactivite;
import com.esprit.services.typeactiviteService;
import com.esprit.utils.DataSource;

public class MainProg {
    public static void main(String[] args) {
        EventService ps = new EventService();
        typeactiviteService ts = new typeactiviteService();
        activiteEventService ac= new activiteEventService();

    ac.ajouter(new activiteevent("11/2/2024",23,1));
        ac.modifier(new activiteevent("14:30",5,1));
        ac.supprimer(new activiteevent("11/2/2024",23,1));
        ts.ajouter(new typeactivite("compet","hi we are playing",1));
        ts.modifier(new typeactivite("cop","gtt",3));
    //ps.ajouter(new Event("11/2/2024", 23,"MREZGA","14H30"));
        ps.modifier(new Event(1,"hammamet", 25,"salah","14:30"));
        //ps.supprimer(new Event(2,"11/2/2024", 23, "MREZGA","14:30"));

        System.out.println(ps.rechercher());
        System.out.println(ac.rechercher());
        System.out.println(ts.rechercher());
    }
}
