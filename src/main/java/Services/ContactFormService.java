package Services;

import Interfaces.IContactFormService;
import jakarta.mail.*;
import jakarta.mail.internet.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.regex.Pattern;

//Service class responsible for sending contact form messages via email using SMTP.
public class ContactFormService implements IContactFormService {

    // Regex pattern to validate email addresses
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");

    // Properties object for email configuration
    private final Properties mailProperties = new Properties();

    // SMTP config loaded from config.properties
    private final String smtpHost;
    private final String smtpPort;
    private final String smtpUsername;
    private final String smtpPassword;
    private final String toEmail;

    // Constructor: Loads mail configuration from config.properties in classpath (src/main/resources).
    public ContactFormService() {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            if (is == null) {
                throw new RuntimeException("config.properties not found in classpath");
            }
            mailProperties.load(is);
        } catch (IOException e) {
            throw new RuntimeException("Could not load email configuration from config.properties", e);
        }

        this.smtpHost = mailProperties.getProperty("mail.smtp.host");
        this.smtpPort = mailProperties.getProperty("mail.smtp.port");
        this.smtpUsername = mailProperties.getProperty("mail.smtp.username");
        this.smtpPassword = mailProperties.getProperty("mail.smtp.password");
        this.toEmail = mailProperties.getProperty("mail.to.email");

        if (toEmail == null || toEmail.isEmpty()) {
            throw new RuntimeException("Recipient email (mail.to.email) not configured.");
        }
    }
    
    // Sends a contact message to the configured recipient email.
    @Override
    public boolean sendContactMessage(String name, String email, String message) {
        if (email == null || !EMAIL_PATTERN.matcher(email).matches()) {
            throw new IllegalArgumentException("Invalid email format!");
        }

        // Set SMTP session properties
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", smtpHost);
        props.put("mail.smtp.port", smtpPort);

        // Create authenticated session
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(smtpUsername, smtpPassword);
            }
        });

        try {
            // Create the message
            MimeMessage mimeMessage = new MimeMessage(session);
            mimeMessage.setFrom(new InternetAddress(smtpUsername));
            mimeMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            mimeMessage.setSubject("New Contact Form Submission");

            mimeMessage.setText("From: " + name +
                    "\nEmail: " + email +
                    "\n\nMessage:\n" + message);

            // Send email
            Transport.send(mimeMessage);
            return true;

        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
    }
}
