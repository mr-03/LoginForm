package controller;

import dao.User;
import dao.UserDAO;
import dao.UserDAOImpl;
import helper.AlertHelper;
import helper.EmailService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;

public class RegistrationFormController {
    @FXML
    private TextField nameField;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button submitButton;

    @FXML
    private Button loginButton;

    UserDAO userDAO = new UserDAOImpl();

    @FXML
    protected void handleSubmitButtonAction(ActionEvent event) {
        Window owner = submitButton.getScene().getWindow();
        if(nameField.getText().isEmpty()) {
            AlertHelper.showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
                    "Please enter your name");
            return;
        }
        if(emailField.getText().isEmpty() || !userDAO.validateEmail(emailField.getText())) {
            AlertHelper.showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
                    "Please enter your valid email id");
            return;
        }
        if(passwordField.getText().isEmpty()) {
            AlertHelper.showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
                    "Please enter a password");
            return;
        }

        if (userDAO.getUser(emailField.getText()) == null) {
            User user = new User(nameField.getText(), emailField.getText(), passwordField.getText());
            EmailService.setUser(user);
            EmailService.sendVerificationCode(emailField.getText());
            switchToVerificationWindow();
            //AlertHelper.showAlert(Alert.AlertType.CONFIRMATION, owner, "Registration Successful!",
            //        "Welcome " + nameField.getText());
        }
        else {
            AlertHelper.showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
                    "A user with this email already exists");
        }
    }

    public void switchToVerificationWindow() {
        userDAO.closeConnection();
        Stage stage = (Stage) loginButton.getScene().getWindow();
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("../fxml/verify_form_reg.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.setTitle("App");
        stage.setScene(new Scene(root, 800, 500));
    }

    public void switchToLoginWindow(ActionEvent actionEvent) {
        userDAO.closeConnection();
        Stage stage = (Stage) loginButton.getScene().getWindow();
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("../fxml/login_form_reg.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.setTitle("App");
        stage.setScene(new Scene(root, 800, 500));
    }
}