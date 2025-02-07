package hu.unideb.inf.controller;

import hu.unideb.inf.model.User;
import hu.unideb.inf.model.Vehicle;
import hu.unideb.inf.model.VehicleType;
import hu.unideb.inf.repository.VehicleDAO;
import hu.unideb.inf.repository.VehicleDAOImpl;
import hu.unideb.inf.MainApp;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class HomePageController implements Initializable {

    @FXML
    private Button loginBt;
    @FXML
    private Button registerBt;
    @FXML
    private Button searchBt;
    @FXML
    private Button adminPageBt;
    @FXML
    private Button userPageBt;
    @FXML
    private TextField searchTf;
    @FXML
    private Button logoutButton;

    @FXML
    private ComboBox<VehicleType> typeIDCB;
    @FXML
    private TableView<Vehicle> vehicleTable;

    @FXML
    private TableColumn<Vehicle, String> vehicleTypeColumn;
    @FXML
    private TableColumn<Vehicle, String> makeColumn;
    @FXML
    private TableColumn<Vehicle, String> modelColumn;
    @FXML
    private TableColumn<Vehicle, Integer> yearColumn;
    @FXML
    private TableColumn<Vehicle, String> engineColumn;
    @FXML
    private TableColumn<Vehicle, String> fuelTypeColumn;
    @FXML
    private TableColumn<Vehicle, Integer> seatingCapacityColumn;
    @FXML
    private TableColumn<Vehicle, String> priceColumn;
    @FXML
    private TableColumn<Vehicle, Integer> TypeIDColumn;

    private ObservableList<Vehicle> vehicleList = FXCollections.observableArrayList();

    private EntityManagerFactory entityManagerFactory;

    private EntityManager entityManager;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        entityManagerFactory = Persistence.createEntityManagerFactory("br.com.fredericci.pu");
        entityManager = entityManagerFactory.createEntityManager();

        vehicleTypeColumn.setCellValueFactory(new PropertyValueFactory<>("vehicleType"));
        makeColumn.setCellValueFactory(new PropertyValueFactory<>("make"));
        modelColumn.setCellValueFactory(new PropertyValueFactory<>("model"));
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("year"));
        engineColumn.setCellValueFactory(new PropertyValueFactory<>("engine"));
        fuelTypeColumn.setCellValueFactory(new PropertyValueFactory<>("fuelType"));
        seatingCapacityColumn.setCellValueFactory(new PropertyValueFactory<>("seatingCapacity"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        TypeIDColumn.setCellValueFactory(new PropertyValueFactory<>("type"));

        loadDataFromDatabase();
        loadVehicleTypes();
    }

    private void loadVehicleTypes() {
        VehicleDAO vehicleDAO = new VehicleDAOImpl(entityManagerFactory, entityManager);
        List<VehicleType> vehicleTypes = vehicleDAO.getAllVehicleTypes();
    }

    private void loadDataFromDatabase() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        VehicleDAO vehicleDAO = new VehicleDAOImpl(entityManagerFactory, entityManager);

        List<Vehicle> vehicles = vehicleDAO.getVehicles();
        vehicleList.setAll(vehicles);

        entityManager.getTransaction().commit();
        entityManager.close();

        vehicleTable.setItems(vehicleList);
    }

    @FXML
    private void adminPageBtClicked(ActionEvent event) throws IOException {
        User loggedInUser = getLoggedInUser();

        if (loggedInUser != null && loggedInUser.getIsAdmin() == 1) {
            MainApp.setRoot("AdminPage");
        } else {
            showError("Hiba!", "Nincsenek admin jogosultságaid.");
        }
    }

    @FXML
    private void userPageBtClicked(ActionEvent event) throws IOException {
        User loggedInUser = SessionManager.getCurrentUser();

        if (loggedInUser != null) {
            MainApp.setRoot("UserPage");
        } else {
            showError("Hiba!", "Először jelentkezz be!");
        }
    }

    private User getLoggedInUser() {
        return SessionManager.getCurrentUser();
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void loginBtClicked(ActionEvent event) throws IOException {
        MainApp.setRoot("LoginPage");
    }

    @FXML
    private void registerBtClicked(ActionEvent event) throws IOException {
        MainApp.setRoot("RegisterPage");
    }

    @FXML
    private void searchBtClicked(ActionEvent event) {
        String searchText = searchTf.getText().toLowerCase();

        FilteredList<Vehicle> filteredList = new FilteredList<>(vehicleList, vehicle -> {
            if (searchText == null || searchText.isEmpty()) {
                return true;
            }

            return (vehicle.getVehicleType() != null && vehicle.getVehicleType().toLowerCase().contains(searchText)) ||
                    (vehicle.getMake() != null && vehicle.getMake().toLowerCase().contains(searchText)) ||
                    (vehicle.getModel() != null && vehicle.getModel().toLowerCase().contains(searchText)) ||
                    String.valueOf(vehicle.getYear()).contains(searchText) ||
                    (vehicle.getEngine() != null && vehicle.getEngine().toLowerCase().contains(searchText)) ||
                    (vehicle.getFuelType() != null && vehicle.getFuelType().toLowerCase().contains(searchText)) ||
                    String.valueOf(vehicle.getSeatingCapacity()).contains(searchText) ||
                    String.valueOf(vehicle.getPrice()).contains(searchText);
        });

        vehicleTable.setItems(filteredList);
    }

    @FXML
    private void logoutUser(ActionEvent event) {
        SessionManager.logout();
        try {
            hu.unideb.inf.MainApp.setRoot("HomePage");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
