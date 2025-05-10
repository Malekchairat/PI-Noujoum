package Controllers;

import models.Evenement;
import services.EvenementService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/event")
public class EventDetailsServlet extends HttpServlet {
    private EvenementService evenementService = new EvenementService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Récupérer l'identifiant de l'événement depuis l'URL (?id=...)
        String idParam = request.getParameter("id_evenement");
        if (idParam == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Identifiant de l'événement manquant.");
            return;
        }
        try {
            int eventId = Integer.parseInt(idParam);
            Evenement event = evenementService.recupererParId(eventId);
            if (event == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Événement non trouvé.");
                return;
            }
            // Ajouter l'événement aux attributs de la requête pour la JSP
            request.setAttribute("event", event);
            // Transférer la requête vers eventDetails.jsp
            request.getRequestDispatcher("/eventDetails.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Identifiant de l'événement invalide.");
        }
    }
}
