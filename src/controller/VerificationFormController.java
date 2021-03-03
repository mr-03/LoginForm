package controller;

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
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;

public class VerificationFormController {
    @FXML
    private TextField codeField;

    @FXML
    private Button submitButton;

    UserDAO userDAO = new UserDAOImpl();

    @FXML
    protected void handleSubmitButtonAction(ActionEvent event) {
        Window owner = submitButton.getScene().getWindow();
        String code = EmailService.code;
        if (code.equals(codeField.getText())) {
            userDAO.addUser(EmailService.currentUser);
            switchToMainWindow();
        } else {
            AlertHelper.showAlert(Alert.AlertType.ERROR, owner, "Verification Error!",
                    "Invalid Code");
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

}