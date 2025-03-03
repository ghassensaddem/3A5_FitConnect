package com.esprit.models;

import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.border.Border;
import com.itextpdf.layout.border.SolidBorder;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.esprit.models.ArticleCommande;
import com.esprit.models.Commande;
import com.esprit.services.CommandeService;

import java.io.FileNotFoundException;
import java.net.URL;
import java.util.List;

public class PdfGenerator {
    public static void generatePdf(int clientId) throws FileNotFoundException {
        String path = "commande.pdf";
        PdfWriter pdfWriter = new PdfWriter(path);
        PdfDocument pdfDocument = new PdfDocument(pdfWriter);
        pdfDocument.setDefaultPageSize(PageSize.A4.rotate());
        Document document = new Document(pdfDocument);
        document.setMargins(20, 20, 20, 20);

        float threecol = 190f;
        float twocol = 285f;
        float[] fullwidth = {threecol * 3};
        float[] fivecolumnwidth = {80f, 200f, 100f, 80f, 100f};

        CommandeService commandeService = new CommandeService();
        List<Commande> commandes = commandeService.getCommandesByClientIdAndEtat(clientId, "En commande");

        if (!commandes.isEmpty()) {
            Commande premiereCommande = commandes.get(0);
            String dateCommande = premiereCommande.getDateCreation().toString();
            String clientEmail = commandeService.getClientEmailById(premiereCommande.getClientId());
            String dateLivraison = premiereCommande.getDateCreation().plusDays(2).toString();

            document.add(createHeaderTable(twocol, dateCommande));
            document.add(createDivider(fullwidth, new SolidBorder(Color.GRAY, 2f)));
            document.add(createClientInfoTable(twocol, clientEmail, dateLivraison));
            document.add(createDivider(fullwidth, new SolidBorder(Color.GRAY, 1f)));

            document.add(new Paragraph("Produits").setBold().setFontSize(14f));
            document.add(createProductHeaderTable(fivecolumnwidth));

            double grandTotal = 0;
            for (Commande cmd : commandes) {
                Equipement equipement = cmd.getEquipement();
                if (equipement != null) {
                    double totalEquipement = equipement.getPrix() * cmd.getQuantite();
                    grandTotal += totalEquipement;

                    Table productTable = new Table(fivecolumnwidth);
                    productTable.setMarginBottom(10);
                    String imagePath = "/" + equipement.getImage(); // Chemin relatif à partir de resources
                    System.out.println("Chemin de l'image : " + imagePath);

                    try {
                        URL imageUrl = PdfGenerator.class.getResource(imagePath);
                        if (imageUrl != null) {
                            System.out.println("Image trouvée : " + imageUrl);
                            ImageData imageData = ImageDataFactory.create(imageUrl);
                            com.itextpdf.layout.element.Image img = new com.itextpdf.layout.element.Image(imageData);
                            img.setWidth(50).setHeight(50);
                            productTable.addCell(new Cell().add(img).setBorder(Border.NO_BORDER));
                        } else {
                            System.out.println("Image non trouvée : " + imagePath);
                            productTable.addCell(new Cell().add("Image non trouvée").setBorder(Border.NO_BORDER));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println("Erreur de chargement de l'image : " + imagePath);
                        productTable.addCell(new Cell().add("Erreur de chargement de l'image").setBorder(Border.NO_BORDER));
                    }

                    // Description
                    productTable.addCell(getCell(equipement.getDescription(), false));

                    // Prix
                    productTable.addCell(getCell(String.valueOf(equipement.getPrix()), false));

                    // Quantité
                    productTable.addCell(getCell(String.valueOf(cmd.getQuantite()), false));

                    // Total
                    productTable.addCell(getCell(String.valueOf(totalEquipement), false));

                    document.add(productTable);
                }
            }

            document.add(createTotalTable(fullwidth, grandTotal));
            document.close();
        }
    }

    private static Table createHeaderTable(float twocol, String dateCommande) {
        Table table = new Table(new float[]{twocol, twocol + 150f});
        table.addCell(new Cell().add("Commande").setFontSize(15f).setBorder(Border.NO_BORDER).setBold());
        Table nestedTable = new Table(new float[]{twocol / 2, twocol / 2});
        nestedTable.addCell(new Cell().add("Date Commande").setBold().setBorder(Border.NO_BORDER));
        nestedTable.addCell(new Cell().add(dateCommande).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(nestedTable).setBorder(Border.NO_BORDER));
        return table;
    }

    private static Table createDivider(float[] fullwidth, Border border) {
        Table divider = new Table(fullwidth);
        divider.setBorder(border);
        return divider;
    }

    private static Table createClientInfoTable(float twocol, String clientEmail, String dateLivraison) {
        Table infoTable = new Table(new float[]{twocol / 2, twocol / 2});
        infoTable.addCell(getCell("Client Email", true));
        infoTable.addCell(getCell(clientEmail, false));
        infoTable.addCell(getCell("Date de Livraison", true));
        infoTable.addCell(getCell(dateLivraison, false));
        return infoTable;
    }

    private static Table createProductHeaderTable(float[] fivecolumnwidth) {
        Table fiveColTable = new Table(fivecolumnwidth);
        fiveColTable.addCell(getHeaderCell("Produit"));
        fiveColTable.addCell(getHeaderCell("Description"));
        fiveColTable.addCell(getHeaderCell("Prix"));
        fiveColTable.addCell(getHeaderCell("Quantité"));
        fiveColTable.addCell(getHeaderCell("Total"));
        return fiveColTable;
    }

    private static Table createTotalTable(float[] fullwidth, double total) {
        Table totalTable = new Table(new float[]{fullwidth[0] - 100, 100});
        totalTable.addCell(new Cell().add("Total").setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER).setBold());
        totalTable.addCell(new Cell().add(String.format("%.2f Dt", total)).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER).setBold());
        return totalTable;
    }

    private static Cell getCell(String text, boolean isBold) {
        Cell cell = new Cell().add(text).setFontSize(10f).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT);
        return isBold ? cell.setBold() : cell;
    }

    private static Cell getHeaderCell(String text) {
        return new Cell().add(text).setBold().setFontColor(Color.WHITE).setBackgroundColor(Color.GRAY).setBorder(Border.NO_BORDER);
    }
}