package Controllers;

import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

public class CalenderEventFetcher {

    private static ListView<String> eventListView;  // Reference to UI component

    public static void setEventListView(ListView<String> listView) {
        eventListView = listView;
    }

    public static void fetchEvents() {
        new Thread(() -> {
            try {
                Calendar service = GoogleCalendarService.getCalendarService();

                // Fetch events from Google Calendar
                Events events = service.events().list("primary")
                        .setMaxResults(10)
                        .setOrderBy("startTime")
                        .setSingleEvents(true)
                        .execute();

                List<Event> items = events.getItems();
                ObservableList<String> eventList = FXCollections.observableArrayList();

                if (items.isEmpty()) {
                    eventList.add("No upcoming events found.");
                } else {
                    for (Event event : items) {
                        String summary = event.getSummary();
                        String startTime = (event.getStart().getDateTime() != null) ?
                                event.getStart().getDateTime().toString() :
                                event.getStart().getDate().toString();
                        eventList.add(summary + " (" + startTime + ")");
                    }
                }

                // Update the UI
                Platform.runLater(() -> {
                    if (eventListView != null) {
                        eventListView.setItems(eventList);
                    }
                });

            } catch (IOException | GeneralSecurityException e) {
                System.out.println("Error fetching events: " + e.getMessage());
            }
        }).start();
    }

    public static void deleteEvent(String eventId) {
        new Thread(() -> {
            try {
                Calendar service = GoogleCalendarService.getCalendarService();
                service.events().delete("primary", eventId).execute();
                System.out.println("Event deleted successfully!");

                // Refresh the event list after deletion
                fetchEvents();
            } catch (Exception e) {
                System.out.println("Error deleting event: " + e.getMessage());
            }
        }).start();
    }
}
