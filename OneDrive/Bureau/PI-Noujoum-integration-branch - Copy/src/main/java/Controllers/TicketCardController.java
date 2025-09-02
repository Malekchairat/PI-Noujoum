package Controllers;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import models.Evenement;
import models.Ticket;
import services.TicketService;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import java.io.FileOutputStream;
import java.util.Base64;

import java.io.*;
import java.util.Base64;

public class TicketCardController {

    @FXML
    private Label eventNameLabel;
    @FXML
    private Label priceLabel;
    @FXML
    private Label quantityLabel;
    @FXML
    private Label artistLabel;
    @FXML
    private ImageView qrCodeImageView;
    @FXML
    private Button downloadButton;

    private Ticket ticket;
    private TicketService ticketService = new TicketService();

    public void setTicketData(Ticket ticket) {
        if (ticket == null) {
            System.out.println("⚠️ Ticket null, impossible d'afficher les données !");
            return;
        }

        this.ticket = ticket;
        System.out.println("📢 setTicketData appelé pour le ticket: " + ticket.getIdTicket());

        Evenement event = ticketService.getEvenementById(ticket.getIdEvenement());
        if (event != null) {
            eventNameLabel.setText(event.getDescription());
            artistLabel.setText("Artiste : " + event.getArtist());
        } else {
            eventNameLabel.setText("Événement inconnu");
            artistLabel.setText("Artiste inconnu");
        }

        priceLabel.setText("Prix : " + ticket.getPrix() + " TND");
        quantityLabel.setText("Quantité : " + ticket.getQuantite());

        if (ticket.getQrCode() != null && !ticket.getQrCode().isEmpty()) {
            try {
                byte[] imageBytes = Base64.getDecoder().decode(ticket.getQrCode());
                Image qrImage = new Image(new ByteArrayInputStream(imageBytes));
                qrCodeImageView.setImage(qrImage);
                System.out.println("✅ QR Code chargé !");
            } catch (Exception e) {
                System.out.println("❌ Erreur lors du chargement du QR Code : " + e.getMessage());
            }
        } else {
            System.out.println("⚠️ Aucun QR Code disponible !");
        }
    }

    @FXML
    public void downloadTicket() {
        if (ticket == null) {
            System.out.println("❌ Erreur : Aucun ticket à télécharger !");
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Enregistrer le ticket");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        fileChooser.setInitialFileName("Ticket_" + ticket.getIdTicket() + ".pdf");

        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            generatePDF(file.getAbsolutePath());
        }
    }

    private void generatePDF(String filePath) {
        Document document = new Document(PageSize.A5); // More compact size

        try {
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            // Custom Font
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.BLACK);
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, BaseColor.DARK_GRAY);
            Font contentFont = FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK);

            // Add Title
            Paragraph title = new Paragraph("Ticket Information\n\n", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            // Fetch Event Info
            Evenement event = ticketService.getEvenementById(ticket.getIdEvenement());

            // Create Table for Ticket Info
            PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10);
            table.setWidths(new float[]{3, 5}); // Adjust column sizes

            table.addCell(createCell("Événement :", headerFont));
            table.addCell(createCell(event != null ? event.getDescription() : "Non spécifié", contentFont));

            table.addCell(createCell("Artiste :", headerFont));
            table.addCell(createCell(event != null ? event.getArtist() : "Non spécifié", contentFont));

            table.addCell(createCell("Prix :", headerFont));
            table.addCell(createCell(ticket.getPrix() + " TND", contentFont));

            table.addCell(createCell("Quantité :", headerFont));
            table.addCell(createCell(String.valueOf(ticket.getQuantite()), contentFont));

            document.add(table);

            // QR Code
            if (ticket.getQrCode() != null && !ticket.getQrCode().isEmpty()) {
                try {
                    byte[] qrBytes = Base64.getDecoder().decode(ticket.getQrCode());
                    com.itextpdf.text.Image qrImage = com.itextpdf.text.Image.getInstance(qrBytes);
                    qrImage.scaleToFit(120, 120);
                    qrImage.setAlignment(Element.ALIGN_CENTER);

                    document.add(new Paragraph("\n", contentFont)); // Add spacing
                    document.add(qrImage);
                } catch (Exception e) {
                    document.add(new Paragraph("⚠ Erreur lors du chargement du QR Code", contentFont));
                    System.out.println("❌ Erreur lors de l'ajout du QR Code au PDF : " + e.getMessage());
                }
            } else {
                document.add(new Paragraph("\n⚠ Aucun QR Code disponible\n", contentFont));
            }

            // Footer
            Paragraph footer = new Paragraph("\nMerci d'avoir acheté votre billet !", headerFont);
            footer.setAlignment(Element.ALIGN_CENTER);
            document.add(footer);

            document.close();
            writer.close();

            System.out.println("✅ PDF créé avec succès : " + filePath);

        } catch (Exception e) {
            System.out.println("❌ Erreur lors de la création du PDF : " + e.getMessage());
        }
    }

    private PdfPCell createCell(String text, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setPadding(5);
        cell.setBorder(Rectangle.NO_BORDER);
        return cell;
    }
}