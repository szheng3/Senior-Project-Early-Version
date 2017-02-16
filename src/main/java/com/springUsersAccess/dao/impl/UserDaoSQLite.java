package com.springUsersAccess.dao.impl;

/**
 * Created by Alex Almanza on 1/31/17.
 */
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.springUsersAccess.dao.UserDao;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;


public class UserDaoSQLite implements UserDao {

    private SingleConnectionDataSource dataSource ;
    public void setDataSource(SingleConnectionDataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public boolean isUsernameTaken(String username) throws SQLException {
         String query = "Select count(1) from user where username = ?";

         try (Connection connection = dataSource.getConnection();
              PreparedStatement pstmt = connection.prepareStatement(query)) {

             pstmt.setString(1, username);
             try (ResultSet resultSet = pstmt.executeQuery()) {
                 resultSet.next(); // assumes there will be a result set
                 return (resultSet.getInt(1) > 0);
             }
         }
    }

    @Override
    public boolean isValidUser(String username, String password) throws SQLException {
        String query = "SELECT count(1) FROM main.user WHERE username = ? AND password = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);
            try(ResultSet resultSet = pstmt.executeQuery()) {
                resultSet.next();  // assumes there will be a result set
                return (resultSet.getInt(1) > 0);
            }
        }
    }

    @Override
    public void addPassword(String username, String password) throws SQLException{
        String query = "UPDATE user SET password = ? WHERE username = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query)) {

            pstmt.setString(1, password);
            pstmt.setString(2, username);
            pstmt.executeUpdate();
        }
    }

    @Override
    public void addUser(String username, byte[] salt, String hashed_password) throws SQLException{
        // Only adds the new user if the user name isnt already taken
        if (isUsernameTaken(username)) {
            throw new IllegalArgumentException("A record for that username already exists: " + username);
        }

        String query = "INSERT INTO user (username, password, salt) VALUES (?, ?, ?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query)) {

            pstmt.setString(1, username);
            pstmt.setString(2, hashed_password);
            pstmt.setBytes(3, salt);
            pstmt.executeUpdate();
        }
    }

    @Override
    public void addUser(String username, byte[] salt) throws SQLException{
        // Only adds the new user if the user name isnt already taken
        if (isUsernameTaken(username)) {
            throw new IllegalArgumentException("A record for that username already exists: " + username);
        }

        String query = "INSERT INTO user (username, salt) VALUES (?, ?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query)) {

            pstmt.setString(1, username);
            pstmt.setBytes(2, salt);
            pstmt.executeUpdate();
        }
    }

    @Override
    public void updateSalt(String username, byte[] salt) throws SQLException {
        throw new UnsupportedOperationException("Cannot safely change salt yet. Need to be sure password is hased to the new salt.");
//        String updateSQL = "UPDATE user "
//                + "SET salt = ? "
//                + "WHERE username=?";
//
//            try (Connection conn = dataSource.getConnection();
//                 PreparedStatement pstmt = conn.prepareStatement(updateSQL)) {
//            // set parameters
//            pstmt.setBytes(1, salt);
//            pstmt.setString(2, username);
//            pstmt.executeUpdate();
//            System.out.println("Stored Salt in database.");
//        }
    }

    @Override
    public byte[] getSalt(String username) throws SQLException {
        String query = "SELECT salt FROM main.user WHERE username = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query)) {

            pstmt.setString(1, username);

            try(ResultSet resultSet = pstmt.executeQuery()) {
                // The program should not try to access the salt of a tuple with no given salt
                if (!resultSet.next()) {
                    throw new IllegalArgumentException("No salt has been given to this user: " + username);
                }
                return resultSet.getBytes(1);
            }
        }
    }

    @Override
    public String changePassword(String username, String password_current, String password_new) throws SQLException {
        // TODO: Implement password changes
        throw new UnsupportedOperationException("Password changes not yet implemented.");
    }
}
