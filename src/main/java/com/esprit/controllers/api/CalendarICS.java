package com.esprit.controllers.api;
/*
import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.component.VEvent;
import java.io.InputStream;
import java.net.URL;
*/
public class CalendarICS {
    public static void main(String[] args) {
        /*try {
            // URL d'un calendrier public en format .ics
            String icsUrl = "https://www.calendarlabs.com/templates/ical/US-Holidays.ics";
            InputStream stream = new URL(icsUrl).openStream();

            // Lecture du calendrier
            CalendarBuilder builder = new CalendarBuilder();
            Calendar calendar = builder.build(stream);

            // Affichage des événements
            for (Object component : calendar.getComponents()) {
                if (component instanceof VEvent) {
                    VEvent event = (VEvent) component;
                    System.out.println("Événement : " + event.getSummary().getValue());
                    System.out.println("Date : " + event.getStartDate().getDate());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }
}
