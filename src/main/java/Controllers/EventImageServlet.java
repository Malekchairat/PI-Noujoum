package Controllers;

import models.Evenement;
import services.EvenementService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.*;
import java.sql.SQLException;

@WebServlet("/eventImage")
public class EventImageServlet extends HttpServlet {
    private EvenementService evenementService = new EvenementService();


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idParam = request.getParameter("id_evenement");
        if (idParam == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Identifiant de l'événement manquant.");
            return;
        }
        try {
            int eventId = Integer.parseInt(idParam);
            Evenement event = evenementService.recupererParId(eventId);
            if (event == null || event.getImageE() == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Image non trouvée.");
                return;
            }
            response.setContentType("image/jpeg");
            InputStream is = event.getImageE().getBinaryStream();
            OutputStream os = response.getOutputStream();
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            is.close();
            os.close();
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Identifiant de l'événement invalide.");
        } catch (SQLException e) {
            // On renvoie une ServletException avec la cause
            throw new ServletException("Erreur lors de la lecture de l'image depuis la base de données", e);
        }
    }
}
