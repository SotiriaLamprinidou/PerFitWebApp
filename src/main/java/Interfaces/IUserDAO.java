package Interfaces;

import Models.UserData;

import java.sql.SQLException;
import java.util.List;

public interface IUserDAO {

    // Finds and returns a user by their email address
    UserData findByEmail(String email) throws Exception;

    // Finds and returns a user by their username
    UserData findByUsername(String username) throws Exception;

    // Retrieves a list of all users
    List<UserData> listAllUsers() throws Exception;

    // Registers a new user in the system
    void register(UserData user) throws Exception;

    // Registers a new user and returns the generated user ID
    int registerAndReturnId(UserData user) throws Exception;

    // Updates an existing user's information
    void update(UserData user) throws Exception;

    // Deletes a user and all their related data from the system
    void deleteUserAndDependencies(int id) throws Exception;


	UserData findById(int id) throws SQLException, ClassNotFoundException;
}
