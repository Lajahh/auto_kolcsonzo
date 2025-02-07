package hu.unideb.inf.controller;

import hu.unideb.inf.model.User;
import hu.unideb.inf.repository.UserDAO;
import hu.unideb.inf.repository.UserDAOImpl;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.IOException;

public class RegistrationPageController {

    @FXML
    private TextField usernameTF;
    @FXML
    private PasswordField passwordTf;
    @FXML
    private PasswordField confirmPasswordTF;
    @FXML
    private TextField firstNameTF;
    @FXML
    private TextField lastNameTF;
    @FXML
    private TextField emailTF;
    @FXML
    private Button registerBtn;
    @FXML
    private Button backButton;

    private EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;

    public void initialize() {
        entityManagerFactory = Persistence.createEntityManagerFactory("br.com.fredericci.pu");
        entityManager = entityManagerFactory.createEntityManager();
    }

    @FXML
    private void registerBtnClicked(ActionEvent event) {
        String username = usernameTF.getText();
        String firstName = firstNameTF.getText();
        String lastName = lastNameTF.getText();
        String email = emailTF.getText();
        String password = passwordTf.getText();

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setIsAdmin(0);

        if (username.isEmpty() || password.isEmpty() || firstName.isEmpty() || lastName.isEmpty() || email.isEmpty()) {
            showError("Hiba!", "Kérlek töltsd ki az összes mezőt.");
            return;
        }

        UserDAO userDAO = new UserDAOImpl(entityManagerFactory, entityManager);
        if (userDAO.isusernameTaken(username)) {
            showError("Hiba!","Ez a felhasználónév már foglalt.");
            return;
        }

        userDAO.insertUser(user);

        showSuccess("Sikeres regisztráció!", "A regisztráció sikeresen megtörtént.");
        redirectToHomePage();
    }

    private void redirectToHomePage() {
        try {
            hu.unideb.inf.MainApp.setRoot("HomePage");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showSuccess(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void backToHome(ActionEvent event) {
        try {
            hu.unideb.inf.MainApp.setRoot("HomePage");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
