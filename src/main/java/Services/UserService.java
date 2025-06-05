package Services;

import Interfaces.IUserService;
import DAO.UserDAO;
import Models.UserData;

import java.sql.SQLException;
import java.util.List;

public class UserService implements IUserService {
    private final UserDAO userDAO = new UserDAO();

    // Checks if a user exists by username or email
    @Override
    public boolean userExists(String username, String email) throws SQLException, ClassNotFoundException {
        return userDAO.findByUsername(username) != null || userDAO.findByEmail(email) != null;
    }

    // Registers a user and returns the newly created ID
    @Override
    public int registerAndReturnId(UserData user) throws SQLException, ClassNotFoundException {
        userDAO.register(user);
        UserData saved = userDAO.findByEmail(user.getEmail());
        if (saved == null) throw new SQLException("Failed to retrieve saved user.");
        return saved.getId();
    }

    // Authenticates user using BCrypt password check
    @Override
    public UserData authenticateUser(String username, String password) throws SQLException, ClassNotFoundException {
        UserData user = userDAO.findByUsername(username);
        return (user != null && org.mindrot.jbcrypt.BCrypt.checkpw(password, user.getPassword())) ? user : null;
    }

    // Returns a list of all users
    @Override
    public List<UserData> listAllUsers() throws SQLException, ClassNotFoundException {
        return userDAO.listAllUsers();
    }

    // Finds user by username
    @Override
    public UserData findUserByUsername(String username) throws SQLException, ClassNotFoundException {
        return userDAO.findByUsername(username);
    }

    // Finds user by email
    @Override
    public UserData findUserByEmail(String email) throws SQLException, ClassNotFoundException {
        return userDAO.findByEmail(email);
    }

    // Registers a new user
    @Override
    public void registerUser(UserData user) throws SQLException, ClassNotFoundException {
        userDAO.register(user);
    }

    // Updates an existing user
    @Override
    public void updateUser(UserData user) throws SQLException, ClassNotFoundException {
        userDAO.update(user);
    }

    // Deletes a user and all associated data
    @Override
    public void removeUserAndDependencies(int userId) throws SQLException, ClassNotFoundException {
        userDAO.deleteUserAndDependencies(userId);
    }

	@Override
	public UserData findUserById(int id) throws ClassNotFoundException, SQLException {
	    return userDAO.findById(id);
	}

}
