package pl.coderslab.entity;

import java.sql.*;
import java.util.Arrays;

public class UserDao extends User {

    private static final String CREATE_USER_QUERY = "INSERT INTO workshop2.users (username, email, password) " +
            "VALUES (?, ?, ?);";
    private static final String READ_USER_QUERY = "SELECT *FROM workshop2.users WHERE id = ?;";
    private static final String UPDATE_USER_QUERY = "UPDATE workshop2.users SET username = ?, " +
            "email = ?, password = ? WHERE id = ?";
    private static final String DELETE_USER_QUERY = "DELETE FROM workshop2.users WHERE id = ?";
    private static final String FIND_ALL_USERS_QUERY = "SELECT * FROM workshop2.users";

    public String hashPassword(String password){
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public User create(User user){

        try(Connection conn = DBUtil.connect()) {
            PreparedStatement stmt = conn.prepareStatement(CREATE_USER_QUERY, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, user.getUserName());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, hashPassword(user.getPassword()));
            stmt.executeUpdate();
            ResultSet resultSet = stmt.getGeneratedKeys();
            if (resultSet.next()){
                long id = resultSet.getLong(1);
                System.out.println("\nInserted ID: "+id);
            }
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()){
                user.setId(rs.getInt(1));
            }
            System.out.println("\n== New user has been INSERTED successfully ==");
            return user;

        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public User read(int userId){

        try (Connection conn = DBUtil.connect()){
            PreparedStatement stmt = conn.prepareStatement(READ_USER_QUERY);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()){
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setEmail(rs.getString("email"));
                user.setUserName(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                return user;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public void update(User user){
        try (Connection conn = DBUtil.connect()){
            PreparedStatement stmt = conn.prepareStatement(UPDATE_USER_QUERY);
            stmt.setString(1, user.getUserName());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, this.hashPassword(user.getPassword()));
            stmt.setInt(4, user.getId());
            stmt.executeUpdate();
            System.out.println("== User has been UPDATED successfully ==");
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void delete(int userId){
        try (Connection conn = DBUtil.connect()){
            PreparedStatement stmt = conn.prepareStatement(DELETE_USER_QUERY);
            stmt.setInt(1, userId);
            stmt.executeUpdate();
            System.out.println("== User has been DELETED successfully ==");
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    private User[] addToArray(User user, User[] users) {
        User[] newUsers = Arrays.copyOf(users, users.length+1);
        newUsers[newUsers.length-1] = user;
        return newUsers;
    }

    public User[] findAll(){
        try (Connection conn = DBUtil.connect()){
            User[] users = new User[0];
            PreparedStatement stmt = conn.prepareStatement(FIND_ALL_USERS_QUERY);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()){
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUserName(rs.getString("username"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                users = addToArray(user, users);
            }
            return users;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }
}