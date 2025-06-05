package Interfaces;


public interface IContactFormService {
    // Sends a contact message to the configured email recipient.
    boolean sendContactMessage(String name, String email, String message);
}
