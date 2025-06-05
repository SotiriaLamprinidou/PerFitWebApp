package Interfaces;

import java.sql.SQLException;
import java.util.List;
import Models.UserData;

public interface IUserService {

    // Authenticates a user using their username and password
    UserData authenticateUser(String username, String password) throws Exception;

    // Checks if a user with the given username or email already exists
    boolean userExists(String username, String email) throws Exception;

    // Registers a user and returns their generated user ID
    int registerAndReturnId(UserData user) throws Exception;

    // Updates the information of an existing user
    void updateUser(UserData user) throws SQLException, ClassNotFoundException;

    // Finds a user by their username
    UserData findUserByUsername(String username) throws SQLException, ClassNotFoundException;

    // Retrieves a list of all users in the system
    List<UserData> listAllUsers() throws SQLException, ClassNotFoundException;

    // Finds a user by their email address
    UserData findUserByEmail(String email) throws SQLException, ClassNotFoundException;

    // Registers a new user without returning the user ID
    void registerUser(UserData user) throws SQLException, ClassNotFoundException;

    // Deletes a user and all related data from the system
    void removeUserAndDependencies(int userId) throws SQLException, ClassNotFoundException;

	UserData findUserById(int id) throws ClassNotFoundException, SQLException;
}
