package dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.regex.Pattern;

public class UserDAOImpl implements UserDAO {

    Connection connection = null;
    Statement statement = null;


    public UserDAOImpl() {
        try {
            connection = getConnection();
            statement = getStatement();
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void closeConnection() {
        try {
            this.connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:sqlite:userDB.db");
    }

    public Statement getStatement() throws SQLException {
        if (connection == null) {
            throw new SQLException();
        }
        return connection.createStatement();
    }

    @Override
    public void addUser(User user) {
        String hashedPassword = hashPassword(user.getPassword());
        String addStatement = "insert into User values('"+user.getFullName()+"','"+user.getEmail()+"','"+hashedPassword+"')";
        try {
            statement.executeUpdate(addStatement);
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void addContact(Contact contact) {
        String addStatement = "insert into Contact values('"+contact.getUserID()+"','"+contact.getFirstName()+"','"+contact.getLastName()+"','"+contact.getEmail()+"')";
        try {
            statement.executeUpdate(addStatement);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ObservableList<Contact> getContactList(String email) {
        ObservableList<Contact> contactList = FXCollections.observableArrayList();
        ResultSet rs = null;
        try {
            rs = statement.executeQuery("select * from Contact where UserID="+"'"+email+"'");
            while (rs.next()) {
                Contact c = new Contact(rs.getString("UserID"), rs.getString("FirstName"), rs.getString("LastName"), rs.getString("Email"));
                contactList.add(c);
            }
        }catch(SQLException e) {
            e.printStackTrace();
        }

        return contactList;
    }

    @Override
    public boolean validateEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";
        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }

    @Override
    public String hashPassword(String password) {
        StringBuilder hash = new StringBuilder();

        try {
            MessageDigest sha = MessageDigest.getInstance("SHA-1");
            byte[] hashedBytes = sha.digest(password.getBytes());
            char[] digits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                    'a', 'b', 'c', 'd', 'e', 'f' };
            for (int idx = 0; idx < hashedBytes.length; idx++) {
                byte b = hashedBytes[idx];
                hash.append(digits[(b & 0xf0) >> 4]);
                hash.append(digits[b & 0x0f]);
            }
        } catch (NoSuchAlgorithmException e) {

        }

        return hash.toString();

    }


    @Override
    public User getUser(String email) {
        ResultSet rs = null;
        User user = null;
        try {
            rs = statement.executeQuery("select * from user where Email='"+email+"'");
            if (rs.isBeforeFirst()) {
                user = new User(rs.getString("fullName"), rs.getString("email"), rs.getString("password"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;

    }
}
