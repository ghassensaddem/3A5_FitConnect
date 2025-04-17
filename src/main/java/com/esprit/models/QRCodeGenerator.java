package com.esprit.models;

import com.google.zxing.*;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.font.PdfFont;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class QRCodeGenerator {

    // Méthode pour générer un QR Code avec un logo et des couleurs personnalisées
    public static void generateQRCodeWithTemplate(String text, String filePath, int width, int height, Color onColor, Color offColor, String logoPath, String pdfFilePath) throws WriterException, IOException {
        // Créer une URL personnalisée ou un texte à encoder
        String customURL = "https://your-custom-url.com/?data=" + text;

        // Créer un objet BitMatrix avec le texte (ou URL) à encoder
        BitMatrix bitMatrix = new MultiFormatWriter().encode(customURL, BarcodeFormat.QR_CODE, width, height);

        // Convertir le BitMatrix en image BufferedImage avec des couleurs personnalisées
        BufferedImage qrImage = toBufferedImage(bitMatrix, onColor, offColor);

        // Ajouter un logo au centre du QR Code
        if (logoPath != null && !logoPath.isEmpty()) {
            qrImage = addLogoToQRCode(qrImage, logoPath);
        }

        // Sauvegarder l'image résultante dans un fichier
        File outputFile = new File(filePath);
        ImageIO.write(qrImage, "PNG", outputFile);

        // Générer un PDF avec le texte du QR Code
        generatePDF(customURL, pdfFilePath);
    }

    // Méthode pour convertir le BitMatrix en BufferedImage avec des couleurs personnalisées
    private static BufferedImage toBufferedImage(BitMatrix matrix, Color onColor, Color offColor) {
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                image.setRGB(x, y, matrix.get(x, y) ? onColor.getRGB() : offColor.getRGB());
            }
        }
        return image;
    }

    // Méthode pour ajouter un logo au centre du QR Code
    private static BufferedImage addLogoToQRCode(BufferedImage qrImage, String logoPath) throws IOException {
        // Charger le logo
        BufferedImage logo = ImageIO.read(new File(logoPath));

        // Calculer la position pour centrer le logo
        int qrWidth = qrImage.getWidth();
        int qrHeight = qrImage.getHeight();
        int logoWidth = logo.getWidth();
        int logoHeight = logo.getHeight();
        int x = (qrWidth - logoWidth) / 2;
        int y = (qrHeight - logoHeight) / 2;

        // Dessiner le logo sur l'image QR Code
        Graphics2D g = qrImage.createGraphics();
        g.drawImage(logo, x, y, null);
        g.dispose();

        return qrImage;
    }

    // Méthode pour générer un PDF avec le contenu du QR Code
    public static void generatePDF(String qrContent, String pdfFilePath) {
        try {
            // Créer le PdfWriter
            PdfWriter writer = new PdfWriter(pdfFilePath);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            // Utiliser une police de base (si Helvetica ne fonctionne pas, utilisez une autre police)
            PdfFont font = PdfFontFactory.createFont("Helvetica,winansi");

            // Ajouter le contenu du QR Code dans le PDF
            document.add(new Paragraph("Contenu du QR Code : " + qrContent).setFont(font));

            // Fermer le document PDF
            document.close();

            System.out.println("PDF généré avec succès : " + pdfFilePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            // Exemple d'appel de la méthode pour générer un QR code et un PDF
            generateQRCodeWithTemplate(
                    "https://exemple.com", // Texte à encoder dans le QR Code
                    "qrcode_with_logo.png", // Chemin de sauvegarde du fichier image
                    300, 300, // Taille du QR Code
                    Color.RED, // Couleur des modules (QR Code)
                    Color.WHITE, // Couleur de fond
                    "path/to/logo.png", // Chemin du logo à ajouter (facultatif)
                    "QRCodeContent.pdf"  // Chemin du fichier PDF de sortie
            );
            System.out.println("QR Code personnalisé généré avec succès !");
        } catch (WriterException | IOException e) {
            e.printStackTrace();
        }
    }
}
