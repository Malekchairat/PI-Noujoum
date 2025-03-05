package controller;

import com.google.gson.JsonObject;
import com.itextpdf.layout.element.Paragraph;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.*;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.WritableImage;
import models.Reclamation;
import services.ReclamationService;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.layout.Document;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Image;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import tools.GeminiAPI;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Properties;

public class StatsAdminController {

    @FXML
    private BarChart<String, Number> bar;

    @FXML
    private BubbleChart<Number, Number> bubble;

    @FXML
    private Button pdfBtn;

    @FXML
    private PieChart pie;

    @FXML
    private StackedBarChart<String, Number> stacked;


    private String pieData;
    private String bubbleData;
    private String barData;
    private String stackedDatapdf;


    private final ReclamationService reclamationService = new ReclamationService();

    public void initialize() {
        populateCharts();
    }

    @FXML
    public void populateCharts() {
        List<Reclamation> reclamations = reclamationService.recuperer();

        if (reclamations.isEmpty()) return;

        // **Pie Chart - Distribution of Complaints by Status**
        pie.getData().clear();
        reclamations.stream()
                .collect(Collectors.groupingBy(Reclamation::getStatut, Collectors.counting()))
                .forEach((statut, count) -> pie.getData().add(new PieChart.Data(statut, count)));

        // **Bar Chart - Number of Complaints by Priority**
        bar.getData().clear();
        XYChart.Series<String, Number> prioritySeries = new XYChart.Series<>();
        prioritySeries.setName("Priorité");

        reclamations.stream()
                .collect(Collectors.groupingBy(Reclamation::getPriorite, Collectors.counting()))
                .forEach((priorite, count) -> prioritySeries.getData().add(new XYChart.Data<>(priorite, count)));

        bar.getData().add(prioritySeries);
        barData = "Bar Chart Data:\n" + prioritySeries.getData().stream()
                .map(data -> data.getXValue() + ": " + data.getYValue())
                .collect(Collectors.joining("\n"));

        // **Bubble Chart - Complaints over Time (Date-based visualization)**
        bubble.getData().clear();
        XYChart.Series<Number, Number> bubbleSeries = new XYChart.Series<>();
        bubbleSeries.setName("Réclamations");

        reclamations.stream()
                .sorted(Comparator.comparing(Reclamation::getDateCreation))
                .forEach(reclamation -> {
                    double randomSize = Math.random() * 10 + 5; // Random bubble size for variation
                    bubbleSeries.getData().add(new XYChart.Data<>(reclamation.getDateCreation().getTime(), randomSize));
                });

        bubble.getData().add(bubbleSeries);
        bubbleData = "Bubble Chart Data:\n" + bubbleSeries.getData().stream()
                .map(data -> data.getXValue() + ": " + data.getYValue())
                .collect(Collectors.joining("\n"));

        // **Stacked Bar Chart - Complaints by Priority & Status**
        stacked.getData().clear();
        Map<String, Map<String, Long>> stackedData = reclamations.stream()
                .collect(Collectors.groupingBy(Reclamation::getPriorite,
                        Collectors.groupingBy(Reclamation::getStatut, Collectors.counting())));

        stackedData.forEach((priorite, statusMap) -> {
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName(priorite);

            statusMap.forEach((statut, count) -> series.getData().add(new XYChart.Data<>(statut, count)));

            stacked.getData().add(series);
        });
        stackedDatapdf = "Stacked Bar Chart Data:\n" + stackedData.entrySet().stream()
                .map(entry -> entry.getKey() + ": " + entry.getValue())
                .collect(Collectors.joining("\n"));
    }


