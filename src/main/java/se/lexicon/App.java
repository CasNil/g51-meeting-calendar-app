package se.lexicon;

import se.lexicon.dao.UserDAO;
import se.lexicon.dao.db.CalendarDBConnection;
import se.lexicon.dao.implementation.UserDAOImpl;
import se.lexicon.exception.CalendarExceptionHandler;
import se.lexicon.model.User;
import se.lexicon.util.ConsoleColors;

import java.util.Optional;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        UserDAO userDAO = new UserDAOImpl(CalendarDBConnection.getConnection());
//        User user = userDAO.createUser("admin");
//        System.out.println("user.userInfo() = " + user.userInfo());
//
//        Optional<User> userOptional = userDAO.findByUsername("admin");
//        if (userOptional.isPresent()) {
//            System.out.println(userOptional.get().userInfo());
//        }
        try {
            userDAO.authenticate(new User("admin", "NTTlJIHr8s"));
            System.out.println(ConsoleColors.GREEN + "You are logged in ..." + ConsoleColors.RESET);
        } catch (Exception e) {
            CalendarExceptionHandler.handleException(e);
        }
    }
}
