package com.esprit.models;
import com.esprit.utils.DataSource;
import com.esprit.services.EventService;
import com.esprit.services.activiteEventService;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.esprit.controllers.api.EventQRCodeGenerator;
import com.itextpdf.text.pdf.qrcode.WriterException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class PDF {
    public PDF() {
    }

    public void GeneratePdf(String filename, ArrayList<Event> events, EventService eventService) throws FileNotFoundException, DocumentException, WriterException {
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, new FileOutputStream(filename + ".pdf"));
        document.open();

        // Ajouter le logo
        try {
            // Remplacez "path_to_logo.png" par le chemin réel de votre logo
            Image logo = Image.getInstance(getClass().getResource("/images/logo2.png").toExternalForm());

            logo.scaleToFit(100, 100);  // Vous pouvez ajuster la taille du logo si nécessaire
            logo.setAlignment(Element.ALIGN_CENTER);  // Centrer le logo
            document.add(logo);  // Ajouter le logo au document
        } catch (IOException e) {
            System.err.println("❌ Impossible d'ajouter le logo : " + e.getMessage());
        }

        // Styles de texte
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 22, new BaseColor(5, 53, 54));
        Font subTitleFont = FontFactory.getFont(FontFactory.HELVETICA, 14, new BaseColor(64, 64, 64));
        Font tableHeaderFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.WHITE);
        Font tableFont = FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK);

        // Titre
        Paragraph title = new Paragraph("📅 Liste des Événements", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);

        // Date de génération
        Paragraph date = new Paragraph("Date de génération : " + LocalDate.now(), subTitleFont);
        date.setAlignment(Element.ALIGN_CENTER);
        date.setSpacingAfter(10);
        document.add(date);

        document.add(new Paragraph("\n")); // Ajout d'un espace

        // Tableau des événements
        PdfPTable table = new PdfPTable(7);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10);
        table.setWidths(new float[]{2, 2, 2, 1, 3, 2, 2});

        BaseColor headerColor = new BaseColor(5, 53, 54);

        String[] headers = {"Nom de l'événement", "Date", "Horaire", "Prix (TND)", "Image", "Revenu Total (TND)", "QR Code"};
        for (String header : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(header, tableHeaderFont));
            cell.setBackgroundColor(headerColor);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setPadding(8);
            table.addCell(cell);
        }

        // Ajouter les événements
        for (Event p : events) {
            table.addCell(new Phrase(p.getLieu(), tableFont));
            table.addCell(new Phrase(p.getDate().toString(), tableFont));
            table.addCell(new Phrase(p.getHoraire(), tableFont));
            table.addCell(new Phrase(p.getPrixdupass() + " TND", tableFont));

            // Ajouter l'image
            try {
                Image img = Image.getInstance(p.getImage());
                img.scaleToFit(70, 70);
                PdfPCell imgCell = new PdfPCell(img, true);
                imgCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                imgCell.setPadding(5);
                table.addCell(imgCell);
            } catch (Exception e) {
                PdfPCell noImgCell = new PdfPCell(new Phrase("⚠️ Non disponible", tableFont));
                noImgCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(noImgCell);
            }

            // Calculer le revenu total
            try {
                double revenuTotal = eventService.calculerRevenuParEvent(p.getId());
                table.addCell(new Phrase(revenuTotal + " TND", tableFont));
            } catch (Exception e) {
                table.addCell(new Phrase("⚠️ Erreur calcul", tableFont));
            }

            // Générer le QR Code
            int idEvent = p.getId();
            activiteevent activity = null;

            try {
                activity = eventService.getActiviteEvent(idEvent);  // Récupération de l'activité
            } catch (SQLException e) {
                // Gérer l'exception SQL si l'activité ne peut pas être trouvée
                PdfPCell errorCell = new PdfPCell(new Phrase("⚠️ Erreur activité", tableFont));
                errorCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(errorCell);
            }

            if (activity != null) {
                try {
                    // Le problème est probablement ici avec la génération du QR code
                    BufferedImage qrBufferedImage = EventQRCodeGenerator.generateQRCodeImage(activity);  // Générer l'image du QR code
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    ImageIO.write(qrBufferedImage, "PNG", baos);

                    // Ajouter l'image QR Code au PDF
                    Image qrPdfImage = Image.getInstance(baos.toByteArray());
                    qrPdfImage.scaleToFit(70, 70);
                    PdfPCell qrCell = new PdfPCell(qrPdfImage, true);
                    qrCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    qrCell.setPadding(5);
                    table.addCell(qrCell);
                } catch (IOException e) {
                    // Si IOException est capturée, afficher une erreur dans le tableau
                    PdfPCell errorCell = new PdfPCell(new Phrase("⚠️ Erreur QR Code IO", tableFont));
                    errorCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(errorCell);
                } catch (com.google.zxing.WriterException e) {
                    throw new RuntimeException(e);
                }
            } else {
                // Ajouter un message d'erreur dans le tableau PDF si l'activité n'est pas trouvée
                PdfPCell errorCell = new PdfPCell(new Phrase("⚠️ Activité non trouvée", tableFont));
                errorCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(errorCell);
            }
        }

        // Ajouter le tableau au document
        document.add(table);
        document.close();

        System.out.println("✅ PDF généré avec succès : " + filename + ".pdf");

        // Ouvrir automatiquement le fichier PDF (Windows uniquement)
        try {
            Process pro = Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + filename + ".pdf");
        } catch (IOException e) {
            System.err.println("❌ Impossible d'ouvrir le fichier PDF : " + e.getMessage());
        }
    }


}

