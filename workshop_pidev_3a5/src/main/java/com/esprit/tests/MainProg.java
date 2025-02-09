package com.esprit.tests;

import com.esprit.models.Avis;
import com.esprit.models.Programme;
import com.esprit.models.seance;
import com.esprit.services.AvisService;
import com.esprit.services.ProgrammeService;
import com.esprit.services.SeanceService;

import java.time.LocalDate;
import java.time.LocalTime;


public class MainProg {
    public static void main(String[] args) {
        ProgrammeService ps = new ProgrammeService();
        SeanceService ss = new SeanceService();
        AvisService avis = new AvisService();

        //ps.ajouter(new Programme(2, LocalDate.of(2024, 3, 2), LocalDate.of(2024, 6, 4), 3.5, "qhjc"));
        //ps.ajouter(new Programme(3, LocalDate.of(2024, 3, 2), LocalDate.of(2024, 6, 4), 9.5, "abvzf"));
       // ps.modifier(new Programme(8, LocalDate.of(2024, 3, 3), LocalDate.of(2024, 6, 4), 3.2, "qhyedgy"));
       //ps.supprimer(new Programme(9, null, null, 0.0, ""));  // Suppression : les valeurs nulles suffisent
      //  ss.ajouter(new seance(0, LocalDate.of(2024, 3, 10), LocalTime.of(14, 30), "Salle B", 8));
        // Modifier une séance (exemple: changer la date et le lieu de la séance avec id=5)
        //ss.modifier(new seance(5, LocalDate.of(2024, 3, 12), LocalTime.of(16, 0), "Salle C", 2));
        // Supprimer une séance avec id=6
        //ss.supprimer(new seance(6, null, null, "", 0));
        avis.ajouter(new Avis(0, "bien", 6, 2));
        //avis.modifier(new Avis(3, "mauvais", 7, 5));
        //avis.supprimer(new Avis(0, "mauvais", 8, 5));
        System.out.println(ps.rechercher());
    }
}
