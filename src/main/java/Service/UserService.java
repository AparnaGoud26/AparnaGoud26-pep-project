package Service;

import java.util.Optional;

import DAO.UserDAO;
import Model.Account;

public class UserService {
    private final UserDAO userDAO;

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public Optional<Account> registerUser(Account user) {
        if (user.getUsername() == null || user.getUsername().isBlank() ||
            user.getPassword() == null || user.getPassword().length() < 4) {
            return Optional.empty();
        }

        Optional<Account> existingUser = userDAO.getUserByUsername(user.getUsername());
        if (existingUser.isPresent()) {
            return Optional.empty();
        }

        return userDAO.createUser(user);
    }

    public Optional<Account> authenticate(String username, String password) {
        Optional<Account> user = userDAO.getUserByUsername(username);
        
        if (user.isPresent() && user.get().getPassword().equals(password)) {
            return user;
        }
        return Optional.empty();
    }

    public Optional<Account> getUserById(int userId) {
        return userDAO.getUserById(userId);
    }
    
}
