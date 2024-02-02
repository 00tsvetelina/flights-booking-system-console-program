package org.example.service;

import org.example.model.Ticket;
import org.example.model.User;
import org.example.repository.TicketRepository;
import org.example.repository.UserRepository;
import org.example.utils.DBUtil;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class UserService {

    private final UserRepository userRepository;
    private final TicketRepository ticketRepository;

    public UserService(UserRepository userRepository, TicketRepository ticketRepository) {
        this.userRepository = userRepository;
        this.ticketRepository = ticketRepository;
    }

    public String getAllUsers() {
        List<User> users = userRepository.getAll();
        if (users.isEmpty()) {
            return "No users found, sorry!";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Here is a list of all users: \n");
        for (User user : users) {
            sb.append(user.toString()).append("\n");
        }

        return sb.toString();
    }

    public String getUserById(Integer id) {
        if (id <= 0) {
            return "Invalid id, please enter a positive number";
        }

        User user = userRepository.getById(id);
        if (user == null) {
            return String.format("Could not find user with id: %d", id);
        }

        return String.format("User with id: %d found - %s", id, user);
    }

    public String registerUser(User user) {
        String validationError = validateUser(user);
        if (validationError != null) {
            return validationError;
        }

        user = userRepository.create(user);
        if (user == null) {
            return  "Registration process failed, please try again!";
        }

        return "Registration was success!";
    }

    public User loginUser(String userName, String password) {
        if (userName == null || password == null) {
            System.out.println("Username or Password fields cannot be empty");
            return null;
        }

        User user = userRepository.loginUser(userName, password);
        if (user == null) {
            System.out.println("Wrong credentials, please try again or register, if no account exists");
            return null;
        }

        if (!user.getEnabled()) {
            System.out.println( "Access denied, user status: disabled");
            return null;
        }

        System.out.printf("Welcome back, %s", userName);
        return user;
    }


    public String setDisableOrEnableUser(User user, Boolean choice) {
        String validationError = validateUser(user);
        if (validationError != null) {
            return validationError;
        }

        userRepository.setDisableOrEnableUser(user, choice);
        return "User *is enable* status changed successfully ";
    }

    public String deleteUser(Integer id) {
        try {
            if (id <= 0) {
                return "Invalid id, please enter a positive number";
            }

            if (userRepository.getById(id) == null) {
                return String.format("Cannot find user with id: %d", id);
            }

            Connection con = DBUtil.getConnection();
            con.setAutoCommit(false);

            List<Ticket> tickets = ticketRepository.getTicketsByUserId(id);
            if (!tickets.isEmpty()) {
                for (Ticket ticket : tickets) {
                    ticketRepository.deleteTicketPromoRelations(ticket.getId());
                }
            }

            userRepository.deleteTicketsByUserId(id);
            userRepository.delete(id);
            con.commit();

            return String.format("User with id: %d successfully deleted!", id);
        } catch (SQLException ex) {
            return String.format("Error while trying to delete user with id: %d", id);
        }
    }

    private String validateUser(User user) {
        if (userRepository.getUserByUserName(user.getUserName()) != null) {
            return "Username already exists, please enter a different one";
        }

        if (userRepository.getUserByEmail(user.getEmail()) != null) {
            return "Email already exists, please enter a different one";
        }

        if (user.getUserName().isEmpty() || user.getUserName().isBlank() || user.getUserName().length() > 10) {
            return "Username should not be blank or exceed 10 characters";
        }

        if (user.getEmail().isEmpty() || user.getEmail().isBlank() || user.getEmail().length() > 20) {
            return "Email should not be blank or exceed 10 characters";
        }

        if (user.getPassword().isEmpty() || user.getPassword().isBlank() || user.getPassword().length() > 30) {
            return "Password should not be blank or exceed 30 characters";
        }

        return null;
    }
}
