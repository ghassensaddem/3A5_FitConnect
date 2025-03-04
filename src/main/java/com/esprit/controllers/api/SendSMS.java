package com.esprit.controllers.api;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import java.time.ZonedDateTime;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

public class SendSMS {

  /*  private static final String ACCOUNT_SID = "";
    private static final String AUTH_TOKEN = "";
    private static final PhoneNumber FROM = new PhoneNumber("whatsapp:+14155238886");

    static {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
    }

    public static String sendWhatsAppMessage(String to, String body) {
        try {
            PhoneNumber toNumber = new PhoneNumber("whatsapp:" + to);
            Message message = Message.creator(toNumber, FROM, body).create();
            return "Message envoyé avec SID: " + message.getSid();
        } catch (Exception e) {
            return "Erreur lors de l'envoi du message: " + e.getMessage();
        }
    }*/
}


