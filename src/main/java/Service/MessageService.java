package Service;

import DAO.MessageDAO;
import Model.Message;

import java.util.List;
import java.util.Optional;

public class MessageService {
    private final MessageDAO messageDAO;

    public MessageService(MessageDAO messageDAO) {
        this.messageDAO = messageDAO;
    }

    public Optional<Message> createMessage(Message message) {
        // Check if the message text is empty or too long
        if (message.getMessage_text().trim().isEmpty() || message.getMessage_text().length() > 255) {
            return Optional.empty();
        }

        // Check if user exists before inserting message
        if (!messageDAO.userExists(message.getPosted_by())) {
            return Optional.empty();
        }

        return messageDAO.insertMessage(message);
    }


    public List<Message> getAllMessages() {
        return messageDAO.getAllMessages();
    }

    public Optional<Message> getMessageById(int messageId) {
        return messageDAO.getMessageById(messageId);
    }


    public List<Message> getMessagesByUserId(int userId) {
        return messageDAO.getMessagesByUserId(userId);
    }
    

    public boolean deleteMessage(int messageId) {
        return messageDAO.deleteMessage(messageId);
    }

    public Optional<Message> updateMessage(int messageId, String newText) {
        Optional<Message> existingMessage = messageDAO.getMessageById(messageId);
        if (existingMessage.isPresent()) {
            return messageDAO.updateMessage(messageId, newText);
        }
        return Optional.empty();
    }

}
