<%@ page import="models.Evenement" %>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%
    // Récupération de l'objet Evenement transmis par le servlet
    Evenement event = (Evenement) request.getAttribute("event");
    if(event == null) {
        System.out.println("Aucun événement trouvé.");
        return;
    }
%>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title><%= event.getArtist() %> - Détails de l'événement</title>
    <!-- Balises Open Graph pour le partage -->
    <meta property="og:title" content="<%= event.getArtist() %> - Détails de l'événement" />
    <meta property="og:description" content="<%= event.getDescription() %>" />
    <meta property="og:url" content="http://myevents.com/event?id=<%= event.getIdEvenement() %>" />
    <meta property="og:image" content="http://myevents.com/eventImage?id=<%= event.getIdEvenement() %>" />
    <meta name="twitter:card" content="summary_large_image">
</head>
<body>
<h1><%= event.getArtist() %></h1>
<p><strong>Description :</strong> <%= event.getDescription() %></p>
<p><strong>Lieu :</strong> <%= event.getLocation() %></p>
<p><strong>Prix :</strong> $<%= event.getPrice() %></p>
<img src="http://myevents.com/eventImage?id=<%= event.getIdEvenement() %>" alt="Image de l'événement" style="max-width:600px;"/>
<!-- Vous pouvez ajouter d'autres informations ici -->
</body>
</html>
