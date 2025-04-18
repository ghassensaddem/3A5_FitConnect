package com.esprit.services;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Call;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Set;

public class TwilioService {
    // 🔹 Identifiants Twilio
    private static final String ACCOUNT_SID = "AC52e67e29bbf0177e70f1c39be7385727";
    private static final String AUTH_TOKEN = "45a98aa11442cba4bd0c53d8b43d545e";
    private static final String TWILIO_PHONE_NUMBER = "+14122238451";

    // 🔹 Liste des numéros vérifiés
    private static final Set<String> VERIFIED_NUMBERS = new HashSet<>();

    static {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

        // Ajouter les numéros vérifiés
        VERIFIED_NUMBERS.add("+21648048587"); // Numéro vérifié
    }

    public static void envoyerSms(String numeroDestinataire, String message) {
        try {
            if (!VERIFIED_NUMBERS.contains(numeroDestinataire)) {
                System.out.println("❌ Le numéro " + numeroDestinataire + " n'est pas vérifié.");
                return;
            }

            Message.creator(
                    new PhoneNumber(numeroDestinataire), // 📲 Numéro du destinataire
                    new PhoneNumber(TWILIO_PHONE_NUMBER), // 📞 Numéro Twilio
                    message // 📝 Contenu du message
            ).create();

            System.out.println("📩 SMS envoyé à " + numeroDestinataire);
        } catch (Exception e) {
            System.out.println("❌ Erreur lors de l'envoi du SMS : " + e.getMessage());
        }
    }

    public static void envoyerAppel(String numeroDestinataire) {
        try {
            if (!VERIFIED_NUMBERS.contains(numeroDestinataire)) {
                System.out.println("❌ Le numéro " + numeroDestinataire + " n'est pas vérifié.");
                return;
            }

            Call call = Call.creator(
                    new PhoneNumber(numeroDestinataire),
                    new PhoneNumber(TWILIO_PHONE_NUMBER),
                    new URI("https://demo.twilio.com/docs/voice.xml") // ✅ URL TwiML valide
            ).create();

            System.out.println("📞 Appel lancé vers " + numeroDestinataire + " - SID: " + call.getSid());
        } catch (Exception e) {
            System.out.println("❌ Erreur lors de l'initiation de l'appel : " + e.getMessage());
        }
    }
}