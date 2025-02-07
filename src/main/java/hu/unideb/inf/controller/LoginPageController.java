package hu.unideb.inf.controller;

import hu.unideb.inf.MainApp;
import hu.unideb.inf.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import java.io.IOException;

public class LoginPageController {

    @FXML
    private TextField usernameTF;
    @FXML
    private PasswordField passwordPF;
    @FXML
    private Button loginButton;
    @FXML
    private Button backButton;

    private EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;

    public void initialize() {
        entityManagerFactory = Persistence.createEntityManagerFactory("br.com.fredericci.pu");
        entityManager = entityManagerFactory.createEntityManager();
    }

    @FXML
    private void loginBtClicked(ActionEvent event) throws IOException {
        String username = usernameTF.getText();
        String password = passwordPF.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showError("Hiba!", "Kérlek töltsd ki mindkét mezőt.");
            return;
        }

        User user = authUser(username, password);

        if (user != null) {
            SessionManager.setCurrentUser(user);

            if (user.getIsAdmin() == 1) {
                MainApp.setRoot("HomePage");
            } else {
                MainApp.setRoot("HomePage");
            }
        } else {
            showError("Hibás bejelentkezés!", "Hibás felhasználónév vagy jelszó.");
        }
    }


    private User authUser(String username, String password) {
        TypedQuery<User> query = entityManager.createQuery("SELECT u FROM User u WHERE u.username = :username AND u.password = :password", User.class);
        query.setParameter("username", username);
        query.setParameter("password", password);

        try {
            return query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    private void openAdminPage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AdminPage.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
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

    @FXML
    private void backToHome(ActionEvent event) {
        try {
            hu.unideb.inf.MainApp.setRoot("HomePage");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