    @FXML
    void generatePDF(ActionEvent event) {
        String filePath = "statistics.pdf";

        try {
            // Capture chart images
            String barPath = saveChartAsImage(bar, "bar_chart.png");
            String linePath = saveChartAsImage(bubble, "Bubble_chart.png");
            String piePath = saveChartAsImage(pie, "pie_chart.png");
            String stackedPath = saveChartAsImage(stacked, "stacked_chart.png");

            // Get AI recommendations
            String barRecommendations = getAIRecommendations("bar chart", barData);
            String lineRecommendations = getAIRecommendations("bubble chart", bubbleData);
            String pieRecommendations = getAIRecommendations("pie chart", pieData);
            String stackedRecommendations = getAIRecommendations("stacked chart", stackedDatapdf);

            // Create PDF
            PdfWriter writer = new PdfWriter(filePath);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            document.add(new Paragraph("Statistics Report").setBold().setFontSize(16));

            addChartToPDF(document, "Bar Chart: ", barPath, barRecommendations);
            addChartToPDF(document, "Bubble Chart: ", linePath, lineRecommendations);
            addChartToPDF(document, "Pie Chart:", piePath, pieRecommendations);
            addChartToPDF(document, "Stacked Bar Chart: ", stackedPath, stackedRecommendations);

            document.close();

            // Open the generated PDF automatically
            Desktop.getDesktop().open(new File(filePath));

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to generate PDF.");
        }
    }

    private String saveChartAsImage(Chart chart, String fileName) throws IOException {
        WritableImage image = chart.snapshot(new SnapshotParameters(), null);
        File file = new File(fileName);
        ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
        return fileName;
    }

    private void addChartToPDF(Document document, String title, String imagePath, String recommendations) throws IOException {
        document.add(new Paragraph(title).setBold().setFontSize(14));

        ImageData imageData = ImageDataFactory.create(imagePath);
        Image chartImage = new Image(imageData);
        chartImage.setAutoScale(true);
        document.add(chartImage);

        document.add(new Paragraph("Recommendations:").setBold().setFontSize(12));
        document.add(new Paragraph(recommendations));

        document.add(new Paragraph("\n"));
    }

    private String getAIRecommendations(String chartType ,String data) {
        String prompt = "Act like an expert in marketing helpping the admin of this Application to enhace the sales and make sure to care about the after sale part. Generate only 2 bullet point recommendations for a " + chartType +data +
                " representing statistics of an application where users buy from them and can do a reclamation and a feedback after getting the answer on the reclamation. " +
                "Only list the recommendations in bullet points without explanations." + "return only the two points in the response.";
        try {
            JsonObject response = GeminiAPI.getGeminiResponse(prompt);
            return extractBulletPoints(response);
        } catch (IOException e) {
            e.printStackTrace();
            return "No recommendations available.";
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void showAlertSuccess(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private String extractBulletPoints(JsonObject jsonResponse) {
        if (jsonResponse.has("candidates") &&
                jsonResponse.getAsJsonArray("candidates").size() > 0) {
            String text = jsonResponse.getAsJsonArray("candidates")
                    .get(0).getAsJsonObject()
                    .getAsJsonObject("content")
                    .getAsJsonArray("parts")
                    .get(0).getAsJsonObject()
                    .get("text").getAsString();
            return text.replace("\n", "\n• "); // Format bullet points
        }
        return "• No recommendations available.";
    }

    public void sendEmail(String recipient) {
        final String senderEmail = "mahmoudtouil9@gmail.com";
        final String senderPassword = "zzjs enoh rimz nqvp"; // Use App Password if using Gmail

        // Check if the PDF file exists before proceeding
        String filePath = "statistics.pdf";
        File file = new File(filePath);

        if (!file.exists()) {
            showAlert("PDF Not Found", "Please generate the PDF report before sending the email.");
            return;
        }

        // SMTP Server Properties
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        // Create Session
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });

        try {
            // Create Email Message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
            message.setSubject("Service Statistics Report");

            // Create the email body
            MimeBodyPart textPart = new MimeBodyPart();
            String emailContent = "Hello,\n\nPlease find attached the service statistics report.\n\nBest Regards,\n";
            textPart.setText(emailContent);

            // Attach the PDF file
            MimeBodyPart attachmentPart = new MimeBodyPart();
            attachmentPart.attachFile(file);

            // Create multipart email
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(textPart);
            multipart.addBodyPart(attachmentPart);

            // Set the content of the message
            message.setContent(multipart);

            // Send Email
            Transport.send(message);
            System.out.println("Email with PDF sent successfully to " + recipient);

            // Show success alert
            showAlertSuccess("Success", "Email sent successfully to " + recipient);

        } catch (MessagingException | IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to send email. Please try again.");
        }
    }


    @FXML
    void sendMail(ActionEvent event) {
        String recipient = "mahmoudtouil9@gmail.com";
        sendEmail(recipient);
    }

}
