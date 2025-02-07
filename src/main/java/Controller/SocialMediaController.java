package Controller;

import java.util.List;
import java.util.Optional;

import Service.UserService;
import Controller.SocialMediaController;
import DAO.MessageDAO;
import DAO.UserDAO;
import Model.Account;
import Model.Message;
import Service.MessageService;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */


public class SocialMediaController {
    private final UserService userService;
    private final MessageService messageService;


    public SocialMediaController() {
        this.userService = new UserService(new UserDAO());
        this.messageService = new MessageService(new MessageDAO());
    }
    
    
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/register", this::registerUser);
        app.post("/login", this::loginUser);
        app.post("/messages", this::createMessage);
        app.get("/messages", this::getAllMessages);
        app.get("/messages/{id}", this::getMessageById);
        app.get("/accounts/{id}/messages", this::getMessagesByUserId);
        app.delete("/messages/{id}", this::deleteMessage);
        app.patch("/messages/{id}", this::updateMessage);

        return app;

    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    
     private void registerUser(Context ctx) {
        Account user = ctx.bodyAsClass(Account.class);
        Optional<Account> createdUser = userService.registerUser(user);
    
        if (createdUser.isPresent()) {
            ctx.status(200).json(createdUser.get()); 
        } else {
            ctx.status(400).result("");
        }
    }
    
    


    private void loginUser(Context ctx) {
        Account user = ctx.bodyAsClass(Account.class);
        Optional<Account> authenticatedUser = userService.authenticate(user.getUsername(), user.getPassword());
    
        if (authenticatedUser.isPresent()) {
            ctx.status(200).json(authenticatedUser.get()); 
        } else {
            ctx.status(401).result("");
        }
    }
    


    private void createMessage(Context ctx) {
        Message message = ctx.bodyAsClass(Message.class);
    
        // Validate message text
        if (message.getMessage_text() == null || message.getMessage_text().trim().isEmpty() || message.getMessage_text().length() > 255) {
            ctx.status(400).result("");
            return;
        }
    
        // Validate if the user exists
        Optional<Account> userExists = userService.getUserById(message.getPosted_by());
        if (userExists.isEmpty()) {
            ctx.status(400).result("");
            return;
        }
    
        // Create the message
        Optional<Message> createdMessage = messageService.createMessage(message);
        if (createdMessage.isPresent()) {
            ctx.status(200).json(createdMessage.get());
        } else {
            ctx.status(400).result("");
        }
    }
   
   
    private void getAllMessages(Context ctx) {
        List<Message> messages = messageService.getAllMessages();
        ctx.status(200).json(messages);
    }

    private void getMessageById(Context ctx) {
        int messageId = Integer.parseInt(ctx.pathParam("id"));
        Optional<Message> message = messageService.getMessageById(messageId);
    
        if (message.isPresent()) {
            ctx.status(200).json(message.get());
        } else {
            ctx.status(200).result(""); 
        }
    }

    private void getMessagesByUserId(Context ctx) {
        int userId = Integer.parseInt(ctx.pathParam("id"));
        List<Message> messages = messageService.getMessagesByUserId(userId);
        ctx.status(200).json(messages);
    }
    

    private void deleteMessage(Context ctx) {
        int messageId = Integer.parseInt(ctx.pathParam("id"));
        Optional<Message> message = messageService.getMessageById(messageId);
    
        if (message.isPresent()) {
            messageService.deleteMessage(messageId);
            ctx.status(200).json(message.get());
        } else {
            ctx.status(200).result("");
        }
    }
    

    private void updateMessage(Context ctx) {
        int messageId = Integer.parseInt(ctx.pathParam("id"));
        Message updatedMessage = ctx.bodyAsClass(Message.class);
    
        // Validate that new message text is not empty and within the limit
        if (updatedMessage.getMessage_text() == null || updatedMessage.getMessage_text().trim().isEmpty() || updatedMessage.getMessage_text().length() > 255) {
            ctx.status(400).result("");
            return;
        }
    
        Optional<Message> updated = messageService.updateMessage(messageId, updatedMessage.getMessage_text());
    
        if (updated.isPresent()) {
            ctx.status(200).json(updated.get());
        } else {
            ctx.status(400).result("");
        }
    }
    
}




