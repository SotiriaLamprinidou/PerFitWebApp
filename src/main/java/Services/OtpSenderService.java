package Services;

import Interfaces.IOtpSenderService;
import jakarta.mail.*;
import jakarta.mail.internet.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.regex.Pattern;

/**
 * Service responsible for sending OTP codes via email.
 */
public class OtpSenderService implements IOtpSenderService {

    // Pattern to validate email address format
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");

    // Properties object to hold configuration from config.properties
    private final Properties configProperties = new Properties();

    // SMTP configuration fields loaded from config file
    private final String smtpHost;
    private final String smtpPort;
    private final String smtpUsername;
    private final String smtpPassword;

    /**
     * Constructor - loads SMTP config from config.properties located on the classpath.
     */
    public OtpSenderService() {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                throw new RuntimeException("config.properties not found in classpath");
            }
            configProperties.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load email configuration from config.properties", e);
        }

        this.smtpHost = configProperties.getProperty("mail.smtp.host");
        this.smtpPort = configProperties.getProperty("mail.smtp.port");
        this.smtpUsername = configProperties.getProperty("mail.smtp.username");
        this.smtpPassword = configProperties.getProperty("mail.smtp.password");
    }

    /**
     * Sends an OTP email to the specified address.
     *
     * @param email Recipient's email address
     * @param otp   The one-time password to send
     */
    @Override
    public void sendOtpByEmail(String email, String otp) {
        // Validate email address format
        if (email == null || !EMAIL_PATTERN.matcher(email).matches()) {
            throw new IllegalArgumentException("Invalid email format!");
        }

        // Configure mail session properties
        Properties mailProps = new Properties();
        mailProps.put("mail.smtp.host", smtpHost);
        mailProps.put("mail.smtp.port", smtpPort);
        mailProps.put("mail.smtp.auth", "true");
        mailProps.put("mail.smtp.starttls.enable", "true");

        // Create a session with SMTP authentication
        Session session = Session.getInstance(mailProps, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(smtpUsername, smtpPassword);
            }
        });

        try {
            // Compose and send the OTP email
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(smtpUsername));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
            message.setSubject("Welcome to Perfit! Your personal fitness guide!");
            message.setText("This is your OTP code with 5 minutes duration: " + otp);
            Transport.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send OTP email.", e);
        }
    }
}
