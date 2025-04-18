package com.esprit.controllers;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import com.esprit.models.PlanningActivity;
import com.esprit.services.PlanningService;

public class ExportCSV {
    private final PlanningService planningService = new PlanningService();

    public void exporterCSV(String filePath) {
        List<PlanningActivity> plannings = planningService.rechercher();

        try (FileWriter writer = new FileWriter(filePath)) {
            // Écrire l'en-tête
            writer.append("ID Activity,ID Salle,Capacité Max,Nombre Inscrits\n");

            for (PlanningActivity planning : plannings) {
                String idActivityLink = "=HYPERLINK(\"detailsActivity://" + planning.getIdActivity() + "\", \"" + planning.getIdActivity() + "\")";
                String idSalleLink = "=HYPERLINK(\"detailsSalle://" + planning.getIdSalle() + "\", \"" + planning.getIdSalle() + "\")";

                writer.append(idActivityLink).append(",")
                        .append(idSalleLink).append(",")
                        .append(String.valueOf(planning.getCapacityMax())).append(",")
                        .append(String.valueOf(planning.getNombreInscription())).append("\n");
            }
            System.out.println("Exportation CSV réussie : " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
