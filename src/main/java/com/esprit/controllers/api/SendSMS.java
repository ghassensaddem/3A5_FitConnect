package com.esprit.controllers.api;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import java.time.ZonedDateTime;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

public class SendSMS {

  /*  private static final String ACCOUNT_SID = "AC6843f215e4d4a66cf94d3aebf7d5de91";
    private static final String AUTH_TOKEN = "61f687bbef3bc267136b7ce64f25e2af";
    private static final PhoneNumber FROM = new PhoneNumber("whatsapp:+14155238886");*/

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
    }
}


