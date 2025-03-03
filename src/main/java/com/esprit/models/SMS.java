package com.esprit.models;


import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;



//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class SMS {
    // Remplacez par vos identifiants Twilio
    public static final String ACCOUNT_SID = "TWILIO_ACCOUNT_SID";
    public static final String AUTH_TOKEN = "TWILIO_ACCOUNT_SID1";
    public static void sendSms(String toPhoneNumber, String messageText) {
        // Initialiser Twilio
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);


        // Votre numéro Twilio (numéro expéditeur)
        String numeroTwilio = "+18575784016"; // Votre numéro Twilio
        // Envoyer un SMS
        Message message = Message.creator(
                        new PhoneNumber(toPhoneNumber),  // Numéro de destination
                        new PhoneNumber(numeroTwilio),        // Votre numéro Twilio
                        messageText)                          // Message
                .create();

        // Afficher l'ID du message envoyé
        System.out.println("Message SID: " + message.getSid());
    }

}


