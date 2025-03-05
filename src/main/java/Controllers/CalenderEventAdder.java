package Controllers;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import models.Evenement;
import services.EvenementService;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class CalenderEventAdder {
    private static final EvenementService serviceEvenement = new EvenementService();

    /**
     * Ajoute un événement à Google Calendar et stocke l'ID Google dans la base de données.
     */
    public static void addEvent(Evenement evt) throws IOException, GeneralSecurityException {
        Calendar service = GoogleCalendarService.getCalendarService();

        String summary = evt.getArtist() + " - " + evt.getLocation();
        String description = evt.getDescription();

        Event event = new Event()
                .setSummary(summary)
                .setDescription(description);

        String startDateStr = evt.getStartDate().toString() + "T10:00:00";
        String endDateStr = evt.getEndDate().toString() + "T12:00:00";

        event.setStart(new EventDateTime().setDateTime(new DateTime(startDateStr)));
        event.setEnd(new EventDateTime().setDateTime(new DateTime(endDateStr)));

        // Ajouter l'événement à Google Calendar
        event = service.events().insert("primary", event).execute();

        // Récupérer l'ID de Google Calendar et le stocker dans la base de données
        String googleEventId = event.getId();
        evt.setGoogleCalendarId(googleEventId);
        serviceEvenement.mettreAJourGoogleId(evt.getIdEvenement(), googleEventId);

        System.out.println("✅ Événement ajouté avec succès : " + event.getHtmlLink());
    }

    /**
     * Met à jour un événement existant sur Google Calendar.
     */
    public static void updateEvent(Evenement event) throws IOException, GeneralSecurityException {
        Calendar service = GoogleCalendarService.getCalendarService();

        if (event.getGoogleCalendarId() == null || event.getGoogleCalendarId().isEmpty()) {
            System.out.println("❌ Impossible de mettre à jour : aucun ID Google Calendar trouvé.");
            return;
        }

        Event googleEvent = service.events().get("primary", event.getGoogleCalendarId()).execute();
        googleEvent.setSummary(event.getArtist() + " - " + event.getLocation());
        googleEvent.setDescription(event.getDescription());
        googleEvent.setStart(new EventDateTime().setDateTime(new DateTime(event.getStartDate().toString() + "T10:00:00")));
        googleEvent.setEnd(new EventDateTime().setDateTime(new DateTime(event.getEndDate().toString() + "T12:00:00")));

        service.events().update("primary", googleEvent.getId(), googleEvent).execute();
        System.out.println("✅ Événement mis à jour avec succès !");
    }

    /**
     * Supprime un événement de Google Calendar.
     */
    public static void deleteEvent(Evenement event) {
        if (event == null) {
            System.out.println("Erreur: L'événement est null, suppression annulée.");
            return;
        }

        String googleId = event.getGoogleCalendarId();
        if (googleId == null) {
            System.out.println("Erreur: L'ID de l'événement Google Calendar est null, suppression annulée.");
            return;
        }

        try {
            Calendar service = GoogleCalendarService.getCalendarService();
            service.events().delete("primary", googleId).execute();
            System.out.println("Événement supprimé avec succès !");
        } catch (Exception e) {
            System.out.println("Erreur lors de la suppression de l'événement Google Calendar: " + e.getMessage());
        }
    }

}
