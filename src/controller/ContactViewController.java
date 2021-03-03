package controller;


import dao.Contact;
import dao.UserDAO;
import dao.UserDAOImpl;
import helper.EmailService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class ContactViewController {

    @FXML
    private TableView<Contact> tableView;

    @FXML
    private TableColumn<Contact, String> firstName;

    @FXML
    private TableColumn<Contact, String> lastName;

    @FXML
    private TableColumn<Contact, String> email;

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private TextField emailField;

    UserDAO userDAO = new UserDAOImpl();


    @FXML
    public void initialize() {
        firstName.setCellValueFactory(new PropertyValueFactory<Contact, String>("firstName"));
        lastName.setCellValueFactory(new PropertyValueFactory<Contact, String>("lastName"));
        email.setCellValueFactory(new PropertyValueFactory<Contact, String>("Email"));

        tableView.getItems().setAll(getContactList());
    }
    private List<Contact> getContactList(){
        String email = EmailService.currentUser.getEmail();
        return userDAO.getContactList(email);

    }


    public void addPerson(ActionEvent actionEvent) {
        String userId = EmailService.currentUser.getEmail();
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String email = emailField.getText();
        Contact contact = new Contact(userId, firstName, lastName, email);
        userDAO.addContact(contact);
        initialize();
    }
}