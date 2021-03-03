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

public class LoginFormController {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button submitButton;

    @FXML
    private Button registerButton;

    UserDAO userDAO = new UserDAOImpl();



    @FXML
    protected void handleSubmitButtonAction(ActionEvent event) {
        Window owner = submitButton.getScene().getWindow();

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

        User user = userDAO.getUser(emailField.getText());
        if (user == null) {
            AlertHelper.showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
                    "There is no user with this email address");
            return;
        }
        if (user.getPassword().equals(userDAO.hashPassword(passwordField.getText()))) {
            //AlertHelper.showAlert(Alert.AlertType.CONFIRMATION, owner, "Login Successful!",
            //        "Welcome " + user.getFullName());
            EmailService.currentUser = user;
            switchToMainWindow();
        }else {
            AlertHelper.showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
                    "Password is not correct");
        }
    }

    private void switchToMainWindow() {
        userDAO.closeConnection();
        Stage stage = (Stage) submitButton.getScene().getWindow();
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("../fxml/contact_view.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.setTitle("App");
        stage.setScene(new Scene(root, 800, 500));
    }

    public void switchToRegisterWindow(ActionEvent actionEvent) {
        userDAO.closeConnection();
        Stage stage = (Stage) registerButton.getScene().getWindow();
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("../fxml/registration_form_reg.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.setTitle("App");
        stage.setScene(new Scene(root, 800, 500));
    }
}