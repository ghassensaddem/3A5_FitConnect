package com.esprit.tests;

import com.esprit.models.Commande;
import com.esprit.models.Personne;
import com.esprit.services.CommandeService;
import com.esprit.services.PersonneService;
import com.esprit.utils.DataSource;

import java.time.LocalDateTime;

public class MainProg {
    public static void main(String[] args) {
        CommandeService ps = new CommandeService();

       System.out.println(ps.rechercher());
    }
}
