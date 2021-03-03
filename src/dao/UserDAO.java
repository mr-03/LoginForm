package dao;

import javafx.collections.ObservableList;

public interface UserDAO {
    public void addUser(User user);
    public User getUser(String email);
    public String hashPassword(String password);
    public boolean validateEmail(String email);
    public ObservableList<Contact> getContactList(String email);
    public void addContact(Contact contact);
    public void closeConnection();
}
