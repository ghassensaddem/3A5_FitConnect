package com.esprit.utils;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.util.Properties;

public class MailSender {
    private static final String EMAIL = "rayanferjani55@gmail.com";
    private static final String PASSWORD = "lticnnrlsrgzzbqs";

    public static boolean envoyerEmail(String destinataire, String sujet, String contenu) {
        // Configuration des propriétés SMTP
        Properties prop = new Properties();
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true");
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");

        // Création d'une session avec authentification
        Session session = Session.getInstance(prop, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(EMAIL, PASSWORD);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(EMAIL));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinataire));
            message.setSubject(sujet);
            message.setText(contenu);

            Transport.send(message);
            return true; // Envoi réussi
        } catch (MessagingException e) {
            e.printStackTrace();
            return false; // Échec de l'envoi
        }
    }
}
