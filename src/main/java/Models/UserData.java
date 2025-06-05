package Models;

import enums.UserRole;

public class UserData {

	private int id;
	private String name;
	private String username;
	private String password;
	private String email;
	private UserRole role;
	private String createdAt;

	// Default constructor
	public UserData() {}

	// Constructor without ID and createdAt
	public UserData(String name, String username, String password, String email, String role) {
		this.name = name;
		this.username = username;
		this.password = password;
		this.email = email;
		this.role = UserRole.fromString(role);
	}

	// Constructor with ID
	public UserData(int id, String name, String username, String password, String email, String role) {
		this.id = id;
		this.name = name;
		this.username = username;
		this.password = password;
		this.email = email;
		this.role = UserRole.fromString(role);
	}

	// Full constructor
	public UserData(int id, String name, String username, String password, String email, String role, String createdAt) {
		this.id = id;
		this.name = name;
		this.username = username;
		this.password = password;
		this.email = email;
		this.role = UserRole.fromString(role);
		this.createdAt = createdAt;
	}

	// Getters
	public int getId() { return id; }
	public String getName() { return name; }
	public String getUsername() { return username; }
	public String getPassword() { return password; }
	public String getEmail() { return email; }
	public UserRole getRole() { return role; }
	public String getCreatedAt() { return createdAt; }

	// Setters
	public void setRole(UserRole role) { this.role = role; }
}
